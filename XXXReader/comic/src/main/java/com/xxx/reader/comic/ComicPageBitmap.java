package com.xxx.reader.comic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageBitmap;
import com.xxx.reader.download.DownloadListener;


/**
 * 漫画分页绘制处理。
 * <p>
 * <p>
 * <p>
 * <p>
 * <P> </>增加漫画图片缓存处理。
 * <p>
 * <p>
 * Created by davidleen29 on 2017/10/13.
 */

public class ComicPageBitmap extends PageBitmap<ComicPageInfo, DrawParam> {



    ComicBitmapDrawer comicBitmapDrawer;



    public ComicPageBitmap(Context context, int screenWidth, int screenHeight ,IDrawable iDrawable, DownloadListener downloadListener) {
        super(screenWidth, screenHeight,iDrawable);


        comicBitmapDrawer = new ComicBitmapDrawer(context, this, iDrawable, downloadListener);


    }


    @Override
    public void drawPage(ComicPageInfo pageInfo, DrawParam drawParam) {
        comicBitmapDrawer.setBitmapFrame(pageInfo == null ? null : pageInfo.bitmapFrames == null ? null : pageInfo.bitmapFrames.size() < 1 ? null : pageInfo.bitmapFrames.get(0));



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return comicBitmapDrawer.onTouchEvent(event);
    }


    @Override
    public void draw(Canvas canvas) {
        comicBitmapDrawer.setBitmapFrame(pageInfo == null ? null : pageInfo.bitmapFrames == null ? null : pageInfo.bitmapFrames.size() < 1 ? null : pageInfo.bitmapFrames.get(0));
        comicBitmapDrawer.draw(canvas,drawParam.width,drawParam.height);
    }
}
