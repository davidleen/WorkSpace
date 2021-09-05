package com.xxx.reader.core;

import com.xxx.reader.book.IChapter;

/**
 * 阅读绘制 分页信息基类
 * <p>
 * Created by davidleen29 on 2017/8/26.
 */

public class PageInfo {


    IChapter iChapter;
    public IChapter getChapterInfo()
    {
        return iChapter;
    }
    public void setChapterInfo(IChapter iChapter)

    {
        this.iChapter=iChapter;
    }

    public int pageIndex; public int pageCount;
    private boolean isFirstPage;
    private boolean isLastPage;

    private long startPos;
    private long endPos;
    private long fileSize;


    public volatile  boolean isReady=false;
    @Override
    public String toString() {
        return  iChapter.toString();

    }

    public void recycle()
    {}


    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    public float getPageDrawHeight()
    {
        return 0;
    }

}
