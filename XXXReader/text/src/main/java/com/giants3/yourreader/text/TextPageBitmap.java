package com.giants3.yourreader.text;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.core.util.Pools;
import android.view.MotionEvent;

import com.giants3.android.frame.util.BitmapHelper;
import com.giants3.android.kit.ResourceExtractor;
import com.giants3.yourreader.text.elements.WordElement;
import com.xxx.reader.BackgroundManager;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageBitmap;
import com.xxx.reader.turnner.sim.SettingContent;

import  android.content.Context;

import java.util.ArrayList;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageBitmap extends PageBitmap<TextPageInfo,DrawParam> {


    Bitmap bitmap;
    BitmapShader bitmapShader;
    DrawTask drawTask;
    Canvas canvas;
    Paint paint;
    Paint aPaint;
    BackgroundDrawer backgroundDrawer;
    public TextPageBitmap(Context context,int screenWidth, int screenHeight, IDrawable iDrawable,Paint paint) {
        super(screenWidth, screenHeight, iDrawable);
        bitmap=Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
        bitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        canvas =new Canvas(bitmap);
        this.paint = paint;


        aPaint = new Paint();
        aPaint.setShader(bitmapShader);
        backgroundDrawer=new BackgroundDrawer();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void drawPage(TextPageInfo pageInfo, DrawParam drawParam) {



            //canvas.drawColor(Color.WHITE);

//        canvas.drawText("xxxxxxxxxx",500,500, paint);
//        Log.e("draw Pageinfo");
//
            if (pageInfo == null || pageInfo.pageParas == null) return;
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
            drawTask = new DrawTask(pageInfo, canvas, backgroundDrawer,paint, iDrawable);
            drawTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




    }

    private void cancelDrawTask() {
        if(drawTask!=null)
        {
            try {
                drawTask.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(bitmap,0,0,null);

    }

    @Override
    public void draw(Canvas canvas,Paint mPaint) {
        super.draw(canvas,mPaint);

        canvas.drawBitmap(bitmap,0,0,mPaint);

    }

    @Override
    public void draw(Canvas canvas,   Path path) {

        if(path!=null)
        {
            canvas.drawPath(path,aPaint);
        }else
        {
            canvas.drawBitmap(bitmap,0,0,null);
        }



    }

    private  static class DrawTask extends AsyncTask
    {

        public TextPageInfo textPageInfo;
        TextPageDrawHelper drawHelper;
        Canvas canvas;
        private BackgroundDrawer backgroundDrawer;
        Paint paint;
        IDrawable iDrawable;

        public DrawTask(TextPageInfo textPageInfo,Canvas canvas,BackgroundDrawer backgroundDrawer,Paint paint,IDrawable iDrawable)
        {
            this.textPageInfo = textPageInfo;
            this.canvas = canvas;
            this.backgroundDrawer = backgroundDrawer;
            this.paint = paint;
            this.iDrawable = iDrawable;

            drawHelper=new TextPageDrawHelper();
        }
        @Override
        protected Object doInBackground(Object[] objects) {



            if(textPageInfo.isReady) {


                BackgroundManager.getInstance().draw(canvas);

                backgroundDrawer.draw(canvas);
                paint.setTextSize(SettingContent.getInstance().getTextSize());
                paint.setColor(TextSchemeContent.getTextColor());

                drawHelper.draw(canvas, textPageInfo);
                synchronized (textPageInfo.elements) {


                    int i = 0;
                    for (WordElement wordElement : textPageInfo.elements) {
                        wordElement.draw(canvas, paint);
                        i++;
                        if (isCancelled()) return null;
                        if (i % 10 == 0) {
                            iDrawable.updateView();

//                    try {
//                        Thread.sleep(3);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                        }
                        if (isCancelled()) return null;

                    }

                }

                iDrawable.updateView();

//            float textSize = SettingContent.getInstance().getTextSize();
//            float y= textSize;
//             for (PagePara pagePara:textPageInfo.pageParas)
//             {
//
//                 float[] xPositions = pagePara.paraTypeset.xPositions;
//                 int[] lineHead=pagePara.paraTypeset.lineHead;
//
//
//                 int startLine=pagePara.firstLine==-1?0:pagePara.firstLine;
//                 int lastline=pagePara.lastLine==-1?pagePara.paraTypeset.lineCount-1:pagePara.lastLine-1;
//                 for (int i = startLine; i <=lastline ; i++) {
//
//                    int index=lineHead[i]; int lastIndex=(i==pagePara.paraTypeset.lineCount-1)?(xPositions.length-1):lineHead[i+1]-1 ;
//                     for (int j = index; j <=lastIndex ; j++) {
//
//
//                         WordElement wordElement= new WordElement();
//                         wordElement.word=pagePara.paraTypeset.paragraghData.getContent().substring(j,j+1);
//                         wordElement.x= (int) xPositions[j];
//                         wordElement.y= (int) y;
//
//                         textPageInfo.elements.add(wordElement);
//                         wordElement.draw(canvas,paint);
//
//
//                         iDrawable.updateView();
//                         try {
//                             Thread.sleep(1);
//                         } catch (InterruptedException e) {
//                             e.printStackTrace();
//                         }
//                         if (isCancelled())return null;
//
//
//
//                     }
//
//
//                     y+=textSize;
//
//                     if(i!=lastline)
//                     {
//                         y+=SettingContent.getInstance().getLineSpace();
//                     }
//
//
//                 }
//
//
//                 y+=SettingContent.getInstance().getParaSpace();
//
//
//             }
            }
            return null;





        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelDrawTask();
        canvas.setBitmap(null);
        BitmapHelper.recycleBitmap(bitmap);
        bitmap=null;
        canvas=null;

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
