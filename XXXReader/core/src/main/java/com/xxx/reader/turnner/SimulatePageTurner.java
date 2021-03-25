package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.core.BuildConfig;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.turnner.sim.PageTurnHelper;

import static com.xxx.reader.turnner.sim.PageTurnHelper.PAGGING_SLOP;

public class SimulatePageTurner extends AbsPageTurner {
    public static final int DURATION = 800;
    private PointF lastTouch = new PointF();
    private PointF firstTouch = new PointF();
    private int direction = TURN_NONE;
    private boolean isDirectionSetting = false;
    private boolean scrolling = false;

    boolean useInter=true;

    private Path currentPageArea =new Path();
    private Path currentPageXORArea =new Path();
    private Path downPageArea =new Path();
    private Rect downPageShadowRect=new Rect();
    private Path currentBackArea=new Path();
    Simulate simulate;


    BitmapMesh bitmapMesh=new BitmapMesh();


    private boolean hasStartScroll = false;

    Handler handler;
    private boolean longPressed;

    private void doTurn()
    {
        //  int direction=  msg.arg1;
//        Log.e("handler:"+direction);
        pageSwitchListener.afterPageChanged(direction==TURN_PREVIOUS?PageSwitchListener.TURN_PREVIOUS:PageSwitchListener.TURN_NEXT);
        isDirectionSetting=false;
        direction=TURN_NONE;


    }
    public SimulatePageTurner(Context context, final PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case MSG_LONG_PRESS:
                        MotionEvent event = (MotionEvent) msg.obj;
                        onLongPress(event);
                        break;
                    case MSG_TURN:

                        doTurn();

                        break;
                }


            }
        }

        ;
        simulate = new Simulate();
        mPaint=new Paint();
        strokePaint=new Paint();
        strokePaint.setColor(Color.parseColor("#ffff00"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(20);


        fillPaint=new Paint();
        fillPaint.setColor(Color.parseColor("#80ff00ff"));
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
    public boolean DEBUG=false&&BuildConfig.DEBUG;
    Paint mPaint;
    Paint strokePaint;
    Paint fillPaint;
    Matrix currentBackMatrix=new Matrix();

    Path autoFlipPath=new Path();
    PathMeasure pathMeasure=new PathMeasure(autoFlipPath,false);
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(isDirectionSetting)
        {


            //获取当前页和底页
            BitmapHolder topPage = null;
            BitmapHolder bottomPage = null;
//            Log.e("direction on draw:"+direction);
            if (direction == TURN_PREVIOUS  ) {
                topPage = bitmapProvider.getPreviousBitmap();
                bottomPage =bitmapProvider.getCurrentBitmap();

            } else if (direction == TURN_NEXT  ) {
                topPage = bitmapProvider.getCurrentBitmap();
                bottomPage = bitmapProvider.getNextBitmap();
            }





            Path lastPath = null;
            if (topPage != null) {





                //绘制上面页
//             long time=System.currentTimeMillis();
              simulate.generateCurrentXORPageArea(currentPageXORArea);
//                canvas.save();
//                canvas.clipPath(currentPageXORArea, Region.Op.XOR);
//                topPage.draw(canvas);
//                canvas.restore();
//               Log.e("time use in draw current Page:"+(System.currentTimeMillis()-time));

//                long time=System.currentTimeMillis();
//                simulate.generateCurrentPageArea(currentPageArea);
//                canvas.save();
//                canvas.clipPath(currentPageArea);
//                topPage.draw(canvas);
//                canvas.restore();
//                Log.e("time use in draw current Page:"+(System.currentTimeMillis()-time));



               // long time=System.currentTimeMillis();

                simulate.generateCurrentPageArea(currentPageArea);

                topPage.draw(canvas,currentPageArea);

              //  Log.e("time use in draw current Page:"+(System.currentTimeMillis()-time));


                //绘制上面页的背面
                simulate.generateCurrentBackPageArea(currentBackArea);

                canvas.save();


                canvas.clipPath(currentBackArea, Region.Op.INTERSECT);


                mPaint.setColorFilter(PageTurnHelper.getColorMatrixColorFilter());
                simulate.generateCurrentBackMatrix(currentBackMatrix);
                canvas.setMatrix(currentBackMatrix);

                topPage.draw(canvas,mPaint);
//                if (simulate.isHorizontalTurning) {
//                    bitmapMesh.calculateHorizontal(simulate.mDragTop ,simulate.mFoldTop );
//                }else
//                {
//                    bitmapMesh.calculate(simulate.mBezierHorizontal ,simulate.mBezierVertical );
//                }
//
//                canvas.drawBitmapMesh(topPage.getBitmap(),BitmapMesh.WIDTH,BitmapMesh.HEIGHT,bitmapMesh.verts,0,null,0,mPaint);
//                canvas.drawBitmap(topPage.getBitmap(),0,0,mPaint);

                // canvas.drawColor(Color.parseColor("#33ffff00"));
                //canvas.drawPath(currentBackArea,strokePaint);
//                canvas.drawColor(Color.parseColor("#a0f9d7b1"));
                canvas.drawColor(TextSchemeContent.getBackPageColor());
                canvas.restore();

                if(DEBUG)
                {
                    canvas.drawPath(currentBackArea,strokePaint);
                }

               // simulate.drawCurrentHorizontalPageShadow(canvas,currentPageArea);
            }


            if (bottomPage != null) {

                simulate.generateDownPageArea(downPageArea);
                canvas.save();
                canvas.clipPath(currentPageXORArea);
                canvas.clipPath(downPageArea, Region.Op.INTERSECT);
                bottomPage.draw(canvas);


                simulate.drawUndersidePageShadow(canvas,downPageShadowRect  );
                canvas.restore();

            }


            simulate.drawCurrentPageShadow(canvas, currentPageXORArea);


//            mCanvas.restore();

            if(DEBUG) {
//                canvas.drawPath(currentPageXORArea, fillPaint);
                canvas.drawPath(currentPageArea, fillPaint);
            }


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
        handler.removeMessages(MSG_LONG_PRESS);
        Message message = handler.obtainMessage();
        message.what=MSG_LONG_PRESS;
        message.obj=MotionEvent.obtain(e);
        handler.sendMessageDelayed(message,1000);
        abortAnimation();
        useInter=false;
        scrolling=false;
        longPressed=false;
        hasStartScroll = false;
        isDirectionSetting = false;
        direction=TURN_NONE;
        float eX = e.getX();
        float eY = e.getY();


        firstTouch.x = eX;
        firstTouch.y = eY;
        lastTouch.x = eX;
        lastTouch.y = eY;


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
               int              pageTurnType = 0;
                if (direction == TURN_NEXT) {

                    pageTurnType = PageSwitchListener.TURN_NEXT;
                } else if (direction == TURN_PREVIOUS) {
                    pageTurnType = PageSwitchListener.TURN_PREVIOUS;


                }


                if(handler.hasMessages(MSG_TURN))
                {
                    handler.removeMessages(MSG_TURN);
                    doTurn();
                }

               // pageSwitchListener.afterPageChanged(pageTurnType);
//
//
//                animating = false;
//
//            }

            drawable.updateView();

        } catch (NullPointerException e) {
        }
    }
    float[] pos=new float[2];
    float[] tag=new float[2];

    protected void computeScroll() {



        if (scroller.computeScrollOffset()) {


            int offsetX = scroller.getCurrX();
            int offsetY = scroller.getCurrY();

            if (useInter)
            {
                boolean posTan = pathMeasure.getPosTan(offsetX, pos, tag);

             //Log.e("offsetX:"+offsetX+",==========pos:" + pos[0] + ", " + pos[1] +",posTan:"+posTan)  ;
                offsetX= (int) (pos[0]+0.5f);
                offsetY= (int) (pos[1]+0.5f);

            }


//            Log.e("==========offsetX:" + offsetX + ",offsetY:" + offsetY + ",isDirectionSetting:" + isDirectionSetting+",direction:"+direction);

            offsetY=Math.min(drawParam.height,Math.max(offsetY,1));
            offsetX=Math.max(-drawParam.width+1,Math.min(offsetX,drawParam.width));
//            Log.e("==========offsetX:" + offsetX + ",offsetY:" + offsetY + ",isDirectionSetting:" + isDirectionSetting+",direction:"+direction+",useInter:"+useInter);
            simulate.calculatePoints(offsetX, offsetY);

            drawable.updateView();
//            if(scroller.isFinished()&&useInter)
//            {
//
//                pageSwitchListener.afterPageChanged(direction==TURN_NEXT?PageSwitchListener.TURN_NEXT:PageSwitchListener.TURN_PREVIOUS);
//                useInter=false;
//
//            }


        }


    }


    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        handler.removeMessages(MSG_LONG_PRESS);



        //scroller.startScroll(startX,startY,-drawParam.width-startX,drawParam.height-startY,3000);
        int startX= (int) e.getX();
        int startY= (int) e.getY();
        setDirectionIfNeeded((int)e.getX(),(int)e.getY(),true);
        if(direction==TURN_NONE) return false;
        if(direction==TURN_PREVIOUS&&!canTurnPrevious()) return false;
        if(direction==TURN_NEXT&&!canTurnNext()) return false;

        hasStartScroll=true;

        simulate.getDestPoint(direction);


        useInter=true;
        autoFlipPath.reset();
        PointF dragCorner=simulate.getDragCorner();
       PointF pointF= simulate.getDestCorner();
       if(longPressed)
       {
           autoFlipPath.moveTo(startX,startY);
       }else {

           autoFlipPath.moveTo(dragCorner.x, dragCorner.y);
       }
        autoFlipPath.quadTo(0,drawParam.height/2,pointF.x,pointF.y);
        pathMeasure.setPath(autoFlipPath,false);
        scroller.startScroll(0,0, (int) pathMeasure.getLength(),0, DURATION);
        handler.sendEmptyMessageDelayed(MSG_TURN,DURATION);

        computeScroll();



        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        handler.removeMessages(MSG_LONG_PRESS);
//        Log.e("scroller:" + scroller.getStartX() + ",e2.getx()" + e2.getX() + ",e2.getY()" + e2.getY() + ", scroller.getFinalX():" + scroller.getFinalX() + ", scroller.getFinalY():" + scroller.getFinalY() + ",distanceX:" + distanceX+","+(e2.getAction()==MotionEvent.ACTION_UP));
        scrolling=true;
        if (Math.abs(distanceX) > PAGGING_SLOP || Math.abs(distanceY) > PAGGING_SLOP)

            printLog();

         setDirectionIfNeeded((int)e2.getX(),(int)e2.getY());
        if(!hasSetDirection())
        {
            return false;
        }
        doScrollTo((int) e2.getX(), (int) e2.getY());
        drawable.updateView();
        return true;



    }

    public boolean hasSetDirection()
    {
        return isDirectionSetting;
    }
    private void setDirectionIfNeeded(int x, int y)
    {
        setDirectionIfNeeded(x,y,false);
    }
    private void setDirectionIfNeeded(int x, int y,boolean mustDecideDirection)
    {

        //还未判滚动方向
        if (!isDirectionSetting) {

            int turnMoveDirection = getTurnMoveDirection(x, y,mustDecideDirection);
            if (turnMoveDirection != TURN_NONE) {

                direction = turnMoveDirection;
                isDirectionSetting = true;
                setDirection(x, y);

            }



        }


    }

    public void setDirection(float eX, float eY) {


//        if (direction == TURN_NEXT) {
            simulate.mCornerTop.x = drawParam.width;
            simulate.mCornerTop.y = 0;
            simulate.mCornerBottom.x = drawParam.width;
            simulate.mCornerBottom.y = drawParam.height;




//        } else if (direction == TURN_PREVIOUS) {
//            simulate.mCornerTop.x = 0;
//            simulate.mCornerTop.y = 0;
//            simulate.mCornerBottom.x = 0;
//            simulate.mCornerBottom.y = drawParam.height;
//
//        }


        simulate.setDirection(eX,eY,direction==TURN_NEXT);


    }


    @Override
    public void updateDrawParam(DrawParam drawParam) {
        super.updateDrawParam(drawParam);
        simulate.updateDraParam(drawParam);
        bitmapMesh.init(drawParam);
    }

    private void doScrollTo(int x, int y) {
        int offsetX = 0;
        int offsetY = 0;
        if (!hasStartScroll) {
            offsetX = (int) simulate.getDragCorner().x;
            offsetY = (int) simulate.getDragCorner().y;

        } else {
            if (!scroller.isFinished()) {
                scroller.abortAnimation();

            }
            offsetX = scroller.getFinalX();
            offsetY = scroller.getFinalY();}


        hasStartScroll = true;
        scroller.startScroll((int) offsetX, offsetY, x - offsetX, y - offsetY, DURATION);
       // Log.e("offset:"+offsetX+","+offsetY+"==scroller:" + scroller.getStartX() + ",e1.getx()" + x + ",e2.getY()" + y + ", scroller.getFinalX():" + scroller.getFinalX() + ", scroller.getFinalY():" + scroller.getFinalY() + ",direction:" + direction);
        computeScroll();
    }

    private void printLog() {

       // Log.e("isDirectionSetting：" + isDirectionSetting + ",direction:" + direction);
    }

    /**
     * 翻页趋势
     *
     * @param x
     * @param y
     * @return
     */
    public int getTurnMoveDirection(float x, float y) {

        return getTurnMoveDirection(x,y,false);

    }


    /**
     * 翻页趋势
     *
     * @param x
     * @param y
     * @return
     */
    public int getTurnMoveDirection(float x, float y,boolean singleTap) {
        int turn = TURN_NONE;

        if(singleTap)
        {


            if (x < drawParam.width / 3) {

                turn = TURN_PREVIOUS;
            } else
                if(x>drawParam.width*2/3) {
                    turn = TURN_NEXT;
                }

                return turn;


        }

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

        longPressed=true;
        int x = (int) e.getX();
        int y = (int) e.getY();

//        Log.e("onLongPress:"+x+","+y);
        //判断前翻还是后翻
        //还未判滚动方向
       setDirectionIfNeeded(x,y,true);



        if (hasSetDirection()) {
            doScrollTo(x, y);
        }


        printLog();

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {



        return false;
    }

    @Override
    protected boolean onActionUp(MotionEvent event) {



//       Log.e("onactionUp:"+event+",scrolling:"+scrolling);

        //未开始滚动 不处理，单点击时间 由onSingleTapUp 处理
        if(!scrolling)
        {
            return false;
        }

        int startX;int startY;
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        if(!scroller.isFinished())
        {
            scroller.forceFinished(true);
            startX=   scroller.getCurrX();
            startY=scroller.getCurrY();
        }else
        {

            startX= eventX;
            startY= eventY;
        }


        //scroller.startScroll(startX,startY,-drawParam.width-startX,drawParam.height-startY,3000);
        useInter=true;
        autoFlipPath.reset();

        //检查点击位置， 如果比down点 更接近起点，则翻页失败处理， 回滚正常
        PointF startPoint=simulate.getDragCorner();

        boolean rollback=false;
        if(Math.pow(eventX-startPoint.x,2)+Math.pow(eventY-startPoint.y,2) <Math.pow(firstTouch.x-startPoint.x,2)+Math.pow(firstTouch.y-startPoint.y,2) )
        {
            rollback=true;
        }


        PointF pointF=rollback?startPoint:simulate.getDestCorner();
        autoFlipPath.moveTo(startX,startY);
        autoFlipPath.lineTo( pointF.x
                ,pointF.y);
        pathMeasure.setPath(autoFlipPath,false);
        scroller.startScroll(0,0, (int) pathMeasure.getLength(),0, DURATION);
        if(!rollback)
            handler.sendEmptyMessageDelayed(MSG_TURN,DURATION);
//        scroller.startScroll(startX,startY,drawParam.width-startX,drawParam.height-startY,3000);
        computeScroll();

        return true;
    }
}
