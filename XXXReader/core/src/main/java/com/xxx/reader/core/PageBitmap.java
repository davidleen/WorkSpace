package com.xxx.reader.core;


import android.graphics.Canvas;
import android.view.MotionEvent;

import com.xxx.reader.text.layout.BitmapHolder;

/**
 * 分页内容绘制
 * <p>
 * <p>
 * <p>
 * Created by davidleen29 on 2017/8/30.
 */

public abstract class PageBitmap<P extends PageInfo, D extends DrawParam> extends AbstractBitmapHolder implements BitmapHolder, IDrawable {


    /**
     * 缓存图片的状态   0  初始 1 已经绘制  2 DIRTY
     *
     */
    private final  int STATE_DIRTY=1;
    private final  int STATE_NONE=0;
    private final IDrawable iDrawable;
    public volatile int state;



    private PageUpdateListener pageUpdateListener;

    public P getPageInfo() {
        return pageInfo;
    }

    protected P pageInfo;
    protected D drawParam;


    public PageBitmap(int screenWidth, int screenHeight,IDrawable iDrawable) {
        super(screenWidth, screenHeight);


        this.iDrawable = iDrawable;
    }


    public void attachPageInfo(P pageInfo) {
        if (this.pageInfo == pageInfo  ) return;
        this.pageInfo = pageInfo;
        drawPage(pageInfo,drawParam);
        iDrawable.updateView();



    }

    public void updateDrawParam(D drawParam) {
        if (this.drawParam == drawParam) return;
        this.drawParam = drawParam;
        drawPage(pageInfo,drawParam);
        iDrawable.updateView();


    }



    public void setDirty() {


    }
   protected void  drawPage(P pageInfo,D drawParam)
   {

   }

    public void setState(int drawState) {
        this.state = drawState;
    }





    public abstract boolean onTouchEvent(MotionEvent event);



    public void setPageUpdateListener(PageUpdateListener pageUpdateListener) {
        this.pageUpdateListener = pageUpdateListener;
    }





    public void onDestroy() {



    }


    @Override
    public void updateView() {
        state=STATE_DIRTY;

        pageUpdateListener.onPageUpdate(this);


    }


    public void invalidate()
    {



    }


     public  interface  PageUpdateListener
    {
        public void onPageUpdate(PageBitmap pageBitmap);
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
