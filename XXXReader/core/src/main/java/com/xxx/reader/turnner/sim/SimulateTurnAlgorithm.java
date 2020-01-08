package com.xxx.reader.turnner.sim;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;

import com.xxx.reader.text.layout.BitmapHolder;

public class SimulateTurnAlgorithm {


    public Rect area=new Rect();


    public void setSize(int width,int height){}


    /**
     * 当前页的显示区域
     * (扣掉下一页显示区域和当前页的背影区域)
     */
    private Path drawCurrentPageArea(Canvas canvas, BitmapHolder topPage) throws Throwable {
        Path tempPath = new Path();
//        if (canvas != null) {
//            //下一页显示区域和当前页的背影区域
//            canvas.save();
//
//            tempPath = isHorizonTurnning ? PageTurnHelper.drawPolygon(mTouchBottom, mCornerBottom, mCornerTop, mTouchTop) :
//                    PageTurnHelper.drawCurrentPagePolygon(mBezierHorizontal, mBezierVertical, mTouchMove, mCorner);
//            if (needSpeedUp) {
//                if (mBezierVertical.control.x < 0) {
//                    return tempPath;
//                }
//                if (isXXhdpi()) {
//                    mPaint.setAntiAlias(mTouchMove.x > 0);
//                    mPaint.setSubpixelText(mTouchMove.x > 0);
//                }
//            }
//
//            canvas.clipPath(tempPath, Region.Op.XOR);
//            canvas.clipRect(0, 0, mShape.width, mShape.height);
//            canvas.translate(0, 0);
////            canvas.drawBitmap(topPage, 0, 0, mPaint);
//            topPage.draw(canvas);
//            canvas.restore();
//        }

        return tempPath;
    }
}
