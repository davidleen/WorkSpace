package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;


/**
 * 滑动式
 * Created by davidleen29 on 2017/8/29.
 */

public class ScrollPageTurner extends FlipPageTurner {


    public ScrollPageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);
    }



    /**
     * 裁剪并绘制当前的漫画
     *
     * @param canvas
     * @param bitmapHolder
     */
    private void drawCurrent(Canvas canvas, BitmapHolder bitmapHolder) {

        canvas.save();
//        float left = offsetX < 0 ? 0 - offsetX : 0;
//        float right = offsetX < 0 ? drawParam.width : drawParam.width - offsetX;
//        canvas.clipRect(left, 0, right, drawParam.height);
//        drawCache.getCurrentBitmap().paint(canvas, drawRect, drawRect , drawable);
//        Bitmap bitmap = bitmapHolder.lockRead();
//
//        Log.e("drawRect:"+drawRect);
//        canvas.drawBitmap(bitmap, drawRect, drawRect, null);
//
//        bitmapHolder.unLockRead();
        bitmapHolder.draw(canvas);
        canvas.restore();
    }


    /**
     * 裁剪并绘制当前的漫画的前一张图
     *
     * @param canvas
     * @param bitmapHolder
     */
    private void drawPrev(Canvas canvas, BitmapHolder bitmapHolder) {
        canvas.save();
        canvas.translate(-drawParam.width, 0);

//        float left = drawParam.width - offsetX;
//        float right = drawParam.width;
//        canvas.clipRect(left, 0, right, drawParam.height);


//        Bitmap bitmap = bitmapHolder.lockRead();
//        canvas.drawBitmap(bitmap, drawRect, drawRect, null);
//        bitmapHolder.unLockRead();
        bitmapHolder.draw(canvas);
        canvas.restore();
    }


    /**
     * 裁剪并绘制当前的漫画的后一张图
     *
     * @param canvas
     * @param bitmapHolder
     */
    private void drawNext(Canvas canvas, BitmapHolder bitmapHolder) {
        canvas.save();
        canvas.translate(drawParam.width, 0);
//        float left = 0;
//        Log.e("=====================drawNext ============offsetX=" + offsetX);
//        float right = 0 - offsetX;
////        left = 0;
////        right = drawParam.width;
//        canvas.clipRect(left, 0, right, drawParam.height);


//        Bitmap bitmap = bitmapHolder.lockRead();
//        canvas.drawBitmap(bitmap, drawRect, drawRect, null);
//        bitmapHolder.unLockRead();
        bitmapHolder.draw(canvas);

        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas, int direction, float offsetX, BitmapProvider provider) {





        canvas.save();


        float dx = offsetX + (direction == TURN_NEXT ? -drawParam.width : 0);
        Log.e("dx:"+dx+",offsetX:"+offsetX);
        canvas.translate(dx, 0);

        //裁剪并绘制当前页
        drawCurrent(canvas, provider.getCurrentBitmap());

        if (direction==TURN_PREVIOUS) {
            drawPrev(canvas, provider.getPreviousBitmap());
//            canvas.restore();
        }
        if (direction==TURN_NEXT) {
            drawNext(canvas, provider.getNextBitmap());
        }


        canvas.restore();


//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        canvas.drawLine(0, drawParam.height / 2, drawParam.width, drawParam.height / 2 + 1, paint);


    }
}
