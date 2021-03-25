package com.xxx.reader.core;

/**
 * 阅读绘制 分页信息基类
 * <p>
 * Created by davidleen29 on 2017/8/26.
 */

public class PageInfo {

    public int pageIndex;
    public int pageCount;
    public int chapterIndex;

    public boolean isFirstPage;
    public boolean isLastPage;

    public long startPos;
    public long endPos;
    public long fileSize;


    public volatile  boolean isReady=false;
    @Override
    public String toString() {
        return "chapterIndex:"+chapterIndex+",pageIndex:"+pageIndex;

    }

    public void recycle()
    {}


}
