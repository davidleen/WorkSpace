package com.xxx.reader.prepare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.xxx.reader.BackgroundManager;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.text.layout.BitmapHolder;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.turnner.sim.SettingContent;


/**
 * Created by davidleen29 on 2017/8/25.
 */

public class DrawLayer {


    IZoomHandler zoomHandler;
    private BitmapProvider drawCache;
    private IDrawable iDrawable;

    private DrawParam drawParam;

    private MenuClickListener clickListener;

    public DrawLayer(Context context, BitmapProvider drawCache, IDrawable iDrawable) {


        this.drawCache = drawCache;
        this.iDrawable = iDrawable;
        zoomHandler = new ZoomHandler(context, iDrawable) {
        };
        zoomHandler.setSimpleOnGestureListener(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {


                if (isInMenuArea((int) e.getX(), (int) e.getY())) {
                    clickListener.onMenuAreaClick();
                }

                return super.onSingleTapConfirmed(e);
            }


        });


    }


    private boolean isInMenuArea(int x, int y) {
        return x < drawParam.width * 2 / 3 && x > drawParam.width / 3
                &&
                y < drawParam.height * 2 / 3 && y > drawParam.height / 3;


    }

    public void setPageTurner(IPageTurner pageTurner) {
        this.pageTurner = pageTurner;
        if (drawParam != null)
            setDrawParamToPageTurner(drawParam);
    }


    IPageTurner pageTurner;


    public boolean onTouchEvent(MotionEvent event) {


        if (zoomHandler != null && zoomHandler.onTouchEvent(event)) return true;
        if (pageTurner != null) {

//            event.offsetLocation();


            return pageTurner.onTouchEvent(event);
        }
        return false;

    }

    public void onDraw(Canvas canvas) {

 
        BackgroundManager.getInstance().drawBackgroundNeeded(canvas);

        canvas.save();
        canvas.clipRect(0, 0, drawParam.width, drawParam.height);

        if (SettingContent.getInstance().isBookSideEffect()) {
            Rect bookSidePadding = SettingContent.getInstance().getBookSidePadding();
            canvas.translate(bookSidePadding.left, 0);
        }
//        if (zoomHandler != null)
//            zoomHandler.zoom(canvas);

        if (pageTurner != null && pageTurner.isInAnimation()) {


            boolean test = false;
            if (test) {

                Bitmap bitmap = Bitmap.createBitmap(drawParam.width, drawParam.height, Bitmap.Config.ARGB_4444);
                Canvas tempCanvas = new Canvas(bitmap);
                pageTurner.onDraw(tempCanvas);
                Paint paint = new Paint();
                paint.setTextSize(60);
                paint.setColor(Color.RED);
                tempCanvas.drawText("TESTING!!!!",100,100, paint);
                canvas.drawBitmap(bitmap, 0, 0, null);
            } else {
                pageTurner.onDraw(canvas);
            }



        } else {

            BitmapHolder currentBitmap = drawCache.getCurrentBitmap();
            currentBitmap.draw(canvas);


        }

        if (SettingContent.getInstance().isBookSideEffect()) {
            Rect bookSidePadding = SettingContent.getInstance().getBookSidePadding();
            canvas.translate(-bookSidePadding.left, 0);
        }
        canvas.restore();
    }


    public void updateDrawParam(DrawParam drawParam) {
        this.drawParam = drawParam;


        setDrawParamToPageTurner(drawParam);
        if (zoomHandler != null)
            zoomHandler.setSize(drawParam.width, drawParam.height);
    }


    public void setDrawParamToPageTurner(DrawParam drawParam) {

//        if (pageTurner != null) {
//            if (SettingContent.getInstance().isBookSideEffect()) {
//
//                DrawParam newDrawParam = new DrawParam(drawParam);
//                Rect bookSidePadding = SettingContent.getInstance().getBookSidePadding();
//                newDrawParam.width -= (bookSidePadding.left + bookSidePadding.right);
//                pageTurner.updateDrawParam(newDrawParam);
//
//            } else {
//                pageTurner.updateDrawParam(drawParam);
//            }
//        }


    }

    public void setClickListener(MenuClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface MenuClickListener {
        void onMenuAreaClick();
    }

}
