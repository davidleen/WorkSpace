package com.xxx.reader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.giants3.android.frame.util.Utils;
import com.giants3.android.kit.ResourceExtractor;
import com.xxx.reader.TextScheme;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.core.R;
import com.xxx.reader.turnner.sim.SettingContent;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackgroundManager {


    private Bitmap background;

    BitmapShader backgroundShader;
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

    public void drawBackgroundNeeded(Canvas canvas)
    {
        if(ready.get())
        {


            if(SettingContent.getInstance().isBookSideEffect())
            {
                canvas.drawBitmap(background,0,0,null);
            }
        }

    }

                    Rect src=new Rect();
                    Rect dest=new Rect();
    public  void draw(Canvas canvas)
    {

        if(ready.get())
        {



            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if(SettingContent.getInstance().isBookSideEffect())
            {


                Rect bookSidePadding = SettingContent.getInstance().getBookSidePadding();
                src.left= bookSidePadding.left;
                src.top= bookSidePadding.top;
                src.right=lastWidth- bookSidePadding.right ;
                src.bottom=lastHeight- bookSidePadding.bottom ;

                dest.top=0;
                dest.left=0;
                dest.right=src.right-src.left;
                dest.bottom=src.bottom-src.top;

                canvas.drawBitmap(background,src,dest,null);


            }else


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
            backgroundShader=new BitmapShader(background, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }

//        if(lastHeight==height&&lastWidth==width)
//        {
//            return;
//        }

        lastHeight=height;
        lastWidth=width;

        reset();










    }

    public void reset() {




        ready.set(false);

        Canvas canvas=new Canvas(background);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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



        if (SettingContent.getInstance().isBookSideEffect())
        {

            Drawable drawable= ResourceExtractor.getDrawable( R.mipmap.book_rect_left);

            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),canvas.getHeight());
            drawable.draw(canvas);

            Drawable right= ResourceExtractor.getDrawable( R.mipmap.book_rect_right);
            right.setBounds(canvas.getWidth()-right.getIntrinsicWidth(),0,canvas.getWidth(),canvas.getHeight());
            right.draw(canvas);


            Drawable bottom= ResourceExtractor.getDrawable( R.mipmap.book_rect_bottom);
            bottom.setBounds(0,canvas.getHeight()-bottom.getIntrinsicHeight(),canvas.getWidth(),canvas.getHeight());
            bottom.draw(canvas);
        }





        ready.set(true);









    }



}
