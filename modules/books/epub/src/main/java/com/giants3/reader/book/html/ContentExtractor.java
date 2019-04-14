package com.giants3.reader.book.html;


import com.giants3.android.frame.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.giants3.reader.book.html.Token.TAG_H1_STRING;
import static com.giants3.reader.book.html.Token.TAG_H2_STRING;
import static com.giants3.reader.book.html.Token.TAG_H3_STRING;
import static com.giants3.reader.book.html.Token.TAG_H4_STRING;
import static com.giants3.reader.book.html.Token.TAG_H5_STRING;
import static com.giants3.reader.book.html.Token.TAG_H6_STRING;

/**
 * html 内容抽取
 * <p>
 * <p>
 * 字符解析方式
 * <p>
 * <p>
 * 可选方案    XmlPullParser
 */
public class ContentExtractor {


    public static String getContent(String html) {
        StringBuffer sb = new StringBuffer(html.length());
        char[] data = html.toCharArray();
        int start = getStart(data);
        boolean isPreTag = false;
        Token token = null;
        Token lastToken = null;
        for (; ; ) {
            token = extract(data, start, isPreTag);
            if (token == null)
                break;
            isPreTag = token.isPreTag();
            if (token.getType() == Token.TOKEN_IMG) {
                String classToken = token.getAttribut("class");
                //sb = sb.append("\n" + Token.TAG_CODE_START_IMG+(classToken==null?"":(classToken+"_"))  + token.getAttribut("src") + Token.TAG_CODE_END+"\n");
                String src = token.getAttribut("src");
                sb = sb.append("\n" + Token.TAG_CODE_START_IMG + src + Token.TAG_CODE_END + "\n");
                if (!StringUtil.isEmpty(classToken)) {
                    addBitmapType(src, classToken);
                }

            }
//            else if (token.getType() == Token.TOKEN_STYLE) {
//
//
//                String text=token.getText();
//                parseStyle(text);
//
//
//            }
            else {
                String lastHtm = lastToken == null ? "" : lastToken.toString();

                if (!(lastHtm.contains(TAG_H1_STRING) || lastHtm.contains(TAG_H2_STRING)
                        || lastHtm.contains(TAG_H3_STRING) || lastHtm.contains(TAG_H4_STRING)
                        || lastHtm.contains(TAG_H5_STRING) || lastHtm.contains(TAG_H6_STRING))) {
                    sb = sb.append(token.getText());
                }


            }
            start += token.getLength();
            lastToken = token;
        }

        while (sb.indexOf("\n") == 0) {
            sb.delete(0, 1);
        }

        return sb.toString();
    }

    /**
     * 获取差额字符串
     *
     * @param htmlPath
     * @param baseUri
     * @param uri
     * @return
     */
    public static String getUriDri(String htmlPath, String baseUri, String uri) {
        String tempUri = uri;
        int indexHtml = htmlPath.lastIndexOf(File.separator);
        int indexBase = baseUri.length();
        if (!baseUri.endsWith(File.separator)) {
            indexBase++;
        }
        if (indexHtml + 1 > indexBase) {//+1-->indexOf和lenght的偏差
            tempUri = htmlPath.substring(indexBase, indexHtml + 1) + uri;//+1-->File.separator;
        }

        return tempUri;
    }

    private static int getStart(char[] data) {
        int start = 0;

        if (data != null && data.length > 0) {
            if (data[0] == 0xfeff) {
                start = 1;
            }
        }

        return start;
    }

    private static Token extract(char[] data, int start, boolean previousIsPre) {
        if (start >= data.length)
            return null;
        // try to read next char:
        char c = data[start];
        if (c == '<') {
            // this is a tag or comment or script:
            int end_index = indexOf(data, start + 1, '>');
            if (end_index == (-1)) {
                // the left is all text!
                return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
            }
            String s = new String(data, start, end_index - start + 1);
            // now we got s="<...>":
            if (s.startsWith("<!--")) { // this is a comment!
                int end_comment_index = indexOf(data, start + 1, "-->");
                if (end_comment_index == (-1)) {
                    // illegal end, but treat as comment:
                    return new Token(Token.TOKEN_COMMENT, data, start, data.length, previousIsPre);
                } else
                    return new Token(Token.TOKEN_COMMENT, data, start, end_comment_index + 3, previousIsPre);
            }
            String s_lowerCase = s.toLowerCase();
            if (s_lowerCase.startsWith("<script")) { // this is a script:
                int end_script_index = indexOf(data, start + 1, "</script>");
                if (end_script_index == (-1))
                    // illegal end, but treat as script:
                    return new Token(Token.TOKEN_SCRIPT, data, start, data.length, previousIsPre);
                else
                    return new Token(Token.TOKEN_SCRIPT, data, start, end_script_index + 9, previousIsPre);
            } else if (s_lowerCase.startsWith("<style")) {
                int end_script_index = indexOf(data, start + 1, "</style>");
                if (end_script_index == (-1))
                    // illegal end, but treat as script:
                    return new Token(Token.TOKEN_STYLE, data, start, data.length, previousIsPre);
                else
                    return new Token(Token.TOKEN_STYLE, data, start, end_script_index + 8, previousIsPre);
            } else if (s_lowerCase.startsWith("<title")) {
                int end_script_index = indexOf(data, start + 1, "</title>");
                if (end_script_index == (-1))
                    // illegal end, but treat as script:
                    return new Token(Token.TOKEN_TITLE, data, start, data.length, previousIsPre);
                else
                    return new Token(Token.TOKEN_TITLE, data, start, end_script_index + 8, previousIsPre);
            } else if (s_lowerCase.startsWith("<head")) {
                int end_script_index = indexOf(data, start + 1, "</head>");
                if (end_script_index == (-1))
                    // illegal end, but treat as script:
                    return new Token(Token.TOKEN_HEAD, data, start, data.length, previousIsPre);
                else
                    return new Token(Token.TOKEN_HEAD, data, start, end_script_index + 7, previousIsPre);
            } else if (s_lowerCase.startsWith("<img")) {
                return new Token(Token.TOKEN_IMG, data, start, start + s.length(), previousIsPre);
            } else { // this is a tag:
                return new Token(Token.TOKEN_TAG, data, start, start + s.length(), previousIsPre);
            }
        }
        // this is a text:
        int next_tag_index = indexOf(data, start + 1, '<');
        if (next_tag_index == (-1))
            return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
        return new Token(Token.TOKEN_TEXT, data, start, next_tag_index, previousIsPre);
    }

    private static int indexOf(char[] data, int start, String s) {
        char[] ss = s.toCharArray();
        for (int i = start; i < (data.length - ss.length); i++) {
            // compare from data[i] with ss[0]:
            boolean match = false;
            for (int j = 0; j < ss.length; j++) {
                if (data[i + j] == ss[j] || data[i + j] == ss[j] - 32) {
                    match = true;
                } else {
                    match = false;
                    break;
                }
            }
            if (match)
                return i;
        }
        return (-1);
    }

    private static int indexOf(char[] data, int start, char c) {
        for (int i = start; i < data.length; i++) {
            if (data[i] == c)
                return i;
        }
        return (-1);
    }


    /**
     * 阅读界面中 图片的类型   目前【无类型】【封面】
     */
    public static Map<String, String> bitmapTypes = new HashMap<String, String>();


    public static void addBitmapType(String src, String type) {
        bitmapTypes.put(src, type);
    }

    public static void clearBitmapTypes() {
        bitmapTypes.clear();
    }

    public static String getBitmapType(String src) {
        return bitmapTypes.get(src);
    }


}

