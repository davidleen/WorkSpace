package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.view.MotionEvent;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.turnner.sim.PageTurnHelper;

import static com.xxx.reader.turnner.sim.PageTurnHelper.PAGGING_SLOP;

public class SimulatePageTurner extends AbsPageTurner {
    private PointF lastTouch = new PointF();
    private PointF firstTouch = new PointF();
    private int direction = TURN_NONE;
    private boolean isDirectionSetting = false;
    private boolean scrolling = false;


    private Path currentPageArea=new Path();
    private Path downPageArea =new Path();
    private Path currentBackArea=new Path();
    Simulate simulate;


    private boolean hasStartScroll = false;
    PointF startPoint = new PointF();


    public SimulatePageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);
        simulate = new Simulate();
        mPaint=new Paint();
    }
    Paint mPaint;
    Matrix currentBackMatrix=new Matrix();
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        if(isDirectionSetting)
        {


            //获取当前页和底页
            BitmapHolder topPage = null;
            BitmapHolder bottomPage = null;
            if (direction == TURN_PREVIOUS  ) {
                topPage = bitmapProvider.getPreviousBitmap();
                bottomPage = bitmapProvider.getCurrentBitmap();

            } else if (direction == TURN_NEXT  ) {
                topPage = bitmapProvider.getCurrentBitmap();
                bottomPage = bitmapProvider.getNextBitmap();
            }


            Path lastPath = null;
            if (topPage != null) {


                simulate.generateCurrentPageArea(currentPageArea);
                canvas.save();
                canvas.clipPath(currentPageArea, Region.Op.XOR);
                topPage.draw(canvas);
                canvas.restore();


                simulate.generateCurrentBackPageArea(currentBackArea);

                canvas.save();

                canvas.clipPath(currentBackArea, Region.Op.INTERSECT);


                 mPaint.setColorFilter(PageTurnHelper.getColorMatrixColorFilter());
                simulate.generateCurrentBackMatrix(currentBackMatrix);
                canvas.setMatrix(currentBackMatrix);
                topPage.draw(canvas,mPaint);
               // canvas.drawColor(Color.parseColor("#88ff0000") );
                canvas.restore();

                simulate.drawCurrentHorizontalPageShadow(canvas,currentPageArea);
            }


            if (bottomPage != null) {

                simulate.generateDownPageArea(downPageArea);
                canvas.save();
                canvas.clipPath(downPageArea, Region.Op.INTERSECT);
                bottomPage.draw(canvas);
                canvas.restore();
             // drawUndersidePageAreaAndShadow(mCanvas, bottomPage, lastPath);

            }

            simulate.drawCurrentPageShadow(canvas,currentPageArea);


//            mCanvas.restore();




        }else
        {
            bitmapProvider.getCurrentBitmap().draw(canvas);
        }



        simulate.draw(canvas);
        computeScroll();
    }

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
    public boolean onDown(MotionEvent e) {

        abortAnimation();
        hasStartScroll = false;
        isDirectionSetting = false;
        float eX = e.getX();
        float eY = e.getY();


        firstTouch.x = eX;
        firstTouch.y = eY;
        lastTouch.x = eX;
        lastTouch.y = eY;

        startPoint.set(0, 0);

        return true;
    }


    public void abortAnimation() {
        try {

            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }
//            setSpeedUpState(false);
//
//            if (isInAnimation()) {
//
//                pageTurnType = 0;
//                if (turnMoveDirection == TURN_NEXT) {
//
//                    pageTurnType = PageSwitchListener.TURN_NEXT;
//                } else if (turnMoveDirection == TURN_PREVIOUS) {
//                    pageTurnType = PageSwitchListener.TURN_PREVIOUS;
//
//
//                }
//                pageSwitchListener.afterPageChanged(pageTurnType);
//
//
//                animating = false;
//
//            }

            drawable.updateView();

        } catch (NullPointerException e) {
        }
    }

    protected void computeScroll() {


        if (scroller.computeScrollOffset()) {


            int offsetX = scroller.getCurrX();
            int offsetY = scroller.getCurrY();


            Log.e("==========offsetX:" + offsetX + ",offsetY:" + offsetY + ",isDirectionSetting:" + isDirectionSetting);


            simulate.calculatePoints(offsetX, offsetY);


            drawable.updateView();

        }


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
        Log.e("scroller:" + scroller.getStartX() + ",e2.getx()" + e2.getX() + ",e2.getY()" + e2.getY() + ", scroller.getFinalX():" + scroller.getFinalX() + ", scroller.getFinalY():" + scroller.getFinalY() + ",distanceX:" + distanceX);

        if (Math.abs(distanceX) > PAGGING_SLOP || Math.abs(distanceY) > PAGGING_SLOP)

            printLog();
        //还未判滚动方向
        if (!isDirectionSetting) {

            int turnMoveDirection = getTurnMoveDirection(e2.getX(), e2.getY());

            if (turnMoveDirection != TURN_NONE) {

                direction = turnMoveDirection;
                isDirectionSetting = true;
                setDirection(e2.getX(), e2.getY());
                startPoint.set(0, 0);

            }


        }

        if (!isDirectionSetting) return false;


        doScrollTo((int) e2.getX(), (int) e2.getY());

        drawable.updateView();
        return true;
    }


    public void setDirection(float eX, float eY) {
        isDirectionSetting = true;

        if (direction == TURN_NEXT) {
            simulate.mCornerTop.x = drawParam.width;
            simulate.mCornerTop.y = 0;
            simulate.mCornerBottom.x = drawParam.width;
            simulate.mCornerBottom.y = drawParam.height;


        } else if (direction == TURN_PREVIOUS) {
            simulate.mCornerTop.x = 0;
            simulate.mCornerTop.y = 0;
            simulate.mCornerBottom.x = 0;
            simulate.mCornerBottom.y = drawParam.height;

        }


        simulate.setDirection(eX,eY);


    }


    @Override
    public void updateDrawParam(DrawParam drawParam) {
        super.updateDrawParam(drawParam);
        simulate.updateDraParam(drawParam);
    }

    private void doScrollTo(int x, int y) {
        int offsetX = 0;
        int offsetY = 0;
        if (!hasStartScroll) {
            offsetX = (int) startPoint.x;
            offsetY = (int) startPoint.y;

        } else {
            if (!scroller.isFinished()) {
                scroller.abortAnimation();

            }
            offsetX = scroller.getFinalX();
            offsetY = scroller.getFinalY();


        }


        hasStartScroll = true;
        scroller.startScroll((int) offsetX, offsetY, x - offsetX, y - offsetY, 10000);
        Log.e("scroller:" + scroller.getStartX() + ",e1.getx()" + x + ",e2.getY()" + y + ", scroller.getFinalX():" + scroller.getFinalX() + ", scroller.getFinalY():" + scroller.getFinalY() + ",direction:" + direction);
        drawable.updateView();
    }

    private void printLog() {

        Log.e("isDirectionSetting：" + isDirectionSetting + ",direction:" + direction);
    }

    /**
     * 翻页趋势
     *
     * @param x
     * @param y
     * @return
     */
    public int getTurnMoveDirection(float x, float y) {
        int turn = TURN_NONE;

        float difference = firstTouch.x - x;
        if (!isDirectionSetting && difference != 0) {
            if (difference < -PAGGING_SLOP) {//PREVIOUS
                turn = TURN_PREVIOUS;
            } else if (difference > PAGGING_SLOP) {//NEXT
                turn = TURN_NEXT;
            } else if (x < drawParam.width / 3) {

                turn = TURN_PREVIOUS;

            } else {
                turn = TURN_NEXT;
            }


        }

        return turn;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        int x = (int) e.getX();
        int y = (int) e.getY();
        //判断前翻还是后翻
        //还未判滚动方向
        if (!isDirectionSetting) {

            int turn = TURN_NONE;
            if (x < drawParam.width / 3) {

                turn = TURN_PREVIOUS;

            } else if (x > drawParam.width * 2 / 3) {
                turn = TURN_NEXT;
            }


            if (turn != TURN_NONE) {

                direction = turn;
                isDirectionSetting = true;

            }


        }

        if (isDirectionSetting) {
            doScrollTo(x, y);
        }


        printLog();

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
