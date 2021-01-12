package com.xxx.reader.prepare;



import android.os.Handler;
import android.os.Message;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.core.DestroyableThread;
import com.xxx.reader.core.PageInfo;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据准备线程，
 * Created by davidleen29 on 2017/8/25.
 */

public class PrepareThread extends DestroyableThread {

    public static final int MSG_UPDATE_PAGES=99;
    public static final int MSG_NEXT=100;
    public static final int MSG_PREVIOUS=98;
    private Handler handler;
    private int maxCacheSize;
    //每个页面的准备顺序   当前  下一页 ， 上一页 ，下下页，  上上页
    int[] indexOfPage;// = new int[]{0, 1, -1, 2, -2}
    private PrepareListener prepareListener;
    private PreparePageInfo preparePageInfo;

    public PrepareThread(final PreparePageInfo preparePageInfo, ChapterMeasureManager chapterMeasureManager, int maxCacheSize ) {
        this.preparePageInfo = preparePageInfo;

        handler=new Handler(){


            @Override
            public void handleMessage(Message msg) {


                switch (msg.what) {
                    case MSG_UPDATE_PAGES: {
                        preparePageInfo.pageInfos.clear();
                        ChapterMeasureResult measuredResult = (ChapterMeasureResult) msg.obj;
                        if (measuredResult != null) {

                            preparePageInfo.addPages(measuredResult.pageValues);

                            if (getPrepareListener() != null) {
                                getPrepareListener().onPagePrepared(preparePageInfo);
                            }
                        }
                    }


                        break;

                    case MSG_NEXT: {
                        PageInfo measuredResult = (PageInfo) msg.obj;
                        if (measuredResult != null) {
                            preparePageInfo.addPage(measuredResult);

                            if (getPrepareListener() != null) {
                                getPrepareListener().onPagePrepared(preparePageInfo);
                            }
                        }

                    }



                        break;

                    case MSG_PREVIOUS: {
                        PageInfo measuredResult = (PageInfo) msg.obj;
                        if (measuredResult != null) {
                            preparePageInfo.addHead(measuredResult);

                            if (getPrepareListener() != null) {
                                getPrepareListener().onPagePrepared(preparePageInfo);
                            }
                        }

                    }



                    break;
                }


            }
        };
        this.chapterMeasureManager = chapterMeasureManager;
        this.maxCacheSize = maxCacheSize;

        indexOfPage = new int[maxCacheSize];
        this.setPrepareListener(getPrepareListener());
        for (int i = 0; i < maxCacheSize; i++) {

            indexOfPage[i] = ((int) Math.pow(-1, i + 1)) * ((i + 1) / 2);
        }
        String result = "";
        for (int i = 0; i < maxCacheSize; i++) {
            result += indexOfPage[i] + ",";
        }
        Log.e("indexOfPage:" + result);

    }


    private static final int SLEEP_DURATION = Integer.MAX_VALUE;


    @Override
    protected void runOnThread() {


//        if (skip.get()) return;
        final  PreparePageInfo temp=preparePageInfo;

        final int midIndex=temp.midIndex;
        final int size=temp.size;





        int drawNextCount=midIndex +PreparePageInfo.PREPARE_SIZE-size;

        
        PageInfo pageInfo=size==0?null:temp.pageInfos.get(size-1);
        PageInfo currentPage=size==0?null:temp.pageInfos.get(midIndex);
        Log.e("drawNextCount:"+drawNextCount+pageInfo);
        for (int i=0;i<drawNextCount;i++)
        {


            if (trySkip()) return;
            PageInfo nextPageInfo;
            if(pageInfo==null)
            {
                nextPageInfo = chapterMeasureManager.getPageInfo(temp.currentChapterIndex,temp.currentPageIndex);
                currentPage=nextPageInfo;
            }else
            {
                  nextPageInfo = chapterMeasureManager.getNextPageInfo(pageInfo );
            }

            if (trySkip()) return;
            if(nextPageInfo!=null) {
                Message message = handler.obtainMessage();
                message.what = MSG_NEXT;
                message.obj = nextPageInfo;
                handler.sendMessage(message);
                pageInfo=nextPageInfo;
            }else
            {
                break;
            }

        }

        int drawPreviousCount= PreparePageInfo.PREPARE_SIZE-midIndex;
        Log.e("drawPreviousCount:"+drawPreviousCount+currentPage);
        if(currentPage!=null)
        {


        for (int i=0;i<drawPreviousCount;i++) {

            if (trySkip()) return;
            PageInfo   previousPageInfo = chapterMeasureManager.getPreviousPageInfo(currentPage);
            if (trySkip()) return;
            if (previousPageInfo != null) {

                Message message = handler.obtainMessage();
                message.what = MSG_PREVIOUS;
                message.obj = previousPageInfo;
                handler.sendMessage(message);
                currentPage = previousPageInfo;
            } else {
                break;
            }
        }

        }
//        if(size==0)
//        {
//
//
//            ChapterMeasureResult measuredResult = chapterMeasureManager.measureResult(0, new Cancelable() {
//                @Override
//                public boolean isCancelled() {
//                    return false;
//                }
//            });
//
//            Message message = handler.obtainMessage();
//                message.what=MSG_UPDATE_PAGES;
//                message.obj=measuredResult;
//                handler.sendMessage(message);
//
//        }else
//        if(size==0)
//        {
//
//
//            ChapterMeasureResult measuredResult = chapterMeasureManager.measureResult(0, new Cancelable() {
//                @Override
//                public boolean isCancelled() {
//                    return false;
//                }
//            });
//
//            Message message = handler.obtainMessage();
//                message.what=MSG_UPDATE_PAGES;
//                message.obj=measuredResult;
//                handler.sendMessage(message);
//
//        }else
//        {
//
//
//            if (midIndex>size-PreparePageInfo.PREPARE_SIZE) {
//                PageInfo pageInfo = chapterMeasureManager.getNextPageInfo(temp.pageInfos.get(size - 1) );
//                Message message = handler.obtainMessage();
//                message.what=MSG_NEXT;
//                message.obj=pageInfo;
//                handler.sendMessage(message);
//
//            }else
//                if(midIndex<=3)
//                {
//
//
//                        PageInfo measuredResult = chapterMeasureManager.getPreviousPageInfo(temp.pageInfos.get(0) );
//
//                        Message message = handler.obtainMessage();
//                        message.what = MSG_PREVIOUS;
//                        message.obj = measuredResult;
//                        handler.sendMessage(message);
//
//
//                }
//
//        }




    }

    private boolean trySkip() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.e(skip.get());
        if (skip.get())
            return true;
        return false;
    }




    @Override
    protected void onDestroy() {



    }


    private static final int MID_CACHE_INDEX = 2;
    private ChapterMeasureManager chapterMeasureManager;
    private int currentPageCount;





//    /**
//     * 缓存页面的额绘制，
//     *
//     * @param offset 偏移值 0 为当前页， 中间页
//     * @param skip   绘制过程中是否略过
//     */
//    public void preparePage(int offset, AtomicBoolean skip) {
//
//
//
//        if(temp.midIndex)
//
//
//        int index = MID_CACHE_INDEX + offset;
//        int currentChapterIndex=preparePageInfo.midIndex;
//
//        if (offset == 0) {
//            //矫正pageindex位置
//            ChapterMeasureResult result = chapterMeasureManager.getMeasuredResult(currentChapterIndex);
//            if (result != null && result.pageCount > 0 && currentIndex >= result.pageCount) {
//
//                Message message = handler.obtainMessage();
//                message.what=MSG_UPDATE_PAGES;
//                message.obj=result;
//                handler.sendMessage(message);
//
//            }
//
//        }
//
//        PageInfo currentPageInfo = chapterMeasureManager.getPageInfo(currentChapterIndex, currentIndex, offset);
//        if (offset == 0 && currentPageInfo != null) {
//            currentPageCount = currentPageInfo.pageCount;
//
//
//        }
//
////        if (DEBUG)
//        //    Log.e("startDrawThread:=========================" + currentPageInfo + ",currentChapterIndex:" + currentChapterIndex + ",currentPageIndex:" + currentPageIndex + ",comicbook:" + iBook);
//
//
//
//
//    }


    private static final int MaxCacheSize=30;
    private void trimList()
    {
        if(preparePageInfo.pageInfos.size()>maxCacheSize)
        {
            int trimSize=preparePageInfo.pageInfos.size()-maxCacheSize;


        }
    }

    public PrepareListener getPrepareListener() {
        return prepareListener;
    }

    public void setPrepareListener(PrepareListener prepareListener) {
        this.prepareListener = prepareListener;
    }
}
