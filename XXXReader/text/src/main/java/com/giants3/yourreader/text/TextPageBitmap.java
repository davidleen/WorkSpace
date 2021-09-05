package com.giants3.yourreader.text;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.view.MotionEvent;

import com.giants3.android.frame.util.BitmapHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.pools.PoolCenter;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.text.page.PageDrawer;
import com.xxx.reader.BackgroundManager;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageBitmap;
import com.xxx.reader.text.page.TextPageInfo2;
import com.xxx.reader.turnner.sim.SettingContent;

import android.content.Context;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageBitmap extends PageBitmap<TextPageInfo2, DrawParam> {


    Bitmap bitmap;
    BitmapShader bitmapShader;
    DrawTask drawTask;

    Paint paint;
    BackgroundDrawer backgroundDrawer;

    public TextPageBitmap(Context context, int screenWidth, int screenHeight, IDrawable iDrawable, Paint paint) {
        super(screenWidth, screenHeight, iDrawable);
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        this.paint = paint;


        backgroundDrawer = new BackgroundDrawer();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void drawPage(TextPageInfo2 pageInfo, DrawParam drawParam) {


        //canvas.drawColor(Color.WHITE);

//        canvas.drawText("xxxxxxxxxx",500,500, paint);
//        Log.e("draw Pageinfo");
//
        if (pageInfo == null || pageInfo.pageData == null) return;
//        float y=0;
//        for(PagePara pagePara:pageInfo.pageParas)
//        {
//            canvas.drawText(pagePara.paraTypeset.paragraghData.getContent(),50,y+=60,paint);
//            iDrawable.updateView();
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        cancelDrawTask();
        drawTask = new DrawTask(pageInfo, bitmap, backgroundDrawer, paint, iDrawable);
        drawTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void cancelDrawTask() {
        if (drawTask != null) {
            try {
                drawTask.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            drawTask=null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        synchronized (bitmap) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

    }

    @Override
    public void draw(Canvas canvas, Paint mPaint) {
        super.draw(canvas, mPaint);

        synchronized (bitmap) {
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }

    }

    @Override
    public void draw(Canvas canvas, Path path) {
        synchronized (bitmap) {
            {

                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }


    }

    private static class DrawTask extends AsyncTask {

        public TextPageInfo2 textPageInfo;

        PageDrawer pageDrawer;
        Bitmap bitmap;
        private BackgroundDrawer backgroundDrawer;
        Paint paint;
        IDrawable iDrawable;

        public DrawTask(TextPageInfo2 textPageInfo, Bitmap bitmap, BackgroundDrawer backgroundDrawer, Paint paint, IDrawable iDrawable) {
            this.textPageInfo = textPageInfo;
            this.bitmap = bitmap;
            this.backgroundDrawer = backgroundDrawer;
            this.paint = paint;
            this.iDrawable = iDrawable;

            pageDrawer = new PageDrawer(textPageInfo.pageData);
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            if (textPageInfo.isReady) {
                Bitmap temp = null;
                try {

                    do {


                        temp = PoolCenter.getObjectPool(Bitmap.class).newObject();
                        if (isCancelled()) return null;
                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (temp == null);
                    Canvas canvas = new Canvas(temp);
                    Paint pagePaint = new Paint(paint);
                    BackgroundManager.getInstance().draw(canvas);

                    backgroundDrawer.draw(canvas);
//                    pagePaint.setTextSize(SettingContent.getInstance().getTextSize());
//                    pagePaint.setColor(TextSchemeContent.getTextColor());

                    pageDrawer.onDraw(canvas, pagePaint);
                    if(isCancelled()) return null;
                    synchronized (bitmap) {
                        canvas.setBitmap(bitmap);
                        canvas.drawBitmap(temp, 0, 0, null);
                    }

                    iDrawable.updateView();
                } catch (Throwable t) {

                    Log.e(t);
                } finally {
                    PoolCenter.getObjectPool(Bitmap.class).release(temp);
                }

            }
            return null;


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelDrawTask();

        BitmapHelper.recycleBitmap(bitmap);
        bitmap = null;


    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
