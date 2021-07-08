package com.giants3.android.reader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.xxx.reader.text.page.PageDrawer;

public class TestReadView extends View {
    public void setDrawer(PageDrawer drawer) {
        this.drawer = drawer;
    }

    PageDrawer drawer;
    Paint paint=new Paint();
    public TestReadView(Context context) {
        super(context);
    }

    public TestReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestReadView(Context context,   AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestReadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(drawer!=null)
            drawer.onDraw(canvas,paint);
    }
}
