package com.xxx.reader.prepare;



import android.os.Handler;
import android.os.Message;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DestroyableThread;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.text.page.PageData;
import com.xxx.reader.text.page.PageTyping;
import com.xxx.reader.text.page.TextPageInfo2;
import com.xxx.reader.text.page.TypeParam;
import com.xxx.reader.text.page.Typing;

import java.io.File;

/**
 * 数据准备线程，
 * Created by davidleen29 on 2017/8/25.
 */

public class PrepareThread extends DestroyableThread {

    private IBook iBook;
    private TypeParam typeParam;
    public static final long DELAY=16;
    public static final int MSG_NEXT=100;
    public static final int MSG_PREVIOUS=98;
    public static final int MSG_PREPARE_CHAPTER=101;
    private Handler handler;
    private int maxCacheSize;
    //每个页面的准备顺序   当前  下一页 ， 上一页 ，下下页，  上上页
    int[] indexOfPage;// = new int[]{0, 1, -1, 2, -2}
    private PrepareListener prepareListener;
    private PreparePageInfo preparePageInfo;
    private PageTyping pageTyping;

    public void updateBook(IBook iBook)
    {
        this.iBook=iBook;
    }  public void updatePageTyping(PageTyping pageTyping)
    {
        this.pageTyping=pageTyping;
    }

    public void updateTypeParam(TypeParam typeParam)
    {
        this.typeParam=typeParam;
    }

    public PrepareThread(final PreparePageInfo preparePageInfo,   int maxCacheSize,Handler handler ) {
        this.preparePageInfo = preparePageInfo;


        this.maxCacheSize = maxCacheSize;

        indexOfPage = new int[maxCacheSize];
        this.handler = handler;

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

        if(iBook==null) return;
        if(typeParam==null) return;
        if(pageTyping==null) return;
//        if (skip.get()) return;
        final  PreparePageInfo temp=preparePageInfo;
        final long fileOffset=temp.currentPageOffset;
        final int  currentChapterIndex=temp.currentChapterIndex;
        final int midIndex=temp.midIndex;
          int size=temp.size;
        PageInfo currentPage=size==0?null:temp.pageInfos.get(midIndex);
        PageInfo prePage=null;
        PageInfo prePrePage=null;
        PageInfo nextPage=null;
        PageInfo nextNextPage=null;

        if(size==0)
        {

            IChapter iChapter=iBook.getChapter(currentChapterIndex);
            if(iChapter==null||!new File(iChapter.getFilePath()).exists())
            {
                handler.removeMessages(MSG_PREPARE_CHAPTER);
                Message message = handler.obtainMessage();
                message.what = MSG_PREPARE_CHAPTER;
                message.arg1 = currentChapterIndex;
                handler.sendMessageDelayed(message, 300);
                return ;
            }
            currentPage = pageTyping.typePage(iChapter,fileOffset,typeParam)  ;
            if (trySkip()) {
                return;
            }

            if(currentPage!=null) {
                currentPage.setChapterInfo(iChapter);
                Message message = handler.obtainMessage();
                message.what = MSG_NEXT;
                message.obj = currentPage;
                handler.sendMessageDelayed(message, DELAY);
            }

        }else
        {
            currentPage= temp.getCurrentPageInfo();
            prePage=temp.getPrePage();
            nextPage=temp.getNextPage();
            prePrePage=temp.getPrePrePage();
            nextNextPage=temp.getNextNextPage();
        }
        if (trySkip()) return;




        if(currentPage==null) return;

        if(nextPage==null)
        {



            PageInfo   nextPageInfo = drawNextPage(currentPage);
            if (trySkip()) {
                return;
            }


            if(nextPageInfo!=null) {
                Message message = handler.obtainMessage();
                message.what = MSG_NEXT;
                message.obj = nextPageInfo;
                handler.sendMessageDelayed(message, DELAY);
            }


            nextPage=nextPageInfo;
        }

        if(prePage==null)
        {
            PageInfo   previousPage =  drawPrePage(currentPage);;
            if (trySkip()) {
                return;
            }

            if(previousPage!=null) {
                Message message = handler.obtainMessage();
                message.what = MSG_PREVIOUS;
                message.obj = previousPage;
                handler.sendMessageDelayed(message, DELAY);
            }


            prePage=previousPage;
        }




        if(nextPage!=null&&nextNextPage==null)
        {



            PageInfo   nextPageInfo = drawNextPage(nextPage) ;
            if (trySkip()) {
                return;
            }
            if(nextPageInfo!=null) {

                Message message = handler.obtainMessage();
                message.what = MSG_NEXT;
                message.obj = nextPageInfo;
                handler.sendMessageDelayed(message, DELAY);

                nextNextPage = nextPageInfo;
            }


        }


        if(prePage!=null&&prePrePage==null)
        {
            PageInfo   previousPage = drawPrePage(prePage) ;
            if (trySkip()) {
                return;
            }

            if(previousPage!=null) {
                Message message = handler.obtainMessage();
                message.what = MSG_PREVIOUS;
                message.obj = previousPage;
                handler.sendMessageDelayed(message, DELAY);
            }


            prePrePage=previousPage;
        }











    }

    private boolean trySkip() {

        if (skip.get()) {

            clearHandler();
            return true;
        }
        return false;
    }

    private void clearHandler() {

        handler.removeMessages(MSG_PREVIOUS);
        handler.removeMessages(MSG_NEXT);
    }


    @Override
    protected void onDestroy() {
        clearHandler();


    }





    private IChapter findBookChapterInfo(IChapter iChapter ,int offset) {
        if (iChapter == null) return null;
        int destIndex=iChapter.getIndex()+offset;
        if (destIndex < 0||destIndex>=iBook.getChapterCount()) return null;
        IChapter destChapter=iBook.getChapter(destIndex);
        if (destChapter == null||!new File(destChapter.getFilePath()).exists()) {

            handler.removeMessages(MSG_PREPARE_CHAPTER);
            Message message = handler.obtainMessage();
            message.what = MSG_PREPARE_CHAPTER;
            message.arg1 = destIndex;
            handler.sendMessageDelayed(message, 300);
              return null;
        }
        return destChapter;
    }

    private PageInfo drawNextPage(PageInfo lastPage)
    {
        if (lastPage == null) return null;
        IChapter iChapter=lastPage.getChapterInfo();
        if(lastPage.isLastPage())
        {
            iChapter=findNextChapter(iChapter);
            lastPage=null;
        }
        if(iChapter==null) return null;

        PageInfo pageInfo = pageTyping.typePageNext(iChapter, typeParam, lastPage);
        if(pageInfo!=null) pageInfo.setChapterInfo(iChapter);
        return pageInfo;

    };

    private PageInfo drawPrePage(PageInfo lastPage)
    {
        if (lastPage == null) return null;
        IChapter iChapter=lastPage.getChapterInfo();
        if(lastPage.isFirstPage())
        {
            iChapter=findPrePage(iChapter);
            lastPage=null;
        }
        if(iChapter==null) return null;

        PageInfo pageInfo = pageTyping.typePagePre(iChapter, typeParam, lastPage);
        if(pageInfo!=null)
        {
            pageInfo.setChapterInfo(iChapter);
        }
        return pageInfo;

    };





    private IChapter findNextChapter(IChapter iChapter)
    {

        return findBookChapterInfo(iChapter,1);

    }

    private IChapter findPrePage(IChapter iChapter)
    {

        return findBookChapterInfo(iChapter,-1);

    }

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



    public PrepareListener getPrepareListener() {
        return prepareListener;
    }

    public void setPrepareListener(PrepareListener prepareListener) {
        this.prepareListener = prepareListener;
    }

    @Override
    public void setSkip(boolean skip) {
        super.setSkip(skip);
        clearHandler();
    }
}
