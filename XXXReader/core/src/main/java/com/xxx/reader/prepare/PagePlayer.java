package com.xxx.reader.prepare;

import android.os.Message;
import android.view.MotionEvent;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.CacheUpdateListener;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageBitmap;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 绘制准备层
 * <p>
 * 分页调度
 * <p>
 * 准备缓存
 * <p>
 * Created by davidleen29 on 2017/8/25.
 */

public class PagePlayer<C extends IChapter, P extends PageInfo, DP extends DrawParam, PB extends PageBitmap> extends BasePlayer<C, DP> implements BitmapProvider,  PrepareListener
{

    public static final boolean DEBUG = true  ;

    /**
     * 最大的缓存数量
     */
    public static int MAX_CACHE_SIZE = 5;
    /**
     * 当前页index
     */
    public static int MID_CACHE_INDEX = MAX_CACHE_SIZE / 2;
    /**
     * 缓存图片内容。
     */
    PageBitmap cacheBitmaps[] = new PageBitmap[MAX_CACHE_SIZE];

    /**
     * 缓存图片内容。
     */
    PageInfo cachePageInfoArray[] = new PageInfo[MAX_CACHE_SIZE];






    /**
     * 真实绘制回调接口
     */
    private IDrawable iDrawable;
    /**
     * 相关绘制参数
     */
    private DP drawParam;


    /**
     * @param iDrawable  界面重绘回调接口
     * @param creator
     */
    public PagePlayer(  final IDrawable iDrawable, PageBitmapCreator<PB> creator) {

        this.iDrawable = iDrawable;

        PageBitmap.PageUpdateListener listener=new PageBitmap.PageUpdateListener() {
            @Override
            public void onPageUpdate(PageBitmap pageBitmap) {


                int index=-1;
                for (int i = 0; i < MAX_CACHE_SIZE; i++) {
                    if (cacheBitmaps[i]==pageBitmap) {
                        index=i;
                    }
                }

                if(Math.abs(MID_CACHE_INDEX-index)<=1)
                 iDrawable.updateView();
                //updateCache();
            }
        };
        for (int i = 0; i < MAX_CACHE_SIZE; i++) {
            cacheBitmaps[i] = creator.create();
            cacheBitmaps[i].setPageUpdateListener(listener);
        }






    }

    @Override
    public void jumpInChapter(float percent) {


        if (currentPageCount <= 0) {

            Message message = Message.obtain();
            message.what = MSG_JUMP_PERCENT;
            message.obj = percent;
            handler.removeMessages(MSG_JUMP_PERCENT);
            handler.sendMessageDelayed(message, 50);
            return;
        }
        currentPageIndex = Math.round(currentPageCount * percent);
        currentPageIndex = Math.max(0, Math.min(currentPageIndex, currentPageCount - 1));


        updateCache();


    }

    /**
     * 直接跳转到指定章节
     *
     * @param chapterIndex
     * @param pageIndex
     */
    @Override
    public void jumpTo(int chapterIndex, int pageIndex) {


        currentPageIndex = pageIndex;
        currentChapterIndex = chapterIndex;

        updateCache();


    }


    @Override
    public BitmapHolder getNextBitmap() {
        return cacheBitmaps[MID_CACHE_INDEX + 1];
    }

    @Override
    public BitmapHolder getPreviousBitmap() {
        return cacheBitmaps[MID_CACHE_INDEX - 1];
    }

    @Override
    public BitmapHolder getBitmap(int index) {
        return cacheBitmaps[index];
    }

    @Override
    public PageBitmap getCurrentBitmap() {
        return cacheBitmaps[MID_CACHE_INDEX];
    }


    @Override
    public void updateDrawParam(DP drawParam) {
        if (this.drawParam != null && this.drawParam.equals(drawParam))
            return;
        this.drawParam = drawParam;

        for(PageBitmap bitmap:cacheBitmaps)
        {
            bitmap.updateDrawParam(drawParam);
        }
        //全部重新绘制。
        setAllCacheDirty();




        updateCache();

    }


    private void setAllCacheDirty() {
        for (int i = 0; i < MAX_CACHE_SIZE; i++) {
            cacheBitmaps[i].setDirty();
        }
    }


    /**
     * 切换书籍
     *
     * @param iBook
     */
    @Override
    public void updateBook(IBook<C> iBook) {
        super.updateBook(iBook);
        setAllCacheDirty();

        updateCache();


    }

    public void clear() {

        setAllCacheDirty();

    }

    @Override
    public void updateCache() {

    }


    @Override
    public void turnNext() {


            long time = Calendar.getInstance().getTimeInMillis();

            PageBitmap tempPageBitmap = cacheBitmaps[0];
            for (int i = 0; i < MAX_CACHE_SIZE - 1; i++) {
                cacheBitmaps[i] = cacheBitmaps[i + 1];
            }
            cacheBitmaps[MAX_CACHE_SIZE - 1] = tempPageBitmap;


            tempPageBitmap.setDirty();
            if (DEBUG)
                Log.e("time use in switch:" + (Calendar.getInstance().getTimeInMillis() - time));




    }
//
//
    @Override
    public void turnPrevious() {


            PageBitmap temp = cacheBitmaps[MAX_CACHE_SIZE - 1];
            for (int i = MAX_CACHE_SIZE - 1; i > 0; i--) {
                cacheBitmaps[i] = cacheBitmaps[i - 1];
            }
            cacheBitmaps[0] = temp;
            temp.setDirty();



    }





    @Override
    public void onDestroy() {

        for (PageBitmap bitmap : cacheBitmaps) {
            bitmap.onDestroy();
        }


    }

    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }


    @Override
    public void onPagePrepared(PreparePageInfo preparePageInfo) {

        List<PageInfo> pageInfos=preparePageInfo.pageInfos;
        int midIndex=preparePageInfo.midIndex;

        int size = preparePageInfo.size;
        for (int i =midIndex- MAX_CACHE_SIZE/2; i <midIndex+MAX_CACHE_SIZE/2 ; i++) {
            PageInfo pageInfo=i<0||i>=size?null:pageInfos.get(i);

            int cacheIndex=i-midIndex + MAX_CACHE_SIZE / 2;
            if(cacheIndex>=0&&cacheIndex<cachePageInfoArray.length) {

                cachePageInfoArray[cacheIndex] = pageInfo;
            }


        }
        bindPageInfos();







    }

    private  void bindPageInfos()
    {
        for (int i = 0; i < MAX_CACHE_SIZE; i++) {

            cacheBitmaps[i].attachPageInfo(cachePageInfoArray[i]);
        }


    }
}
