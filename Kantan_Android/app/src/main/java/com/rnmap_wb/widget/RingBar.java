package com.rnmap_wb.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class RingBar extends View {
    private int ringColor;
    private float ringWidth;
    private float progress;

    Paint paint;
    public RingBar(Context context) {
        super(context);
        init(context);
    }

    public RingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {


        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        rect=new RectF();
        ringColor= Color.BLUE;
        ringWidth=10;
        progress=66;
    }


    public void  setRingWidth(float width)
    {

        this.ringWidth = width;
        invalidate();

    }

    public void setRingColor(int color)
    {
        this.ringColor=color;
        invalidate();

    }



    private RectF rect;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       int mRadius= (int) ((getWidth()-ringWidth*2)/2);

          float degree=progress*360;
        int viewSize = (int) (mRadius * 2);
        int left = (getWidth() - viewSize) / 2;
        int top = (getHeight() - viewSize) / 2;
        int right = left + viewSize;
        int bottom = top + viewSize;
        rect.set(left, top, right, bottom);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringWidth);
         paint.setColor(Color.LTGRAY);
         canvas.save();
         canvas.translate(rect.left,rect.top);
         canvas.drawCircle(rect.width() / 2, rect.height() / 2, rect.width()/2, paint);
         canvas.restore();
        paint.setColor(ringColor);
        canvas.drawArc(rect,-90,(float)  degree ,false,paint);






    }

    public void setProgress(float v) {
        this.progress=v;
        invalidate();

    }
}
