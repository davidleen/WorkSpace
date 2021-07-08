package com.giants3.yourreader.text;

import android.graphics.Paint;

import com.giants3.android.frame.util.Log;
import com.giants3.reader.book.html.Token;
import com.xxx.reader.turnner.sim.SettingContent;

/**
 * @author Administrator
 */
public class TXTtypeset {

    public static final int LAST_PARAGRAH_LAST_LINE_EMPTY = 1;
    public static final int LAST_PARAGRAH_LAST_LINE_NO_EMPTY = 0;
    public static final int NEXT_PARAGRAH_FIRST_LINE_EMPTY = -1;
    public static final int NEXT_PARAGRAH_FIRST_LINE_NO_EMPTY = 0;
    public static final int UNKNOW = 2;
    public static final float CHAPR_REPLACE_FLAG = 0.01f;
    public static final int MAX_CHANGE = 3;


    public float textSize=0;
    private   byte[] charWidths = null;


    private int h_spacing;
    public static int TYPESET_FLAG = 9853;
    public static int TYPESET_FLAG_IMG = 9955;
    public static int TYPESET_FLAG_TAG_IMG = 9900;
    public static int TYPESET_FLAG_TAG_COMPRESS = 9909;
    private Paint paint = null;
    private int screenWidth;
    private boolean measureDirect = false;
    private static int chineseCharWidth = 0;
    private float PADDING_LEFT = 10;
    private float PADDING_RIGHT = 10;
                                              public TXTtypeset(float textSize, int w, int hg )
                                                {
                                                    this(textSize,w,hg,null);
                                                }
    public TXTtypeset(float textSize, int w, int hg ,float[] padding) {
        if(padding!=null)
        {
            PADDING_LEFT=padding[0];
            PADDING_RIGHT=padding[2];

        }

        h_spacing = hg;
        screenWidth = w;
        this.textSize = textSize;
        paint=new Paint();
        paint.setTextSize(textSize);
        if (charWidths == null) {
            charWidths = new byte[256 * 256];
            clearTypeset();
            chineseCharWidth = (int) paint.measureText("和");
            charWidths['\t'] = (byte) (paint.getTextSize() * 2f + h_spacing * 2f);
        }

    }


    public void setMeasureDirect(boolean measureDirect) {
        this.measureDirect = measureDirect;
    }


    private StringBuffer processSpecialChar(StringBuffer sb) {
        String str = sb.toString().trim();
        str = str.replace("《", "");
        str = str.replace("》", "");
        str = str.replace("【", "");
        str = str.replace("】", "");
        str = str.replace("“", "");
        str = str.replace("”", "");
        if (str.startsWith("（")) {
            str = str.replace("（", "");
            str = str.replace("）", "");
        }
        if (str.length() <= 1) {
            str += "  ";
        }
        return new StringBuffer(str);
    }

    private StringBuffer titleEndProcess(StringBuffer data, float[] set, int currentIndex) {
        float endCharWidth = paint.measureText(".");
        if (set[currentIndex - 2] + endCharWidth * 3 > screenWidth - PADDING_RIGHT) {
            currentIndex -= 1;
        }

        set[currentIndex - 1] = set[currentIndex - 2] + endCharWidth;
        set[currentIndex] = set[currentIndex - 1] + endCharWidth;
        data.setCharAt(currentIndex - 2, '.');
        data.setCharAt(currentIndex - 1, '.');
        data.setCharAt(currentIndex, '.');
        data.delete(currentIndex + 1, data.length());
        return data;
    }

    /**
     * @param data
     * @param linehead
     * @param paraEmptyLineState // 相邻段落空行情况
     * @return
     */
    public float[] typeset(StringBuffer data, int[] linehead, int paraEmptyLineState) {


        return typeset(data, linehead, paraEmptyLineState, true);
    }

    /**
     * @param data
     * @param linehead
     * @param paraEmptyLineState // 相邻段落空行情况
     * @param indentFirst        是否首行缩进
     * @return
     */
    public float[] typeset(StringBuffer data, int[] linehead, int paraEmptyLineState, boolean indentFirst) {
        int firstChar = getFirstChar(data);
        float x = PADDING_LEFT;
        //首行缩进判断
        int indentCount = indentFirst ? getIndentCount() : 0;
        if (firstChar == 0 && indentCount != 0) {
            x = PADDING_LEFT + ((this.getTextWidth('　') + h_spacing) * indentCount);
        }
        int dataLength = data.length();
        float[] set = new float[dataLength > 0 ? dataLength : 1];
        set[0] = x;
        int line = 0;
        linehead[0] = 0;
        int count = 0;
        boolean allWord = true;
        int tx = 0;
        float m = 0;
        char currentChar = 0;
        for (int i = 0; i < dataLength; ) {
            currentChar = data.charAt(i);
            switch (currentChar) {
                case '\n':
                    set[i] = TYPESET_FLAG;
                    if (SettingContent.getInstance().getSettingSpaceType() != 0) {
                        emptyLineProcess(data, set, linehead, paraEmptyLineState);
                    }
                    return set;
                case '\t':
                    set[i] = TYPESET_FLAG;
                    break;
                case '\r':
                    set[i] = TYPESET_FLAG;
                    i++;
                    continue;
                case Token.TAG_CODE_START_IMG:
                    set[i] = TYPESET_FLAG_TAG_IMG;
                    linehead[++line] = i;
                    while (++i < dataLength && data.charAt(i) != Token.TAG_CODE_END) {
                        set[i] = TYPESET_FLAG_IMG;
                    }
                    if (i + 1 < dataLength) {
                        set[i++] = TYPESET_FLAG_TAG_IMG;
                    }
                    continue;
                default:
                    break;
            }

            if (allWord && !isWord(currentChar)) {
                allWord = false;
            }
            tx = this.getTextWidth(currentChar);
            m = screenWidth - PADDING_RIGHT - x - tx;
            if (m <= 0) {
                int index = -1;
                if (!allWord && i != 0 && isWord(currentChar) && isWord(data.charAt(i - 1))) {
                    i--;
                    while (isWord(data.charAt(i - 1))) {
                        i--;
                        if (i <= 0) {
                            break;
                        }
                    }
                    x = PADDING_LEFT;
                    allWord = true;
                    set[i] = x;
                    line++;
                    linehead[line] = i;
                    count = 0;
                    continue;
                } else if (currentChar == '\t') {//末尾'\t'不调节,只换行
                    i++;
                    x = PADDING_LEFT;
                    allWord = true;
                    if (i < dataLength) {
                        set[i] = x;
                        line++;
                        if (currentChar != 0) {
                            linehead[line] = i;
                        }
                    }
                    count = 0;
                    continue;
                } else {
                    index = processHeadEndChar(data, m, linehead, set, line, i);
                    if (index != -1) {
                        i = index + 1;
                    } else {
                        int c = 0;
                        for (int ii = linehead[line]; ii < i; ii++) {
                            if (data.charAt(ii) != '\t' && count - 1 != 0) {
                                set[ii] += ((screenWidth - PADDING_RIGHT - x + h_spacing) / (count - 1)) * c;
                            }
                            c++;
                        }
                    }
                }
                if (i >= dataLength) {
                    break;
                }
                x = PADDING_LEFT;
                allWord = true;
                set[i] = x;

                currentChar = data.charAt(i);
                if (index == -1 || currentChar != '\n' && currentChar != '\t'
                        && currentChar != '\r' && currentChar != 0 && currentChar != Token.TAG_CODE_START_IMG) {
                    line++;
                    if (line >= linehead.length) {
                        Log.e("Out of range line: " + line);
                        return null;
                    }
                    linehead[line] = i;
                }
                count = 0;
                continue;
            }
            count++;

            if (isWord(currentChar) && (((i + 1) < dataLength) && isWord(data.charAt(i + 1))))
                x += tx;
            else
                x += tx + h_spacing;

            if ((i + 1) < set.length) {
                if (indentCount != 0 && i + 1 <= firstChar) {
                    if (i + 1 < firstChar) {
                        x = (float) (PADDING_LEFT + (i + 1) * 0.1);    // 绘制时会进行换行判断，所以要加上一些偏移，否则会被误认为换行
                    } else {
                        x = PADDING_LEFT + ((this.getTextWidth('　') + h_spacing) * indentCount);
                    }
                }
                set[i + 1] = x;
            }
            i++;
        }

        if (SettingContent.getInstance().getSettingSpaceType() != 0) {
            emptyLineProcess(data, set, linehead, paraEmptyLineState);
        }
        return set;
    }

    private int processHeadEndChar(StringBuffer data, float m, int[] lineHead, float typeSet[], int line, int currentIndex) {
        char cruuentChar = data.charAt(currentIndex);
        boolean isInvalidHead = isInvalidHeadChar(cruuentChar);
        boolean isInvalidEnd = isInvalidEndChar(data.charAt(currentIndex - 1));

        if (!isInvalidHead && !isInvalidEnd) {
            return -1;
        }

        int backIndex = -1;    // 往前数，适合行首行尾规则的位置
        int forwardIndex = -1; // 往后数，适合行首行尾规则的位置
        int i = 0;
        for (i = currentIndex - 1; (i > lineHead[line]) && (currentIndex - 1 - i <= MAX_CHANGE); i--) {
            if (!isInvalidEndChar(data.charAt(i)) && (i + 1 == data.length() || !isInvalidHeadChar(data.charAt(i + 1)))) {
                backIndex = i;
                break;
            }
        }
        for (i = currentIndex; i <= currentIndex + MAX_CHANGE && i < data.length(); i++) {
            if (!isInvalidEndChar(data.charAt(i)) && (i + 1 == data.length() || !isInvalidHeadChar(data.charAt(i + 1)))) {
                forwardIndex = i;
                break;
            }
        }
        if (backIndex == forwardIndex) {    // 排版失败，按原来方案排版。
            Log.d("process invalid head or end fail ... ");
            return -1;
        }

        float offset = 0;
        float charOffset = 0;
        int replayceCharCount = 0;
        int cannotCutHSpacingCount = 0;    //字符间没有间距的数量
        if (forwardIndex != -1) {
            for (int j = lineHead[line]; j <= forwardIndex; j++) {
                if ((j + 1 <= forwardIndex) && (typeSet[j + 1] - typeSet[j] < h_spacing)) {
                    cannotCutHSpacingCount++;
                }
                charOffset = getReplaceCharOffset(data.charAt(j));
                if (charOffset != 0) {
                    replayceCharCount++;
                    offset += charOffset;
                }

                if (forwardIndex > currentIndex) {
                    m -= (getTextWidth(data.charAt(forwardIndex)) + h_spacing);
                }
            }
        }

        int state = 0;
        m *= -1;
        if (offset >= m) {    // 字符压缩排版可以排下
            Log.d("state = 1;");
            state = 1;
        } else if (forwardIndex != -1 && h_spacing > 1 && offset + (h_spacing - 1) * (forwardIndex - lineHead[line] - 1 - cannotCutHSpacingCount) > m) { // 字符压缩排版 + 字间距压缩 可以排下
            state = 2;
            Log.d("state = 2;");
        } else {    // 排不下,将多余部分排版到下一行
            if (backIndex == -1) {
                Log.d("process invalid head or end fail 2 ... ");
                return -1;
            }
            state = 3;
            Log.d("state = 3;");
        }
        float xOffset = 0;
        float charAddWidth = 0;
        if (state != 3) {
            currentIndex = forwardIndex;
            typeSet[lineHead[line]] += CHAPR_REPLACE_FLAG;
        } else {
            currentIndex = backIndex;
            charAddWidth = (screenWidth - typeSet[currentIndex + 1] - PADDING_RIGHT) / (currentIndex - lineHead[line]);
        }
        int firstChar = getFirstChar(data);
        for (int j = lineHead[line]; j <= currentIndex; j++) {
            if (state == 3) {
                typeSet[j] += (j - lineHead[line]) * charAddWidth;
            } else {
                if (state == 2) {
                    if (j + 1 <= currentIndex && j + 1 < firstChar) {
                        continue;
                    }
                }
                if (typeSet[j] > PADDING_LEFT + 1) {    // 首行缩进填充不需要改变
                    typeSet[j] -= xOffset;
                }
                charOffset = getReplaceCharOffset(data.charAt(j));
                if (state == 2) {
                    xOffset += (m - offset) / (currentIndex - lineHead[line] - cannotCutHSpacingCount);
                }
                if (charOffset != 0) {
                    charOffset -= (offset - m) / replayceCharCount;
                }
                xOffset += charOffset;
            }
        }

        return currentIndex;
    }

    public static char getReplaceChar(char charIn) {
        char charReplace = charIn;
        switch (charIn) {
            case '，':
                charReplace = ',';
                break;
            case '？':
                charReplace = '?';
                break;
            case '！':
                charReplace = '!';
                break;
            case '：':
                charReplace = ':';
                break;
            case '、':
                charReplace = '、';
                break;
            default:
                break;
        }
        return charReplace;
    }

    private final int getReplaceCharOffset(char charIn) {
        char charReplace = 0;
        switch (charIn) {
            case '，':
                charReplace = ',';
                break;
            case '？':
                charReplace = '?';
                break;
            case '！':
                charReplace = '!';
                break;
            case '：':
                charReplace = ':';
                break;
            case '。':
                charReplace = '。';
                break;
            case '、':
                charReplace = '、';
                break;
            default:
                break;
        }
        // 句号压缩 1/3
        if (charReplace == '。' || charReplace == '、') {
            return getTextWidth(charIn) / 3;
        } else if (charReplace != 0) {
            return getTextWidth(charIn) - getTextWidth(charReplace);
        }
        return 0;
    }

    // 不能在行首出现的字符
    public static final boolean isInvalidHeadChar(char charIn) {
        switch (charIn) {
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

    // 不能在行尾部出现的字符
    public  static final boolean isInvalidEndChar(char charIn) {
        switch (charIn) {
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

    /**
     * 首行缩进偏移字符数
     *
     * @return
     */
    private int getIndentCount() {
        switch (SettingContent.getInstance().getSettingIndentMode()) {
            case SettingContent.INDENT_DISABLE:
                return 0;
            case SettingContent.INDENT_TWO_CHAR:
                return 2;
            case SettingContent.INDENT_ONE_CHAR:
                return 1;
            default:
                break;
        }
        return 0;
    }

    /**
     * 获取第一个非空字符的位置
     *
     * @param contentBuffer
     * @return
     */
    private int getFirstChar(StringBuffer contentBuffer) {
        int indentMode = SettingContent.getInstance().getSettingIndentMode();
        if (indentMode == SettingContent.INDENT_DISABLE) {
            return 0;
        }

        int index = 0;
        for (; index < contentBuffer.length(); index++) {
            if (isEmptyChar(contentBuffer.charAt(index))) {
                continue;
            }

            break;
        }

        return index;
    }

    // 空格判断参考 http://zh.wikipedia.org/zh/%E7%A9%BA%E6%A0%BC
    protected final boolean isEmptyChar(char ch) {
        if (ch == 0x00a0 || ch == 0x0020 || ch == '\t' || (ch >= 0x2002 && ch <= 0x200D)
                || ch == '　' || ch == 0x202f) {
            return true;
        }
        return false;
    }

    private void emptyLineProcess(StringBuffer data, float[] typeSet, int[] linehead, int paraEmptyLineState) {
        boolean hasSpace = paraEmptyLineState == LAST_PARAGRAH_LAST_LINE_EMPTY;
        int endIndex = 0;
        boolean hasChar = false;
        char charData = 0;
        int lineIndex = -1;    // 段落尾部第一个空行
        for (int j = 0; j < linehead.length; j++) {
            if (linehead[j] != -1) {
                if (j + 1 < linehead.length && linehead[j + 1] != -1) {
                    endIndex = linehead[j + 1];
                } else {
                    endIndex = data.length();    //	 最后一行
                }
                hasChar = false;
                if (data.charAt(linehead[j]) == Token.TAG_CODE_START_IMG) {    //图片段落
                    hasChar = true;
                } else {
                    for (int index = linehead[j]; index < endIndex; index++) {
                        charData = data.charAt(index);
                        if (charData != ' ' && charData != '\r' && charData != '\n'
                                && charData != '　' && charData != '\t' && charData != 0) {
                            hasChar = true;
                            break;
                        }
                    }
                }

                if (hasChar) {
                    hasSpace = false;
                    lineIndex = -1;
                } else {
                    if ((hasSpace && SettingContent.getInstance().getSettingSpaceType() == 1) ||
                            SettingContent.getInstance().getSettingSpaceType() == 2) {
                        for (int k = linehead[j]; k < endIndex; k++) {
                            typeSet[k] = TYPESET_FLAG_TAG_COMPRESS;
                        }
                        linehead[j] = -1;
                    } else {
                        lineIndex = j;
                        hasSpace = true;
                    }
                }
            }
        }

        if (SettingContent.getInstance().getSettingSpaceType() == 1
                && lineIndex != -1 && paraEmptyLineState == NEXT_PARAGRAH_FIRST_LINE_EMPTY) {
            if (lineIndex + 1 < linehead.length && linehead[lineIndex + 1] != -1) {
                endIndex = linehead[lineIndex + 1];
            } else {
                endIndex = data.length();    //	 最后一行
            }
            for (int k = linehead[lineIndex]; k < endIndex; k++) {
                typeSet[k] = TYPESET_FLAG_TAG_COMPRESS;
            }
            linehead[lineIndex] = -1;
        }
    }

    public   void clearTypeset() {

        if (charWidths != null) {

            for (int i = 0; i < charWidths.length; i++) {
                charWidths[i] = 0;
            }
            charWidths[0] = 1;
            charWidths[0xfeff] = 1;
            chineseCharWidth = 0;
        }
    }

    private final boolean isWord(char c) {
        return ((c >= 0x30 && c <= 0x7a));
    }

    /**
     * @param c
     * @return
     */
    private final int getTextWidth(char c) {
        if (c > '\u4e00' && c < '\u9fbf') {
            if (chineseCharWidth < 1) {
                chineseCharWidth = (int) paint.measureText("和");
            }
            return chineseCharWidth;
        }
        if (charWidths[c] == 0 || measureDirect) {
            charWidths[c] = (byte) paint.measureText(c + "");  
        }
        return charWidths[c] & 0xff;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getWidth() {
        return screenWidth;
    }

    public int getH_spacing() {
        return h_spacing;
    }

}
