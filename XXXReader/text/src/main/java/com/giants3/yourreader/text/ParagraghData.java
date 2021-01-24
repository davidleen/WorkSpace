package com.giants3.yourreader.text;

/**
 * 段落数据
 */
public class ParagraghData {
    /*段落内容*/
    private String content;

    /*段落在文件中字节位置**/
    public long paragragStart = 0;
    /*段落在文件中字节位置**/
    public long paragraghEnd = 0;

    /*编码*/
    private int code;


    /*内容长度*/
    private int contentCount = -1;


    public final int getContentCount() {
        if (contentCount == -1) {
            if (content == null || content.length() == 0) {
                contentCount = 0;
            } else {
                contentCount = content.length();
            }
        }

        return contentCount;
    }

    public void setContentCount(int count) {
        contentCount = count;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
