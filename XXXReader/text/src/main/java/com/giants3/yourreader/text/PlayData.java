package com.giants3.yourreader.text;

/**
 * 朗读播放数据类
 * 注：注释中的自然段落或完整段落指的是，在文本中的实际段落
 * 显示段落
 */
public class PlayData {
    public static final int PLAY_MAX_CHAR = 256;

    // 播放的文本流
    public StringBuilder content;

    long fileStartPos;
    long fileEndPos;


    int playCount;

    int lastIndex;
    int lastCount;

    public PlayData() {
        playCount = -1;

        lastIndex = 0;
        lastCount = 0;
        content = new StringBuilder(PLAY_MAX_CHAR * 2);

    }

    /**
     * 方便对象复用
     */
    public void reset() {
        content.delete(0, content.length());

        playCount = -1;
        lastIndex = 0;
        lastCount = 0;
    }

    public boolean hasData() {
        return content.length() > 0;
    }


    public final String getContent() {
        return content.toString();
    }


    /**
     * 修正播放位置（跳过特殊字符）
     *
     * @param charIndex 播放的字符在content中的位置
     * @return 播放的字符在content中的位置
     */
    private int caculateIndexForward(int charIndex) {
        // 过滤换行符
        int i = charIndex;
        for (; i < content.length() - 1; i++) {
            if (content.charAt(i) != '　' && content.charAt(i) != ' ' && content.charAt(i) != '\r' && content.charAt(i) != '\n') {
                return i;
            }
        }

        return i;
    }


}
