package com.xxx.reader.prepare;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.core.PageBitmap;
import com.xxx.reader.core.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PagePrepareWorker {


    private static final int MID_CACHE_INDEX = 2;
    List<PageInfo> pageInfos=new ArrayList<>();
    int currentChapterIndex;
    int currentPageIndex;
    private ChapterMeasureManager chapterMeasureManager;
    private int currentPageCount;

    public PagePrepareWorker(ChapterMeasureManager chapterMeasureManager)
    {
        this.chapterMeasureManager = chapterMeasureManager;
    }

    /**
     * 缓存页面的额绘制，
     *
     * @param offset 偏移值 0 为当前页， 中间页
     * @param skip   绘制过程中是否略过
     */
    public void preparePage(int offset, AtomicBoolean skip) {


//        if (skip.get()) return;


        int index = MID_CACHE_INDEX + offset;

        if (offset == 0) {
            //矫正pageindex位置
            ChapterMeasureResult result = chapterMeasureManager.getMeasuredResult(currentChapterIndex);
            if (result != null && result.pageCount > 0 && currentPageIndex >= result.pageCount) {
                currentPageIndex = result.pageCount - 1;
            }

        }

        PageInfo currentPageInfo = chapterMeasureManager.getPageInfo(currentChapterIndex, currentPageIndex, offset);
        if (offset == 0 && currentPageInfo != null) {
            currentPageCount = currentPageInfo.pageCount;


        }

//        if (DEBUG)
        //    Log.e("startDrawThread:=========================" + currentPageInfo + ",currentChapterIndex:" + currentChapterIndex + ",currentPageIndex:" + currentPageIndex + ",comicbook:" + iBook);






    }



}
