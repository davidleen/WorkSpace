package com.giants3.reader.book.html;

import android.text.Html;

import com.giants3.android.frame.util.Log;

import java.util.HashMap;
import java.util.Map;


public class Token {
    public static final char TAG_CODE_START_IMG = 0x1e;
    public static final char TAG_CODE_END = 0x17;
    private static final String[] interpunctionArrays = new String[]{"'", "\""};

    public static final int TOKEN_TEXT = 0; // html text.
    public static final int TOKEN_COMMENT = 1; // comment like <!--
    // comments... -->
    public static final int TOKEN_TAG = 2; // tag like <pre>, <font>,
    // etc.
    public static final int TOKEN_SCRIPT = 3;
    public static final int TOKEN_TITLE = 4;
    public static final int TOKEN_STYLE = 5;
    public static final int TOKEN_HEAD = 6;
    public static final int TOKEN_IMG = 7;

    private static final char[] TAG_BR = "<br".toCharArray();
    private static final char[] TAG_P = "<p".toCharArray();
    private static final char[] TAG_LI = "<li".toCharArray();
    private static final char[] TAG_PRE = "<pre".toCharArray();
    private static final char[] TAG_HR = "<hr".toCharArray();
    private static final char[] TAG_H1 = "<h1".toCharArray();
    private static final char[] TAG_H2 = "<h2".toCharArray();
    private static final char[] TAG_H3 = "<h3".toCharArray();
    private static final char[] TAG_H4 = "<h4".toCharArray();
    private static final char[] TAG_H5 = "<h5".toCharArray();
    private static final char[] TAG_H6 = "<h6".toCharArray();
    private static final char[] TAG_DIV = "<div".toCharArray();
    private static final char[] END_TAG_TD = "</td>".toCharArray();
    private static final char[] END_TAG_TR = "</tr>".toCharArray();
    private static final Map<String, String> attribute = new HashMap<String, String>();

    public static final String TAG_H1_STRING = "<h1";
    public static final String TAG_H2_STRING = "<h2";
    public static final String TAG_H3_STRING = "<h3";
    public static final String TAG_H4_STRING = "<h4";
    public static final String TAG_H5_STRING = "<h5";
    public static final String TAG_H6_STRING = "<h6";


    private int type;
    private String html; // original html
    private String text = null; // text!
    private int length = 0; // html length
    private boolean isPre = false; // isPre tag?

    public Token(int type, char[] data, int start, int end, boolean previousIsPre) {
        this.type = type;
        this.length = end - start;
        this.html = new String(data, start, length);
        parseText(previousIsPre);
        parseAttribut();
    }

    public int getLength() {
        return length;
    }

    public boolean isPreTag() {
        return isPre;
    }

    public int getType() {
        return type;
    }

    private void parseAttribut() {
        if (type == TOKEN_TAG || type == TOKEN_IMG) {
            String[] arrays = withoutAngularBrackets(html).split(" ");
            if (arrays != null && arrays.length > 0) {
                for (String arr : arrays) {
                    String temp = arr.trim();
                    int eqitSize = temp.indexOf("=");
                    if (eqitSize > 1) {
//						attribute.put(temp.substring(0, eqitSize).toLowerCase(),
//								withoutForeAftInterpunction(temp.substring(eqitSize+1)));//+1 ���Ⱥ�
                        String key = temp.substring(0, eqitSize).toLowerCase();
                        String value = temp.substring(eqitSize + 1);
                        if (interpunctionArrays != null && interpunctionArrays.length > 0) {
                            int errCount = 0;
                            for (int i = 0; i < interpunctionArrays.length; i++) {
                                if (!value.endsWith(interpunctionArrays[i])) {
                                    errCount++;
                                }
                            }
                            if (errCount == interpunctionArrays.length) {
                                value = value.substring(0, value.length() - 1);
                            }
                        }
                        attribute.put(key,
                                withoutForeAftInterpunction(value));
                    }
                }
            }
        }
    }

    public String getAttribut(String attrib) {
        return attribute.get(attrib.toLowerCase());
    }

    private void parseText(boolean previousIsPre) {
        if (type == TOKEN_TAG) {
            char[] cs = html.toCharArray();
            if (compareTag(TAG_BR, cs) || compareTag(TAG_P, cs) || compareTag(TAG_H1, cs) || compareTag(TAG_H2, cs) ||
                    compareTag(TAG_H3, cs) || compareTag(TAG_H4, cs) || compareTag(TAG_H5, cs) || compareTag(TAG_H6, cs))
                text = "\n";
            else if (compareTag(TAG_LI, cs))
                text = "\n";
            else if (compareTag(TAG_PRE, cs))
                isPre = true;
            else if (compareTag(TAG_HR, cs))
                text = "\n----------\n";
            else if (compareString(END_TAG_TD, cs))
                text = "\t";
            else if (compareString(END_TAG_TR, cs))
                text = "\n";
        }
        // text token:
        else if (type == TOKEN_TEXT) {
            text = toText(html, previousIsPre);
        }
    }

    public String getText() {
        return text == null ? "" : text;
    }

    private String toText(String html, final boolean isPre) {
        char[] cs = html.toCharArray();
        StringBuffer buffer = new StringBuffer(cs.length);
        int start = 0;
        boolean continueSpace = false;
        char current, next;
        for (; ; ) {
            if (start >= cs.length)
                break;
            current = cs[start]; // read current char
            if (start + 1 < cs.length) // and next char
                next = cs[start + 1];
            else
                next = '\0';
            if (current == ' ') {
                if (isPre || !continueSpace)
                    buffer = buffer.append(' ');
                continueSpace = true;
                // continue loop:
                start++;
                continue;
            }
            // not ' ', so:
            if (current == '\r' && next == '\n') {
                if (isPre)
                    buffer = buffer.append('\n');
                // continue loop:
                start += 2;
                continue;
            }
            if (current == '\n' || current == '\r') {
                if (isPre)
                    buffer = buffer.append('\n');
                // continue loop:
                start++;
                continue;
            }

            if (current == '\t') {
                if (isPre)
                    buffer = buffer.append('\t');
                // continue loop:
                start++;
                continue;
            }

            // cannot continue space:
            continueSpace = false;
            if (current == '&') {
                // maybe special char:
                int length = readUtil(cs, start, ';', 10);
                if (length == (-1)) { // just '&':
                    buffer = buffer.append('&');
                    // continue loop:
                    start++;
                    continue;
                } else { // check if special character:
                    String spec = new String(cs, start, length);
                    String specChar = Html.fromHtml(spec).toString();
                    if (specChar != null) { // special chars!
                        buffer = buffer.append(specChar);
                        // continue loop:
                        start += length;
                        continue;
                    } else { // check if like '?':
                        if (next == '#') { // maybe a char
                            String num = new String(cs, start + 2, length - 3);
                            try {
                                int code = Integer.parseInt(num);
                                if (code > 0 && code < 65536) { // this is a
                                    // special char:
                                    buffer = buffer.append((char) code);
                                    // continue loop:
                                    start += num.length() + 3;
                                    continue;
                                }
                            } catch (Exception e) {
                                Log.e(e);
                            }
                            // just normal char:
                            buffer = buffer.append("&#");
                            // continue loop:
                            start += 2;
                            continue;
                        } else { // just '&':
                            buffer = buffer.append('&');
                            // continue loop:
                            start++;
                            continue;
                        }
                    }
                }
            } else { // just a normal char!
                buffer = buffer.append(current);
                // continue loop:
                start++;
                continue;
            }
        }
        return buffer.toString();
    }

    private int readUtil(final char[] cs, final int start, final char util, final int maxLength) {
        int end = start + maxLength;
        if (end > cs.length)
            end = cs.length;
        for (int i = start; i < end; i++) {//start + maxLength
            if (cs[i] == util) {
                return i - start + 1;
            }
        }
        return (-1);
    }

    // compare standard tag "<input" with tag "<INPUT value=aa>"
    private boolean compareTag(final char[] ori_tag, char[] tag) {
        if (ori_tag.length >= tag.length)
            return false;
        for (int i = 0; i < ori_tag.length; i++) {
            if (Character.toLowerCase(tag[i]) != ori_tag[i])
                return false;
        }
        // the following char should not be a-z:
        if (tag.length > ori_tag.length) {
            char c = Character.toLowerCase(tag[ori_tag.length]);
            if (c < 'a' || c > 'z')
                return true;
            return false;
        }
        return true;
    }


    private boolean compareString(final char[] ori, char[] comp) {
        if (ori.length > comp.length)
            return false;
        for (int i = 0; i < ori.length; i++) {
            if (Character.toLowerCase(comp[i]) != ori[i])
                return false;
        }
        return true;
    }

    public String toString() {
        return html;
    }

    private String withoutForeAftInterpunction(String url) {
        return withoutForeAftInterpunction(url, null);
    }

    private String withoutForeAftInterpunction(String str, String sign) {
        String tempStr = str;
        if (isEmpty(sign)) {
            if (interpunctionArrays != null && interpunctionArrays.length > 0) {
                for (String interpunction : interpunctionArrays) {
                    tempStr = withoutForeAftSingleSign(tempStr, interpunction);
                }
            }
        } else {
            tempStr = withoutForeAftSingleSign(tempStr, sign);
        }

        return tempStr;
    }

    private String withoutForeAftSingleSign(String str, String sign) {
        String tempStr = str;

        if (!isEmpty(str) && str.startsWith(sign) && str.endsWith(sign) && str.length() > 1) {
            tempStr = str.substring(1, str.length() - 1);
        }

        return tempStr;
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() < 0;
    }

    private String withoutAngularBrackets(String url) {
        String tempUrl = url;
        if (!isEmpty(url)) {
            int left = url.indexOf("<");
            int right = url.indexOf(">");
            if (left >= 0 && right > 0 && right > left) {
                tempUrl = url.substring(left + 1, right);
            }
        }

        return tempUrl;
    }
}

