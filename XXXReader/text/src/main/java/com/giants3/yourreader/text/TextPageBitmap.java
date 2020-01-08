package com.giants3.yourreader.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.util.Pools;
import android.view.MotionEvent;

import com.giants3.android.frame.util.Log;
import com.giants3.yourreader.text.elements.WordElement;
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
    DrawTask drawTask;

    public TextPageBitmap(Context context,int screenWidth, int screenHeight, IDrawable iDrawable) {
        super(screenWidth, screenHeight, iDrawable);
        bitmap=Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void drawPage(TextPageInfo pageInfo, DrawParam drawParam) {


        Canvas canvas =new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();paint.setTextSize(SettingContent.getInstance().getTextSize());
//        canvas.drawText("xxxxxxxxxx",500,500, paint);
//        Log.e("draw Pageinfo");
//
        if(pageInfo==null||pageInfo.pageParas==null) return;
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

        if(drawTask!=null)
        {
            try {
                drawTask.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        drawTask= new DrawTask(pageInfo,canvas,paint,iDrawable);
        drawTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




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


    private  class DrawTask extends AsyncTask
    {

        public TextPageInfo textPageInfo;

        Canvas canvas;
        Paint paint;
        IDrawable iDrawable;

        public DrawTask(TextPageInfo textPageInfo,Canvas canvas,Paint paint,IDrawable iDrawable)
        {
            this.textPageInfo = textPageInfo;
            this.canvas = canvas;
            this.paint = paint;
            this.iDrawable = iDrawable;
        }
        @Override
        protected Object doInBackground(Object[] objects) {



            textPageInfo.elements=new ArrayList<>();


            float textSize = SettingContent.getInstance().getTextSize();
            float y= textSize;
             for (PagePara pagePara:textPageInfo.pageParas)
             {

                 float[] xPositions = pagePara.paraTypeset.xPositions;
                 int[] lineHead=pagePara.paraTypeset.lineHead;


                 int startLine=pagePara.firstLine==-1?0:pagePara.firstLine;
                 int lastline=pagePara.lastLine==-1?pagePara.paraTypeset.lineCount-1:pagePara.lastLine-1;
                 for (int i = startLine; i <=lastline ; i++) {

                    int index=lineHead[i]; int lastIndex=(i==pagePara.paraTypeset.lineCount-1)?(xPositions.length-1):lineHead[i+1]-1 ;
                     for (int j = index; j <=lastIndex ; j++) {


                         WordElement wordElement= new WordElement();
                         wordElement.word=pagePara.paraTypeset.paragraghData.getContent().substring(j,j+1);
                         wordElement.x= (int) xPositions[j];
                         wordElement.y= (int) y;

                         textPageInfo.elements.add(wordElement);
                         wordElement.draw(canvas,paint);
                         if (isCancelled())return null;


                     }

                     iDrawable.updateView();
                     try {
                         Thread.sleep(1);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     y+=textSize;

                     if(i!=lastline)
                     {
                         y+=SettingContent.getInstance().getLineSpace();
                     }


                 }


                 y+=SettingContent.getInstance().getParaSpace();


             }
            return null;





        }
    }
}
