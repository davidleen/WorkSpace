package com.giants3.android.frame.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by davidleen29 on 2018/9/11.
 */

public class BitmapHelper {


    private final static int QUALITY = 75; // 图片压缩初始质量
    private final static Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.PNG; // 图片压缩格式

    /**
     * 将一个view 转换成bitmap
     * Created by davidleen29 on 2017/1/6.
     */

    public static Bitmap convert(View v) {

        int[] wh = Utils.getScreenWH();
        return convert(v, wh[0], wh[1]);


    }


    /**
     * 将一个view 转换成bitmap
     * Created by davidleen29 on 2017/1/6.
     */

    @SuppressLint("NewApi")
    public static Bitmap convert(View v, int destWidth, int destHeight) {


        Bitmap bitmap = null;
        boolean useCache = false;
        if (useCache) {
            if (v.getDrawingCache() == null) {
                boolean enabled = v.isDrawingCacheEnabled();
                v.setDrawingCacheEnabled(true);
                v.buildDrawingCache();
                v.setDrawingCacheEnabled(enabled);
            }
            bitmap = v.getDrawingCache();

        }

        if (bitmap == null) {
            //图片生成   3个阶段  measure layout draw
            v.measure(View.MeasureSpec.makeMeasureSpec(destWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(destHeight, View.MeasureSpec.UNSPECIFIED));
            int measuredHeight = v.getMeasuredHeight();
            int measuredWidth = v.getMeasuredWidth();
//            Log.e("wrapHeight:"+wrapHeight);
//            Log.e("wrapWidth:"+v.getMeasuredWidth());
            v.layout(0, 0, measuredWidth, measuredHeight);

            bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
        }


        return bitmap;

    }
    public static boolean bitmapSaveToFile(Bitmap bitmap, int quality, String savePath,
                                           boolean isReCreated )
    {
        return bitmapSaveToFile(bitmap,quality,savePath,isReCreated, Bitmap.CompressFormat.PNG);
    }

    public static boolean bitmapSaveToFile(Bitmap bitmap, int quality, String savePath,
                                           boolean isReCreated, Bitmap.CompressFormat format) {

        File file = null;

        file = new File(savePath);

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (file.exists()) {
            if (isReCreated)
                file.delete();
            else
                return true;
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(format, quality, out);
            out.flush();
        }  catch (Throwable e) {
            e.printStackTrace();
        } finally {
            FileUtils.safeClose(out);

        }

        return true;
    }

    public static boolean bitmapSaveToFile(Bitmap bitmap, String savePath, boolean isReCreated) {
        return bitmapSaveToFile(bitmap, QUALITY, savePath, isReCreated);
    }

    public static void recycleBitmap(Bitmap bmp) {


        if (bmp != null && !bmp.isRecycled()) {
            try {
                bmp.recycle();
            } catch (Throwable e) {

                e.printStackTrace();
            }
        }
    }

    public static Bitmap fromFile(String bitmapFilePath) {
        return fromFile(bitmapFilePath, null);
    }

    public static Bitmap fromFile(String bitmapFilePath, Bitmap bitmap) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inBitmap = bitmap;

        return BitmapFactory.decodeFile(bitmapFilePath, options);
    }


    public static Bitmap fromFile(String bitmapFilePath, int sampleSize) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(bitmapFilePath, options);
    }

    public static int[] readSie(String tempFile) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(tempFile, options);
        return new int[]{options.outWidth, options.outHeight};


    }

    public static void rotateBitmapFile(String filePath, int degree, float sampleSize) {


        Bitmap bitmap = fromFile(filePath, (int) sampleSize);

        Matrix matrix = new Matrix();
        if (degree != 0)
            matrix.setRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        bitmapSaveToFile(bmp,QUALITY, filePath, true,Bitmap.CompressFormat.JPEG);
        recycleBitmap(bitmap);
        recycleBitmap(bmp);


    }
}
