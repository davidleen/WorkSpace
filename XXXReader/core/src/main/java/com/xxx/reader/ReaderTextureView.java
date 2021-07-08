package com.xxx.reader;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;


import com.giants3.android.frame.util.Log;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.prepare.DrawLayer;

/**
 * Created by davidleen29 on 2018/3/21.
 */

public class ReaderTextureView extends TextureView implements IDrawable, TextureView.SurfaceTextureListener {
    public ReaderTextureView(Context context) {
        this(context,null);
    }

    public ReaderTextureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReaderTextureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSurfaceTextureListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReaderTextureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setDrawLayer(DrawLayer drawLayer) {
        this.drawLayer = drawLayer;
    }

    DrawLayer drawLayer;



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(drawLayer!=null)
        {
            if(drawLayer.onTouchEvent(event)) return true;
        }

        return false;
    }







    @Override
    public void updateView() {
        getSurfaceTexture().updateTexImage();
    }


    @Override
    public boolean onTrackballEvent(MotionEvent event) {


        Log.e(event);
        return false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        updateSize(width, height);
    }

    private void updateSize(int width, int height) {
        DrawParam drawParam=new DrawParam();
        drawParam.width=width;
        drawParam.height=height;

        drawParam.padding=new float[]{30,80,30,80};
        if(drawLayer!=null)
        {
            drawLayer.updateDrawParam(drawParam);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        updateSize(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {





    }
}
