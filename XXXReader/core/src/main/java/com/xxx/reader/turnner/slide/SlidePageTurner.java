package com.xxx.reader.turnner.slide;

import android.content.Context;
import android.graphics.Canvas;

import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.turnner.FlipPageTurner;


/**
 * 滑动式
 * Created by davidleen29 on 2017/8/29.
 */

public class SlidePageTurner extends FlipPageTurner {


    public SlidePageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);
    }



    @Override
    protected void onDraw(Canvas canvas, int direction, float offsetX, BitmapProvider bitmapProvider) {
        if (drawParam == null) return;

        drawRect.set(0, 0, drawParam.width, drawParam.height);
        canvas.clipRect(0, 0, drawParam.width, drawParam.height);
        BitmapHolder topHolder=bitmapProvider.getCurrentBitmap();
        BitmapHolder bottomHolder=bitmapProvider.getCurrentBitmap();

        float topOffset=offsetX;
        if (direction== IPageTurner.TURN_PREVIOUS) {
            topOffset=-drawParam.width+offsetX;
            topHolder=bitmapProvider.getPreviousBitmap();
            bottomHolder=bitmapProvider.getCurrentBitmap();
        }else
        if (direction== IPageTurner.TURN_NEXT) {
            topOffset=offsetX;
            topHolder=bitmapProvider.getCurrentBitmap();
            bottomHolder=bitmapProvider.getNextBitmap();

        }

        bottomHolder.draw(canvas);

        canvas.save();
        canvas.translate(topOffset, 0);
        topHolder.draw(canvas);
        canvas.restore();
    }
}
