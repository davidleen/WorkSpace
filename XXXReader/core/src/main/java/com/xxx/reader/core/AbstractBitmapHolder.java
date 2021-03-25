package com.xxx.reader.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import com.xxx.reader.text.layout.BitmapHolder;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by davidleen29 on 2018/3/21.
 */

public class AbstractBitmapHolder implements BitmapHolder {




    public AbstractBitmapHolder(int width, int height) {


    }






    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }





    @Override
    public void draw(Canvas canvas) {




    }

    @Override
    public void draw(Canvas canvas, Paint mPaint) {

    }

    @Override
    public void draw(Canvas canvas,     Path path) {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}
