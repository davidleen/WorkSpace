package com.giants3.yourreader.text.composite;

import java.util.Arrays;

/**
 * 段落解析
 *
 * 需要参数
 */
public class ParaCompositor {



    public int orientation = 0;


    public  static final char CHAR_TAB = 9853;


    public static class Compositor {
     public    String data;
        public float[] positions;
        public int[] lineIndex;
        public int lineCount;
    }

    /**
     * @param data
     * @param wordGap
     * @param indentCount
     * @param orientation
     */
    public static Compositor composite(String data, float width, float wordGap, int
            indentCount, int orientation, TextMeasure textMeasure) {


        float startPos = 0;

        if (data == null || data.length() == 0) return null;

        int dataLength = data.length();

        Mark mark = new Mark();
        mark.lineIndex = 0;

        float[] set = new float[dataLength];

        //缩进处理
        float pos = startPos + indentCount * textMeasure.getTextWidth('a', orientation);

        do {


            char currentChar = data.charAt(mark.index);
            switch (currentChar) {
                case '\n':
                case '\t':


                case '\r':
                    //换行处理
                    set[mark.index] = CHAR_TAB;
                    mark.lineIndex++;
                    headIndex[mark.lineIndex] = mark.index + 1;
                    pos=startPos;

                    continue;


                default:


                    float textWidth = textMeasure.getTextWidth(currentChar, orientation);
                    if (pos + textWidth <= width) {
                        set[mark.index] = pos;
                        pos += textWidth;
                        if (mark.index < dataLength - 1 && !isCommonChar(currentChar)) {

                            if (pos + wordGap >= width) {


                                adjustCurrentLine(data, width, set, mark,  orientation, textMeasure);
                                pos=startPos;


                            } else {
                                pos += wordGap;
                            }
                        } else {
                            //no need adjust.
                        }
                    } else {



                        mark.index--;
                        adjustCurrentLine(data, width, set, mark,  orientation, textMeasure);
                        pos=startPos;


                    }


                    break;
            }


        } while ((++mark.index < dataLength));

        Compositor compositor = new Compositor();
        compositor.positions = set;
        int lineCount=mark.lineIndex+1;
        int[] indexLine = new int[lineCount];
        System.arraycopy(headIndex, 0, indexLine, 0, indexLine.length);
        compositor.lineIndex = indexLine;
        compositor.lineCount = lineCount;

        return compositor;

    }


    public static class Mark {
        int index;
        int lineIndex;

    }

    /**
     * 调整当前行尾的处理。
     *
     * @param data
     * @param set
     * @param mark
     * @param startPos
     */
    private static void adjustCurrentLine(String data, float width, float[] set, Mark mark,   int orientation, TextMeasure textMeasure) {

        int dataLength = data.length();

        //当前是行首字符，或者 下一个字符是行尾字符。
        if (shouldNotLineTrailChar(data.charAt(mark.index)) || shouldNotLineHeadChar(data.charAt(mark.index + 1))) {
            //取消当前字符排版， 下放到下一行
            mark.index--;
        }
        //调整当前行
        int countThisLine = mark.index - headIndex[mark.lineIndex];

        float additionGap = width - set[mark.index] - textMeasure.getTextWidth(data.charAt(mark.index), orientation);


        if(additionGap>0) {
            float additionAverageGap = additionGap / (countThisLine );
            //额外空间平分到每个字符上。
            int step=1;
            for (int j = headIndex[mark.lineIndex] + 1; j <= mark.index; j++) {
                set[j] += additionAverageGap*step++;
            }
        }




        mark.lineIndex++;

        if (mark.lineIndex >= headIndex.length) {
            headIndex = Arrays.copyOf(headIndex, headIndex.length * 2);
        }
        headIndex[mark.lineIndex] = mark.index + 1;



    }

    /**
     * 是否文本字符判断 0-10  a-z  A-Z  :;<=>?@  [/]^-、 非汉字等。
     *
     * @param c
     * @return
     */
    private static final boolean isCommonChar(char c) {
        return ((c >= 0x30 && c <= 0x7a));
    }

    /**
     * 是否不应显示在行首的字符
     *
     * @param c
     * @return
     */
    private static boolean shouldNotLineHeadChar(char c) {

        switch (c) {
            case '；':
            case ';':
            case '、':
            case '‘':
            case ':':
            case '：':
            case '。':
            case '，':
            case '？':
            case '！':
            case '”':
            case '）':
            case '}':
            case '》':
            case '%':
            case ',':
            case '?':
            case '!':
            case ')':
            case ']':
                break;
            default:
                return false;
        }
        return true;

    }

    /**
     * 是否不应该显示在行尾字符
     *
     * @param c
     * @return
     */
    private static boolean shouldNotLineTrailChar(char c) {
        switch (c) {
            case '、':
            case '’':
            case ':':
            case '：':
            case '{':
            case '（':
            case '《':
            case '“':
            case '(':
            case '[':
                break;
            default:
                return false;
        }
        return true;
    }


    public float[] positions;
    public static int[] headIndex = new int[100];
    public float wordGap;

    public float width;
}
