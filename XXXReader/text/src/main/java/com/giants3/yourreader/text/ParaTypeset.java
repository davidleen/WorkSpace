package com.giants3.yourreader.text;

/**
 * 段落排版数据
 */
public class ParaTypeset {
    ParagraghData paragraghData;
    float[] xPositions;
    int[] lineHead;
    int lineCount = -1;
    int wordCount;

    public int getLineCount() {
        if (lineCount == -1) {


            int line = 0;
            for (int i = 0; i < lineHead.length; i++) {

                if (lineHead[i] == -1) {
                    break;
                }
                line++;
            }
            lineCount = line;

        }
        return lineCount;
    }
}
