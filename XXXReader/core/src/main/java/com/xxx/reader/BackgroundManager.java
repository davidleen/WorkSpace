package com.xxx.reader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.giants3.android.frame.util.Utils;
import com.xxx.reader.TextScheme;
import com.xxx.reader.TextSchemeContent;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackgroundManager {


    private Bitmap background;

    AtomicBoolean ready=new AtomicBoolean(false);


    private static BackgroundManager instance =new BackgroundManager();


    public static BackgroundManager getInstance()
    {
        return instance;
    }



    public void register(Activity activity)
    {



    }


    public void unRegister(Activity activity)
    {}


    public  void draw(Canvas canvas)
    {

        if(ready.get())
        {


            canvas.drawBitmap(background,0,0,null);




        }



    }

    private int lastWidth;int lastHeight;
    public void update(int width, int height)
    {

        if(background==null)
        {
             int[] wh= Utils.getScreenWH();
            background=Bitmap.createBitmap(wh[0],wh[1],Bitmap.Config.RGB_565);
        }

        if(lastHeight==height&&lastWidth==width)
        {
            return;
        }

        lastHeight=height;
        lastWidth=width;

        reset();










    }

    public void reset() {




        ready.set(false);

        Canvas canvas=new Canvas(background);
        if(TextSchemeContent.getBackgroundType()== TextScheme.BACKGROUND_TYPE_IMAGE)
        {

            Bitmap bitmap = BitmapFactory.decodeFile(TextSchemeContent.getBackGroundImagePath());


            boolean stretch=TextSchemeContent.getBackgroundTileMode().isEmpty()||TextSchemeContent.getBackgroundTileMode().equals("stretch");
            if (stretch) {
                //不规则图片 带投影， 只能拉伸铺满
                canvas.drawBitmap(bitmap,null,new Rect(0,0,lastHeight,lastHeight),null);
            } else {
                //这里做底图平铺处理。
                int bgWidth = bitmap.getWidth();
                int bgHeight = bitmap.getHeight();
                int width=lastWidth;int height=lastHeight;
                int w = width % bgWidth == 0 ? width / bgWidth : width / bgWidth + 1;
                int h = height % bgHeight == 0 ? height / bgHeight : height / bgHeight + 1;
                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        float left = (float) j * bgWidth;
                        float top = (float)i * bgHeight;
                        canvas.drawBitmap(bitmap, left, top, null);
                    }
                }
            }


            bitmap.recycle();


        }else
        {
            canvas.drawColor(TextSchemeContent.getBackGroundColor());
        }



        ready.set(true);









    }
}
