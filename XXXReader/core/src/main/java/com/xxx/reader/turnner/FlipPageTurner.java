package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapProvider;


/**
 * 滚动式
 * Created by davidleen29 on 2017/8/29.
 */

public abstract class FlipPageTurner extends AbsPageTurner implements GestureDetector.OnGestureListener {


    /**
     * 是否触发了fling的标志
     */
    private boolean mIsFling = false;


    /**
     * 是否要进行翻页调整
     */
    private boolean mIsAdjust = false;

    private boolean isTouching = false;
    /**
     * 滑动的距离 根据这个距离进行绘制
     */
    protected float offsetX;

    /**
     * 翻页方向
     * {@link com.xxx.reader.core.IPageTurner#TURN_NEXT }
     * {@link com.xxx.reader.core.IPageTurner#TURN_NONE }
     * {@link com.xxx.reader.core.IPageTurner#TURN_PREVIOUS }
     */
    private int direction = 0;


    protected Rect drawRect = new Rect();

    public FlipPageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);

    }


    @Override
    public boolean onDown(MotionEvent e) {

        direction = IPageTurner.TURN_NONE;
        if (e.getX() > drawParam.width * 2 / 3) direction = IPageTurner.TURN_NEXT;
        if (e.getX() < drawParam.height / 3) direction = IPageTurner.TURN_PREVIOUS;


        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e("onScroll==============");
        if (mIsFling) return false;


        int direction = (int) (e2.getX() - e1.getX());

        scroller.abortAnimation();

        int currX = scroller.getCurrX();
        if (!canScrollPrevious(direction, currX)) {
            scroller.startScroll(currX, 0, 0, 0);
        } else if (!canScrollNext(direction, currX)) {
            scroller.startScroll(currX, 0, 0, 0);
        } else {
            scroller.startScroll(currX, 0, (int) (-distanceX), 0);
        }

        drawable.updateView();
//        return true;
        return true;
    }

    /**
     * 能否滚到下一张
     *
     * @param direction
     * @param currX
     * @return true 为能  false 不能
     */
    private boolean canScrollNext(int direction, int currX) {
        return !(!canTurnNext() && direction < 0 && (currX < -drawRect.width() / 3));
    }

    /**
     * 能否滚到上一张的判断
     *
     * @param direction
     * @param currX
     * @return true 为能  false 不能
     */
    private boolean canScrollPrevious(int direction, int currX) {
        return !(direction > 0 && !canTurnPrevious() && !(currX < drawRect.width() / 3));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        Log.e("=================velocityX="+(velocityX)+" ,offsetX ="+offsetX);
        if (mIsFling) return false;
        if (isCancelMove(e1, e2)) {
            return true;
        }
        if (cancelFlingAndSroll(e1, e2, velocityX)) {
            drawable.updateView();
            return true;
        }

        return true;

    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        boolean handled = false;
        if (pageSwitchListener != null) {
            int x = (int) e.getX();
            int y = (int) e.getY();


            int width = drawParam.width;
            int height = drawParam.height;

            int startX = 0;
            if (x < width / 3) {
                handled = true;
                direction = IPageTurner.TURN_PREVIOUS;
                scroller.startScroll(startX, 0, drawRect.width() + startX, 0, 888);

            } else if (x > width * 2 / 3) {
                handled = true;

                direction = IPageTurner.TURN_NEXT;
                scroller.startScroll(startX, 0, -drawRect.width() - startX, 0, 888);
            }
            offsetX = startX;
            if (handled) drawable.updateView();
        }


        return handled;
    }

//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        Log.e("onSingleTapUp");
//        boolean handled=false;
//        direction= IPageTurner.TURN_NONE;
//        int startX=0;
//
//        if(e.getX()>drawParam.width*2/3  && canTurnNext())
//        {
//
//            direction= IPageTurner.TURN_NEXT;
//            scroller.startScroll(startX, 0, -drawRect.width() - startX, 0,3000);
//
//            handled=true;
//        }else
//            if(e.getX()<drawParam.width/3  && canTurnPrevious())
//            {
//                direction= IPageTurner.TURN_PREVIOUS;
//                scroller.startScroll(startX, 0, drawRect.width() + startX, 0,3000);
//                handled=true;
//            }
//
//         offsetX=startX;
//
//
//        if(handled)   drawable.updateView();
//
//
//       return handled;
//    }


    protected void computeScroll() {
        if (drawParam == null) return;

        if (!scroller.computeScrollOffset()) {

            if (mIsFling) { //快速滑动处理
                handleFling();
                resetScroller();
                direction = IPageTurner.TURN_NONE;
                return;
            }




            Log.e("=============computeScroll .offsetX="+isTouching);
            if (isNeedScrollBack()&&!isTouching) {
                doScrollBack(); //在同一张图的回弹  如 滑动距离不够
                return;
            }


            if(!isTouching&&offsetX>-drawParam.width&&direction==IPageTurner.TURN_NEXT)
            {

                //scroll the rest;
                scroller.startScroll((int)offsetX,0,(int)(-drawParam.width-offsetX),0);
                drawable.updateView();

            return ;
            }
            if(!isTouching&&offsetX< drawParam.width&&direction==IPageTurner.TURN_PREVIOUS)
            {
                //scroll the rest;
                scroller.startScroll((int)offsetX,0,(int)(drawParam.width-offsetX),0);
                drawable.updateView();
                return ;
            }




            if (!isTouching) {
                if (offsetX<=-drawParam.width&&direction == IPageTurner.TURN_NEXT && canTurnNext() && pageSwitchListener != null) {
                    pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_NEXT);
                }
                if (offsetX>=drawParam.width&&direction == IPageTurner.TURN_PREVIOUS && canTurnPrevious() && pageSwitchListener != null) {
                    pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_PREVIOUS);
                }
                direction = IPageTurner.TURN_NONE;
                resetScroller();
            }


            return;
        }

        offsetX = scroller.getCurrX();
      Log.e("=============computeScroll .offsetX="+offsetX);

        drawable.updateView();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouching=false;
                break;
            case MotionEvent.ACTION_DOWN:
                isTouching=true;
                break;
        }


       return super.onTouchEvent(event);


    }

    /**
     * 是否需要回滚到当前页
     *
     * @return
     */
    private boolean isNeedScrollBack() {

        if(offsetX>0&&offsetX<drawParam.width/2&&direction==IPageTurner.TURN_PREVIOUS) return true;
        if(offsetX<0&&offsetX>-drawParam.width/2&&direction==IPageTurner.TURN_NEXT) return true;
        return false;
    }

    /**
     * 处理快速滑动
     */
    private void handleFling() {
        mIsFling = false;
        if (offsetX < 0 && canTurnNext() && pageSwitchListener != null) {
            pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_NEXT);
            direction=IPageTurner.TURN_NONE;

        }
        if (offsetX > 0 && canTurnPrevious() && pageSwitchListener != null) {
            pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_PREVIOUS);
            direction=IPageTurner.TURN_NONE;

        }
    }

    /**
     * 重置scroller 使得scroller.getcurX获取到的值是对的
     */
    private void resetScroller() {
        scroller.startScroll(0, 0, 0, 0);
        offsetX = 0;
    }

    /**
     * 滑动距离够大,进行翻页调整
     *
     * @param isLeft 是否向左划  显示下一张
     */
    private void adjustPage(boolean isLeft) {
//        Log.e("adjustPage =========offsetX"+offsetX);
        if (offsetX == 0) {
            return;
        }

        int w = isLeft ? -drawRect.width() : drawRect.width();
        direction = w == 0 ? IPageTurner.TURN_NONE : (w > 0 ? IPageTurner.TURN_PREVIOUS : IPageTurner.TURN_NEXT);
        int diaX = (int) (w - offsetX);

        scroller.startScroll(scroller.getCurrX(), 0, diaX, 0);
        mIsAdjust = true;
        drawable.updateView();

    }


    /**
     * 滑动距离不够 回滚处理
     */
    private void doScrollBack() {

        scroller.startScroll((int)offsetX, 0, -(int)offsetX, 0);
        drawable.updateView();

    }


    private boolean isCancelMove(MotionEvent e1, MotionEvent event) {

        int direction = (int) (event.getX() - e1.getX());
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (direction > 0 && !canTurnPrevious()

                        || direction < 0 && !canTurnNext()
                        ) {
                    doScrollBack();
                    if (pageSwitchListener != null)
                        pageSwitchListener.onPageTurnFail(direction > 0 ? PageSwitchListener.TURN_PREVIOUS : PageSwitchListener.TURN_NEXT);
                    drawable.updateView();
                    return true;
                }

                break;

        }
        return false;
    }

    /**
     * 处理fling操作
     *
     * @param e1        开始事件
     * @param e2        结束事件
     * @param velocityX 水平每秒滑动的像素
     * @return
     */
    private boolean cancelFlingAndSroll(MotionEvent e1, MotionEvent e2, float velocityX) {
        int direction = (int) (e2.getX() - e1.getX());
        switch (e2.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                Log.e("==============velocityX=" + velocityX + "==============direction=" + direction + "===========e2.getX()=" + e2.getX() + "====scroller.getCurrX()=" + scroller.getCurrX());
                if (Math.abs(direction) > drawRect.width() / 2 || Math.abs(velocityX) > drawRect.width() * 1.5) {
                    mIsFling = true;
                    scroller.forceFinished(true);
                    int startX = scroller.getCurrX();
                    if (Math.abs(startX) == drawRect.width()) {
                        startX = 0;
                    }
                    if (direction < 0) {

                        scroller.startScroll(startX, 0, -drawRect.width() - startX, 0);
                    }

                    if (direction > 0) {

                        scroller.startScroll(startX, 0, (drawRect.width() - startX), 0);
                    }


                    return true;
                }

        }
        return false;

    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //  Log.e("onDoubleTap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //  Log.e("onDoubleTapEvent");
        return false;
    }

    @Override
    public void setOnScrollListener(ScrollListener listener) {

    }

    @Override
    public void onDraw(Canvas canvas) {

        if (drawParam == null) return;

        drawRect.set(0, 0, drawParam.width, drawParam.height);
        canvas.clipRect(0, 0, drawParam.width, drawParam.height);
        onDraw(canvas, direction, offsetX, bitmapProvider);


        computeScroll();
    }

    protected abstract void onDraw(Canvas canvas, int direction, float offsetX, BitmapProvider provider);
}
