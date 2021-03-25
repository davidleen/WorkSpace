package com.xxx.reader.text.layout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by davidleen29 on 2018/3/21.
 */

public interface BitmapHolder  extends TouchEventListener{











    void draw(Canvas canvas);
    void draw(Canvas canvas, Paint mPaint);
    void draw(Canvas canvas,  Path path);
    Bitmap  getBitmap();
}
