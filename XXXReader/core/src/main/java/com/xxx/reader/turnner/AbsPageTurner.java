package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapProvider;


/**
 * 翻页动画处理的 基类
 * Created by davidleen29 on 2017/8/29.
 */

public abstract class AbsPageTurner implements IPageTurner, GestureDetector.OnGestureListener , GestureDetector.OnDoubleTapListener {
    private final GestureDetector gestureDetector;

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void setOnScrollListener(ScrollListener listener) {

    }

    protected DrawParam drawParam;
    protected final PageSwitchListener pageSwitchListener;
    protected IDrawable drawable;
    protected final BitmapProvider bitmapProvider;
    protected  Scroller scroller;


    public AbsPageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {


        scroller = new Scroller(context);

        gestureDetector = new GestureDetector(context, this);


        this.pageSwitchListener = pageSwitchListener;
        this.drawable = drawable;


        this.bitmapProvider = bitmapProvider;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    protected int getDrawHeight() {
        return (int) (drawParam.height);
    }

    @Override
    public boolean isInAnimation() {
        return true;
    }


    protected boolean canTurnNext() {
        if(pageSwitchListener==null) return true;
        return pageSwitchListener.canPageChanged(PageSwitchListener.TURN_NEXT);
    }


    protected boolean canTurnPrevious() {
        if(pageSwitchListener==null) return true;
        return pageSwitchListener.canPageChanged(PageSwitchListener.TURN_PREVIOUS);
    }


    @Override
    public void updateDrawParam(DrawParam drawParam) {
        this.drawParam = drawParam;
    }




    @Override
    public void onDraw(Canvas canvas) {


    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}
