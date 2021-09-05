package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapProvider;

import static com.xxx.reader.turnner.sim.PageTurnHelper.PAGGING_SLOP;


/**
 * 滚动式
 * Created by davidleen29 on 2017/8/29.
 */

public abstract class FlipPageTurner extends AbsPageTurner implements GestureDetector.OnGestureListener {


    public static final int SCROLL_DURATION = 888;
    /**
     * 是否触发了fling的标志
     */
    private boolean mIsFling = false;

    boolean animating = false;


    /**
     * 是否要进行翻页调整
     */
    private boolean mIsAdjust = false;

    private boolean isTouching = false;
    /**
     * 滑动的距离 根据这个距离进行绘制
     */
    protected float offsetX;

    Point downPoint = new Point();


    /**
     * 翻页方向
     * {@link com.xxx.reader.core.IPageTurner#TURN_NEXT }
     * {@link com.xxx.reader.core.IPageTurner#TURN_NONE }
     * {@link com.xxx.reader.core.IPageTurner#TURN_PREVIOUS }
     */


    boolean turnWorking = false;
    private int direction = 0;


    protected Rect drawRect = new Rect();
    private boolean scrollingBack;
    private boolean scrolling;

    public FlipPageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);

    }


    @Override
    public boolean onDown(MotionEvent e) {

        downPoint.y = (int) e.getY();
        downPoint.x = (int) e.getX();
        if (!scroller.isFinished()) {
            scroller.abortAnimation();

             drawable.updateView();
        }

        scrolling=false;
        mIsFling=false;
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e("onScroll==============distanceX:"+distanceX);
        if (mIsFling) return false;
        int currX=0;
        if (!turnWorking) {

            if ((Math.abs(distanceX) > PAGGING_SLOP || Math.abs(distanceY) > PAGGING_SLOP)) {
                if (downPoint.x > drawParam.width * 2 / 3)
                    direction = IPageTurner.TURN_NEXT;
                else if (downPoint.x < drawParam.height / 3)
                    direction = IPageTurner.TURN_PREVIOUS;
                else {
                    direction = distanceX > 0 ? IPageTurner.TURN_PREVIOUS : IPageTurner.TURN_NEXT;
                }
            }
            offsetX=direction==TURN_NEXT? drawParam.width:0;


//            resetScroller();
            turnWorking = true;
        }
        if (!turnWorking) return false;


        scrolling=true;

//        switch (e2.getAction()) {
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//
//
//
//                if (direction < 0 && !canTurnPrevious()
//
//                        || direction > 0 && !canTurnNext()
//                ) {
//                    doScrollBack();
//                    if (pageSwitchListener != null)
//                        pageSwitchListener.onPageTurnFail(direction);
//                    drawable.updateView();
//
//                } else {
//                    scrollRest(direction);
//                }
//
//                return true;
//
//
//        }

        if(!scroller.isFinished())
        {
            scroller.abortAnimation();
              offsetX = scroller.getFinalX();
        }

        scroller.startScroll((int) offsetX, 0,  (int)- distanceX, 0,100);
        Log.e("scroller:" + scroller.getStartX()+",e1.getx()"+e1.getX() +",e2.getx()"+e2.getX() + ", scroller.getFinalX():" + scroller.getFinalX() + ",direction:" + direction);
        drawable.updateView();
        return true;
//        return true;
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
        Log.e("=================velocityX=" + (velocityX) + " ,offsetX =" + offsetX+(e2.getAction()==MotionEvent.ACTION_UP));
        if (mIsFling) return false;
        if(direction==0)
        {
            direction=velocityX<0?TURN_NEXT:TURN_PREVIOUS;
        }

        scrolling=false;
        if (direction == TURN_PREVIOUS && !canTurnPrevious()

                || direction == TURN_NEXT && !canTurnNext()
        ) {
            doScrollBack();
            if (pageSwitchListener != null)
                pageSwitchListener.onPageTurnFail(direction > 0 ? PageSwitchListener.TURN_PREVIOUS : PageSwitchListener.TURN_NEXT);

            return true;
        }


        if (  Math.abs(velocityX) > drawRect.width() * 1.5) {
            mIsFling = true;

            int startX=  (int) e2.getX();
            if(!scroller.isFinished())
            {
                scroller.abortAnimation();
                startX=scroller.getFinalX();
            }



            if (direction == TURN_NEXT) {

                scroller.startScroll(startX, 0, 0 - startX, 0);
            }

            if (direction == TURN_PREVIOUS) {

                scroller.startScroll(startX, 0, (drawRect.width() - startX), 0);
            }
            drawable.updateView();


        }else
        {
            doScrollBack();
        }


        return true;


    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {


        boolean handled = false;
        if (pageSwitchListener != null) {
            int x = (int) e.getX();
            int y = (int) e.getY();






            handled= startTurn(x,y);




        }


        return handled;


    }


    @Override
    protected boolean startTurn(int x, int y) {

        boolean handled=false;

        int width = drawParam.width;
        int height = drawParam.height;

        int startX = 0;
        if (x < width / 3) {
//                resetScroller();

            handled = true;
            direction = IPageTurner.TURN_PREVIOUS;
            turnWorking = true;
            startX=0;
            scroller.startScroll(startX, 0, drawRect.width()  , 0, SCROLL_DURATION);

        } else if (x > width * 2 / 3) {
//                resetScroller();

            handled = true;
            turnWorking = true;
            direction = IPageTurner.TURN_NEXT;
            startX=drawRect.width();
            scroller.startScroll(startX, 0, -drawRect.width()  , 0, SCROLL_DURATION);
        }

        offsetX=startX;
        if (handled) drawable.updateView();




        return handled;


    }

    private void scrollRest(int direction) {
        scroller.forceFinished(true);
        int startX = scroller.getCurrX();
        if (Math.abs(startX) == drawRect.width()) {
            startX = 0;
        }
        if (direction ==IPageTurner.TURN_NEXT) {

            scroller.startScroll(startX, 0,   - startX, 0);
        }

        if (direction ==IPageTurner.TURN_PREVIOUS) {

            scroller.startScroll(startX, 0, (drawRect.width() - startX), 0);
        }

        drawable.updateView();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.e("onSingleTapConfirmed");
        return super.onSingleTapConfirmed(e);

    }


    protected void computeScroll() {
        if (drawParam == null) return;

        Log.e("==========" + offsetX+",mIsFling:"+mIsFling+",turnWorking:"+turnWorking);
        if (!scroller.computeScrollOffset()) {
//            if (mIsFling) { //快速滑动处理
//                handleFling();
//                resetScroller();
//                direction = IPageTurner.TURN_NONE;
//                return;
//            }


            Log.e("offsetX：" + offsetX);
            if (!scrollingBack && turnWorking) {
//                if (!isTouching && offsetX > -drawParam.width && direction == IPageTurner.TURN_NEXT) {
//
//                    //scroll the rest;
//                    scroller.startScroll((int) offsetX, 0, (int) (-drawParam.width - offsetX), 0);
//
//                }
//                if (!isTouching && offsetX < drawParam.width && direction == IPageTurner.TURN_PREVIOUS) {
//                    //scroll the rest;
//                    scroller.startScroll((int) offsetX, 0, (int) (drawParam.width - offsetX), 0);
//
//                }


                if (scroller.getCurrX() <=0 && direction == IPageTurner.TURN_NEXT && canTurnNext() && pageSwitchListener != null) {
                    Log.e("XXXXXXXXXXXXXXXX");
                    pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_NEXT);
                    offsetX = 0;
                    drawable.updateView();
                    turnWorking = false;
                    direction = IPageTurner.TURN_NONE;
                }
                if (scroller.getCurrX() >= drawParam.width && direction == IPageTurner.TURN_PREVIOUS && canTurnPrevious() && pageSwitchListener != null) {
                    Log.e("XXXXXXXXXXXXXXXX");
                    pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_PREVIOUS);
                    offsetX = 0;
                    drawable.updateView();
                    turnWorking = false;
                    direction = IPageTurner.TURN_NONE;
                }


            }
        } else {

            offsetX = scroller.getCurrX();
            drawable.updateView();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                isTouching = false;
//                break;
//            case MotionEvent.ACTION_DOWN:
//                scrollingBack = false;
//                isTouching = true;
//                break;
//        }


        boolean b = super.onTouchEvent(event);


            switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouching = false;

                if(turnWorking&&scrolling)
                {

                    scrollRest(direction);
                }





                break;
            case MotionEvent.ACTION_DOWN:

                break;
        }

        return b;


    }

    /**
     * 是否需要回滚到当前页
     *
     * @return
     */
    private boolean isNeedScrollBack() {

        if (offsetX > 0 && offsetX < drawParam.width / 2 && direction == IPageTurner.TURN_PREVIOUS)
            return true;
        if (offsetX < 0 && offsetX > -drawParam.width / 2 && direction == IPageTurner.TURN_NEXT)
            return true;
        return false;
    }

    /**
     * 处理快速滑动
     */
    private void handleFling() {
        mIsFling = false;
        if (offsetX < 0 && canTurnNext() && pageSwitchListener != null) {
            pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_NEXT);
            direction = IPageTurner.TURN_NONE;

        }
        if (offsetX > 0 && canTurnPrevious() && pageSwitchListener != null) {
            pageSwitchListener.afterPageChanged(PageSwitchListener.TURN_PREVIOUS);
            direction = IPageTurner.TURN_NONE;

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
     * 滑动距离不够 回滚处理
     */
    private void doScrollBack() {
        Log.e("do scroll backing------------");
        scroller.startScroll((int) offsetX, 0, -(int) offsetX, 0);
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

                    return true;
                }

                break;

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

        Log.e("offsetX:"+offsetX+",direction:"+direction);
        onDraw(canvas, direction, offsetX, bitmapProvider);

        computeScroll();
    }

    protected abstract void onDraw(Canvas canvas, int direction, float offsetX, BitmapProvider provider);


}
