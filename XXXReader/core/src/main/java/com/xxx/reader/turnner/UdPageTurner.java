package com.xxx.reader.turnner;

import android.content.Context;
import android.graphics.Canvas;

import com.xxx.reader.BackgroundManager;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;

public class UdPageTurner extends AbsPageTurner {


    public float startY;

    public UdPageTurner(Context context, PageSwitchListener pageSwitchListener, IDrawable drawable, BitmapProvider bitmapProvider) {
        super(context, pageSwitchListener, drawable, bitmapProvider);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

         






    }
}
