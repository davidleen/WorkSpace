package com.xxx.reader;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.giants3.android.frame.util.Log;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.prepare.DrawLayer;

/**
 * Created by davidleen29 on 2018/3/21.
 */

public class ReaderView  extends View implements IDrawable{
    private ReaderView.onSizeChangeLister onSizeChangeLister;

    public ReaderView(Context context) {
        this(context,null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr );
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    private void init()
    {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }
    public void setDrawLayer(DrawLayer drawLayer) {
        this.drawLayer = drawLayer;
    }

    DrawLayer drawLayer;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);
        if(drawLayer!=null)
        {
            boolean result = drawLayer.onTouchEvent(event);
            Log.e("touch result:"+result+",event:"+event);
            if(result) return true;
        }

        return b;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(drawLayer!=null)
        {
            drawLayer.onDraw(canvas);
        }



    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        DrawParam drawParam=new DrawParam();
        drawParam.width=w;
        drawParam.height=h;

       // drawParam.padding=new int[]{30,80,30,80};
        if(drawLayer!=null)
        {
            drawLayer.updateDrawParam(drawParam);
        }
        if(onSizeChangeLister!=null)
        {
            onSizeChangeLister.onSizeChanged(w,h);
        }


    }

    @Override
    public void updateView() {
        postInvalidate();
    }

    public void setOnSizeChangeListener(onSizeChangeLister onSizeChangeLister) {

        this.onSizeChangeLister = onSizeChangeLister;
    }


    public interface onSizeChangeLister
    {
        public void onSizeChanged(int width,int height);
    }
}
