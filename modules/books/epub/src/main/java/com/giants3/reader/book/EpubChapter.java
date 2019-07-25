package com.giants3.reader.book;


import com.xxx.reader.book.IChapter;

import java.io.File;

/**
 * Epub章节数据
 *
 * @author davidleen29
 */
public class EpubChapter implements IChapter {
    /**
     * chapterTitle 章节名
     */
    private String text;
    /**
     * chapterTitle 章节id
     */
//	private String id;
    /**
     * chapterTitle 章节地址 相对路径
     */
    private String src;

    /**
     * chapterTitle 章节地址 绝对路径
     */
    private String srcFilePath;


    /**
     * 在epub中的顺序
     */
    private int playOrder;

    private int pri = 0;

    private boolean mHasChild = false;

    private boolean expanded = false;
    private String path;

    public void setHasChild(boolean hasChild) {
        this.mHasChild = hasChild;
    }

    public boolean hasChild() {
        return mHasChild;
    }

    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getSrc() {
        return src;
    }

    // 部分epub书籍章节节点 为  xxx.htm#xxxx
    public void setSrc(String src) {
        int index1 = src.lastIndexOf('.');
        if (index1 != -1) {
            int index2 = src.indexOf("#", index1);
            if (index2 != -1) {
                this.src = src.substring(0, index2);
                return;
            }
        }
        this.src = src;
    }

    public int getPri() {
        return pri;
    }

    public void setPri(int pri) {
        this.pri = pri;
    }

    public int getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }


    EpubChapter() {
    }

    /**
     * 判断章节对应的文件是否存在
     *
     * @return
     */
    public boolean hasChapterFile() {

        return new File(srcFilePath).exists();

    }

    /**
     * 设置章节文件的绝对路径
     *
     * @param relPath
     */
    public void setSrcFilePath(String relPath) {

        srcFilePath = relPath;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getName() {
        return text;
    }

    @Override
    public String getUrl() {
        return src;
    }

    @Override
    public String getFilePath() {
        return srcFilePath;
    }

    @Override
    public int getIndex() {
        return playOrder;
    }

    @Override
    public boolean hasPay() {
        return false;
    }

    public void setBookPath(String path) {
        this.path = path;
    }

    public String getBookPath()
    {
        return path;
    }
}
