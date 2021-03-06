package com.xxx.reader.turnner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.MathUtils;
import com.xxx.reader.PointUtils;
import com.xxx.reader.core.BuildConfig;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.turnner.sim.PageTurnHelper;
import com.xxx.reader.turnner.sim.Shape;

import static com.xxx.reader.turnner.sim.PageTurnHelper.getLenghtShadow;

public class Simulate {


    public PointF mCornerTop = new PointF(0.0f, 0.0f);       //拖拽点对应的上页脚
    public PointF mCornerBottom = new PointF(0.0f, 0.0f); //拖拽点对应的下页脚
    public PointF mFoldTop = new PointF(0.0f, 0.0f);   //折合页上顶点
    public PointF mFoldBottom = new PointF(0.0f, 0.0f); //折合页下顶点
    public PointF mDrag = new PointF(0.0f, 0.0f);      //翻起页顶点=拖拽点
    public PointF mDragTop = new PointF(0.0f, 0.0f);      //翻起位置上顶点
    public PointF mDragBottom = new PointF(0.0f, 0.0f);      //翻起位置底部顶点
    public PointF mLeftTop = new PointF(0.0f, 0.0f);      //翻起位置底部顶点
    public PointF mLeftBottom = new PointF(0.0f, 0.0f);      //翻起位置底部顶点

    public PageTurnHelper.Bezier mBezierHorizontal = new PageTurnHelper.Bezier();                 //贝塞尔曲线
    public PageTurnHelper.Bezier mBezierVertical = new PageTurnHelper.Bezier();                 //另一条贝塞尔曲线
    //拖拽时 对应的页边角。
    public PointF mDragCorner = new PointF();  //拖拽时 对应的页边角。
    public PointF destCorner = new PointF();


    /**
     * 滚动路径起点
     */
    public PointF startScrollPoint = new PointF();  //拖拽时 对应的页边角。
    //滚动路径终点。
    public PointF endScrollPoint = new PointF();

    //下页的阴影旋转角度
    private float mDegrees;
    //对角线长度（触控点与对应边角）
    private float mTouch2Corner;
    /**
     * 翻页固定点。
     */
    public PointF mPinedPoint = new PointF();

    //中心点（翻起页顶点=拖拽点  与 翻起页对角）
    PointF mMidle = new PointF(0, 0);
    /**
     * 判断是否平行翻页，平行翻页情况下 ，不需要计算贝塞尔曲线
     */
    public boolean isHorizontalTurning = false;
    private DrawParam drawParam;
    private boolean mIsRtLb=true;       //是否属于右上左下
    private float mDiagonal;     //对角线

    /**
     * 正负值   1表示正向 对角在右下方。   -1 表示负向  对角在右上方。
     */
    private int signature;
    /**
     * 拖拽点阴影顶点位置。
     */
    PointF shadowVertexPoint =new PointF();

    /**
     * 贝塞尔曲线  阴影路径
     */
    Path BezierShadowPath=new Path();

    /**
     * 翻起页阴影区域
     */
    Rect currentBezierShadowRect=new Rect();

    public  boolean  DEBUG=BuildConfig.DEBUG&&false;


    public Simulate() {
        if (DEBUG) {
            paint = new Paint();
            paint.setColor(Color.RED);
        }
    }


    /**
     * 根据 屏幕x，y 值  ，计算翻起页顶点位置，
     *
     * @param offsetX
     * @param offsetY
     */
    public void calculatePoints(float offsetX, float offsetY) {










        if (isHorizontalTurning) {
            mDrag.set(offsetX, offsetY);

            float folderX = (float) (mDrag.x + (mCornerTop.x - mDrag.x) * 0.4);
            mFoldTop.set(folderX, 0);
            mFoldBottom.set(folderX, drawParam.height);

            mDragTop.set(offsetX,0);
            mDragBottom.set(offsetX,drawParam.height);



        } else {
            //计算出真正拖拽点对应的点
            mDrag.x=offsetX;
            mDrag.y=offsetY;
            PointUtils.middle(mMidle, mDrag, mDragCorner);
            //计算出最大高度的拖拽点对应的点
            float folderRate=0.25f;
            float Cx=Math.min(mMidle.x,mDragCorner.x*folderRate/(1+folderRate));

            float middleHeight = (float) Math.sqrt((mMidle.x - Cx) * (mDragCorner.x - mMidle.x));
            float maxMiddleY=signature*(mDragCorner.y- middleHeight);
            float maxDragY=maxMiddleY*2-mDragCorner.y;
            //与点击位置比较，
            if( signature==1)
            {
                mDrag.y=Math.max( maxDragY ,offsetY);
            }else
            {
                mDrag.y=Math.min(maxDragY,offsetY);
            }



            //重新计算中点
            PointUtils.middle(mMidle, mDrag, mDragCorner);
            /**
             * 点重合
              */

            if(MathUtils.getLength(mDrag,mDragCorner)<=1)
            {
                mBezierHorizontal.control.set(mDrag);
                mBezierHorizontal.start.set(mDrag);
                mBezierHorizontal.end.set(mDrag);
                mBezierVertical.start.set(mDrag);
                mBezierVertical.control.set(mDrag);
                mBezierVertical.end.set(mDrag);
            }else {


                //计算控制点。
                mBezierHorizontal.control.x = Math.min(mMidle.x,mMidle.x - (mMidle.y - mDragCorner.y) * (mMidle.y - mDragCorner.y) / (mDragCorner.x - mMidle.x));
                mBezierHorizontal.control.y = mDragCorner.y;
                //计算起点（）
                mBezierHorizontal.start.x = mBezierHorizontal.control.x - (mDragCorner.x - mBezierHorizontal.control.x) * folderRate;
                mBezierHorizontal.start.y = mDragCorner.y;

//
//                //   翻页限制检查
//                if (mBezierHorizontal.start.x <0f) {
//
//                    float width = mDragCorner.x - mBezierHorizontal.start.x;
//                    float radio = (mPinedPoint.x - mBezierHorizontal.start.x) / width;
//                    float newOffsetX = mDrag.x + (mDragCorner.x - mDrag.x) * radio;
//                    float newOffsetY = mDrag.y + (mDragCorner.y - mDrag.y) * radio;
//                    // Log.e("newOffsetX:"+newOffsetX+",newOffsetY:"+newOffsetY+",mBezierHorizontal.start.x:"+mBezierHorizontal.start.x);
//                    calculatePoints(newOffsetX, newOffsetY);
//                    return;
//
//                }


                //计算控制点。
                float dividerValue=(mDragCorner.y-mMidle.y);
                if(dividerValue==0){
                    dividerValue=signature*0.000001f;
                }
                mBezierVertical.control.y = mMidle.y - (mMidle.x - mDragCorner.x) * (mMidle.x - mDragCorner.x) /dividerValue;
                mBezierVertical.control.x = mDragCorner.x;
                //计算起点（）
                mBezierVertical.start.y = mBezierVertical.control.y - (mDragCorner.y - mBezierVertical.control.y) *folderRate;
                mBezierVertical.start.x = mDragCorner.x;


                //计算横线贝塞尔线的终点（两条贝塞尔线起点连线 跟 拖动点与贝塞尔控制点连线 的交点）
                PointUtils.calculateCross(mBezierHorizontal.end, mDrag, mBezierHorizontal.control, mBezierHorizontal.start, mBezierVertical.start);

                //计算纵向贝塞尔线的终点（两条贝塞尔线起点连线 跟 拖动点与纵向贝塞尔控制点连线 的交点）
                PointUtils.calculateCross(mBezierVertical.end, mDrag, mBezierVertical.control, mBezierHorizontal.start, mBezierVertical.start);


            }

            mBezierHorizontal.calculateVertex();
            mBezierVertical.calculateVertex();


        }
        mIsRtLb = PageTurnHelper.isRightTopOrLeftBottom(drawParam, mDragCorner);
        mTouch2Corner = (float) Math.hypot((mDrag.x - mDragCorner.x), (mDrag.y - mDragCorner.y));
        mDegrees = PageTurnHelper.getDegrees(mBezierHorizontal.control.x - mDragCorner.x, mBezierVertical.control.y - mDragCorner.y);

    }

    public void updateDraParam(DrawParam drawParam) {

        this.drawParam = drawParam;
        mDiagonal= (float) Math.hypot(drawParam.width,drawParam.height);
        mLeftTop.set(0,0);
        mLeftBottom.set(0,drawParam.height);
    }

    public void setDirection(float eX, float eY) {

    }
    public void setDirection(float eX, float eY,boolean turnNext) {


        if (Math.abs((eY - drawParam.height / 2)) < (drawParam.height / 6)) {
            isHorizontalTurning = true;
        } else {
            isHorizontalTurning = false;
        }

        int startScrollX=turnNext?drawParam.width:-drawParam.width;
        int endScrollX=turnNext?-drawParam.width: drawParam.width;
        if (isHorizontalTurning) {
            mDragCorner.set(drawParam.width, 0);
            destCorner.set(-drawParam.width,0);
            startScrollPoint.set(startScrollX,0);
            endScrollPoint.set(endScrollX,0);

        } else {

            if (eY < drawParam.height / 3) {
                mDragCorner.set(drawParam.width, 0);
                mPinedPoint.set(0, 0);
                destCorner.set(-drawParam.width,0);
                startScrollPoint.set(startScrollX,0);
                endScrollPoint.set(endScrollX,0);
                signature=-1;
            } else {
                mDragCorner.set(drawParam.width, drawParam.height);
                signature=1;
                mPinedPoint.set(0, drawParam.height);
                destCorner.set(-drawParam.width,drawParam.height);
                startScrollPoint.set(startScrollX,drawParam.height);
                endScrollPoint.set(endScrollX,drawParam.height);
            }
        }


        //startScrollPoint  endScrollPoint  终点y值微调，避免顶点计算时候除不尽异常。


    }


    Paint paint;

    public void draw(Canvas canvas) {


        if (DEBUG) {
            int radius = 10;
            canvas.drawCircle(mDrag.x, mDrag.y, radius, paint);
            canvas.drawCircle(mDragCorner.x, mDragCorner.y, radius, paint);
            canvas.drawCircle(mMidle.x, mMidle.y, radius, paint);
            mBezierVertical.draw(canvas, paint);
            mBezierHorizontal.draw(canvas, paint);


            canvas.drawCircle(shadowVertexPoint.x, shadowVertexPoint.y, 3, paint);


            canvas.drawCircle(mFoldTop.x, mFoldTop.y, radius, paint);
            canvas.drawCircle(mFoldBottom.x, mFoldBottom.y, radius, paint);
            canvas.drawCircle(mCornerTop.x, mCornerTop.y, radius, paint);

        }

    }

    public void generateCurrentXORPageArea(Path currentPageArea) {

        if (isHorizontalTurning) {
            drawPolygon(currentPageArea,mDragBottom, mCornerBottom, mCornerTop, mDragTop);
        } else
        {
            drawCurrentPagePolygon(currentPageArea,mBezierHorizontal, mBezierVertical, mDrag, mDragCorner);
        }


    }

    public void generateCurrentPageArea(Path currentPageArea) {

        if (isHorizontalTurning) {
            drawPolygon(currentPageArea,mDragBottom, mLeftBottom, mLeftTop, mDragTop);

        } else
        {




            currentPageArea.reset();


            //if(!mDrag.equals(mDragCorner)) {
                currentPageArea.moveTo(mBezierVertical.start.x, mBezierVertical.start.y);
             //   if(mBezierVertical.start.x>=0&&mBezierHorizontal.start.x>=0&&Math.abs(mBezierVertical.start.x-mBezierHorizontal.start.x)>1) {

                    currentPageArea.quadTo(mBezierVertical.control.x, mBezierVertical.control.y, mBezierVertical.end.x, mBezierVertical.end.y);
                    currentPageArea.lineTo(mDrag.x, mDrag.y);
                    if (mBezierHorizontal.end.x != Float.NaN && mBezierHorizontal.end.y != Float.NaN)
                        currentPageArea.lineTo(mBezierHorizontal.end.x, mBezierHorizontal.end.y);
                    currentPageArea.quadTo(mBezierHorizontal.control.x, mBezierHorizontal.control.y, mBezierHorizontal.start.x, mBezierHorizontal.start.y);
//                }else
//                {
//                    currentPageArea.lineTo(mBezierHorizontal.start.x, mBezierHorizontal.start.y);
//                }
//            }else
//            {
//                currentPageArea.lineTo(mDragCorner.x,mDragCorner.y);
//            }

            if(mDragCorner.y>drawParam.height/2)//右下角
            {
                currentPageArea.lineTo(0,drawParam.height);
                currentPageArea.lineTo(0,0);
                currentPageArea.lineTo(mDragCorner.x,0);

            }else
            {
                currentPageArea.lineTo(0,0);
                currentPageArea.lineTo(0,drawParam.height);
                currentPageArea.lineTo(mDragCorner.x,drawParam.height);
            }



            currentPageArea.close();


//            Log.e("mDragCorner:"+mDragCorner+",mBezierVertical.start:"+mBezierVertical.start+",mDrag:"+mDrag+",mBezierHorizontal.start:"+mBezierHorizontal.start+",currentPageArea:"+currentPageArea);
//            Log.e("mBezierVertical.control:"+mBezierVertical.control +",mBezierVertical.end:"+mBezierVertical.end );
//            Log.e("mBezierHorizontal.control:"+mBezierHorizontal.control +",mBezierHorizontal.end:"+mBezierHorizontal.end );



        }




    }


    /**
     * 绘制多边形
     */
    public static void drawPolygon(Path tempPath,PointF... points) {

        tempPath.reset();
        int length = points.length;
        if (points != null && length > 1) {
            tempPath.moveTo(points[0].x, points[0].y);
            for (int i = 1; i < length; i++) {
                tempPath.lineTo(points[i].x, points[i].y);
            }
            tempPath.close();
        }

    }


    public static void drawCurrentPagePolygon(Path tempPath,PageTurnHelper.Bezier bezierHorizontal, PageTurnHelper.Bezier bezierVertical, PointF touch, PointF corner) {

        tempPath.reset();
        if (bezierHorizontal != null && bezierVertical != null && touch != null && corner != null) {
            tempPath.moveTo(bezierHorizontal.start.x, bezierHorizontal.start.y);
            tempPath.quadTo(bezierHorizontal.control.x, bezierHorizontal.control.y,
                    bezierHorizontal.end.x, bezierHorizontal.end.y);
            tempPath.lineTo(touch.x, touch.y);
            tempPath.lineTo(bezierVertical.end.x, bezierVertical.end.y);
            tempPath.quadTo(bezierVertical.control.x, bezierVertical.control.y,
                    bezierVertical.start.x, bezierVertical.start.y);
            tempPath.lineTo(corner.x, corner.y);
            tempPath.close();
        }

    }

    public void generateDownPageArea(Path downPageArea) {
        if(isHorizontalTurning)
             drawPolygon(downPageArea,mFoldBottom, mCornerBottom, mCornerTop, mFoldTop);
        else
            drawPolygon(downPageArea,mBezierHorizontal.start, mBezierHorizontal.vertex,
                    mBezierVertical.vertex, mBezierVertical.start, mDragCorner);
    }

    public void generateCurrentBackPageArea(Path currentBackArea) {

        if(isHorizontalTurning)
        {
            drawPolygon(currentBackArea,mDragBottom, mFoldBottom, mFoldTop, mDragTop);

        }
        else
        {

            drawCurrentPagePolygon(currentBackArea,mBezierHorizontal, mBezierVertical, mDrag, mDragCorner);

        }
    }


    public void generateCurrentBackMatrix(Matrix matrix)
    {
        matrix.reset();
        if(isHorizontalTurning)
        {

            //平移
            matrix.preTranslate(-mCornerTop.x, 0);
            //x轴翻转
            matrix.postScale(-1, 1);
            //移动指定位置
            matrix.postTranslate(mDragTop.x, 0);
        }
        else
        {
            PageTurnHelper.getCurrentBackAreaMatrix(matrix,mDragCorner, mBezierHorizontal, mBezierVertical,mDrag);



        }



    }
    /**
     * 绘制翻起页
     */
    public void drawCurrentHorizontalPageShadow(Canvas canvas, Path lastPath) {


        if (canvas != null && lastPath != null) {
            //绘制翻起水平阴影
            canvas.save();
            canvas.clipPath(lastPath, Region.Op.XOR);
            canvas.clipRect(0, 0, drawParam.width, drawParam.height);

                GradientDrawable mCurrentPageShadow = PageTurnHelper.getFrontShadowDrawableVRL();
                  PageTurnHelper.getCurrentHorizontalShadowRect(currentBezierShadowRect,mDragBottom,
                        mFoldBottom, mTouch2Corner * 0.17f, drawParam);
                mCurrentPageShadow.setBounds(currentBezierShadowRect);

                mCurrentPageShadow.draw(canvas);

            canvas.restore();
        }

    }

    public   void getCurrentHorizontalShadowRect(Rect output,boolean isLeft, PointF move,  float touch2Corner ) {
        int left = 0, right = 0;

        if (isLeft ) {
            left = (int) move.x;
            right = (int) (move.x + getLenghtShadow(touch2Corner));
        } else {
            left = (int) (move.x - getLenghtShadow(touch2Corner));
            right = (int) move.x;
        }

          output.set(left, 0, right, drawParam.height);
    }
    Rect   horizontalShadowRect=new Rect();

    public void drawCurrentPageShadow(Canvas mCanvas,Path lastPath) {
//        if (!needSpeedUp || mBezierVertical.control.x > 0) {
            if (isHorizontalTurning) {
                drawCurrentHorizontalPageShadow(mCanvas, lastPath);
            } else {
                drawCurrentPageBezierShadow(mCanvas, lastPath);
            }
//        }
    }
    /**
     * 绘制翻起页
     */
    public void drawCurrentPageBezierShadow(Canvas canvas, Path lastPath)   {
        PointF mCorner=mDragCorner;
        DrawParam mShape=drawParam;
        if (canvas != null && lastPath != null) {
            float touch2Corner = mTouch2Corner * 0.17f, degrees = 0.0f;

            PageTurnHelper.getShadowVertexPoint(shadowVertexPoint,mBezierHorizontal, mDrag, mIsRtLb, touch2Corner);



            GradientDrawable mCurrentPageShadow = null;

            //绘制翻起水平阴影
            degrees = PageTurnHelper.getDegrees(mDrag.x - mBezierHorizontal.control.x, mBezierHorizontal.control.y - mDrag.y);
            if (mCorner.y == mShape.height && degrees > -89 || mCorner.y == 0 && (degrees < -95 || degrees > 0)) {
                canvas.save();
                canvas.clipPath(lastPath, Region.Op.XOR);

                drawPolygon(BezierShadowPath,shadowVertexPoint, mDrag,
                        mBezierHorizontal.control, mBezierHorizontal.start);
                canvas.clipPath(BezierShadowPath, Region.Op.INTERSECT);
                canvas.clipRect(0, 0, mShape.width, mShape.height);

                    mCurrentPageShadow = mIsRtLb ? PageTurnHelper.getFrontShadowDrawableVLR() : PageTurnHelper.getFrontShadowDrawableVRL();

                     PageTurnHelper.getCurrentHorizontalShadowRect(currentBezierShadowRect,mIsRtLb,
                            mBezierHorizontal, mDiagonal, touch2Corner);

                    mCurrentPageShadow.setBounds(currentBezierShadowRect);

                    canvas.rotate(PageTurnHelper.getDegrees(mDrag.x - mBezierHorizontal.control.x, mBezierHorizontal.control.y - mDrag.y),
                            mBezierHorizontal.control.x, mBezierHorizontal.control.y);
                    mCurrentPageShadow.draw(canvas);

                   // canvas.drawRect(currentBezierShadowRect,paint);

                canvas.restore();
            }

            //绘制翻起垂直阴影
            degrees = PageTurnHelper.getDegrees(mBezierVertical.control.y - mDrag.y, mBezierVertical.control.x - mDrag.x);
            if (mCorner.y == mShape.height && degrees < 85 || mCorner.y == 0 && degrees > -85) {
                canvas.save();
                canvas.clipPath(lastPath, Region.Op.XOR);
                drawPolygon(BezierShadowPath,shadowVertexPoint, mDrag,
                        mBezierVertical.control, mBezierVertical.start);
                canvas.clipPath(BezierShadowPath, Region.Op.INTERSECT);
                canvas.clipRect(0, 0, mShape.width, mShape.height);

                    mCurrentPageShadow = mIsRtLb ? PageTurnHelper.getFrontShadowDrawableHTB() : PageTurnHelper.getFrontShadowDrawableHBT();
                      PageTurnHelper.getCurrentVerticalShadowRect(currentBezierShadowRect,mIsRtLb,
                            mBezierVertical, mDiagonal, touch2Corner, mShape);
                    mCurrentPageShadow.setBounds(currentBezierShadowRect);

                    canvas.rotate(degrees, mBezierVertical.control.x, mBezierVertical.control.y);
                    mCurrentPageShadow.draw(canvas);
                   // canvas.drawRect(currentBezierShadowRect,paint);

                canvas.restore();
            }
        }
    }


    /**
     * 绘制下一页阴影部分
     * @param canvas
     */
    public void drawUndersidePageShadow(Canvas canvas, Rect bottomShadowRect) {
        if(isHorizontalTurning)
        {


                GradientDrawable mBackShadowDrawable = PageTurnHelper.getBackShadowDrawableLR();
                PageTurnHelper.getUndersideShadowRect(bottomShadowRect,mDragBottom, mFoldBottom, drawParam);
                mBackShadowDrawable.setBounds(bottomShadowRect);
                mBackShadowDrawable.draw(canvas);


        }else {

            canvas.rotate(mDegrees, mBezierHorizontal.start.x, mBezierHorizontal.start.y);

                GradientDrawable mBackShadowDrawable = mIsRtLb ?
                        PageTurnHelper.getBackShadowDrawableLR() : PageTurnHelper.getBackShadowDrawableRL();
                PageTurnHelper.getUndersideShadowRect(bottomShadowRect,mIsRtLb, mBezierHorizontal, mDiagonal, mTouch2Corner);
                mBackShadowDrawable.setBounds(bottomShadowRect);
                mBackShadowDrawable.draw(canvas);


        }
    }

    public void getDestPoint(int direction) {




    }

    public PointF getDragCorner() {


        return startScrollPoint;
    }   public PointF getDestCorner() {


        return endScrollPoint;
    }
//    /**
//     * 绘制翻起页
//     */
//    public void drawCurrentHorizontalPageShadow(Canvas canvas, Path lastPath) {
//        if (canvas != null && lastPath != null) {
//            //绘制翻起水平阴影
//            canvas.save();
//            canvas.clipPath(lastPath, Region.Op.XOR);
//            canvas.clipRect(0, 0, drawParam.width, drawParam.height);
//
//                GradientDrawable mCurrentPageShadow = PageTurnHelper.getFrontShadowDrawableVRL();
//                mCurrentPageShadow.setBounds(PageTurnHelper.getCurrentHorizontalShadowRect(mTouchBottom,
//                        mFoldBottom, mTouch2Corner * 0.17f, mShape));
//
//                mCurrentPageShadow.draw(canvas);
//
//            canvas.restore();
//        }
//    }


//    public static void getCurrentHorizontalShadowRect(Rect rect ,  PageTurnHelper.Bezier bezierHorizontal, float diagonal, float touch2Corner) {
//        int left = 0, right = 0;
//
//        if (isRtLb) {
//            left = (int) (bezierHorizontal.control.x);
//            right = (int) (bezierHorizontal.control.x + getLenghtShadow(touch2Corner));
//        } else {
//            left = (int) (bezierHorizontal.control.x - getLenghtShadow(touch2Corner));
//            right = (int) bezierHorizontal.control.x + 1;
//        }
//
//        return new Rect(left, (int) (bezierHorizontal.control.y - diagonal), right, (int) (bezierHorizontal.control.y));
//    }
}
