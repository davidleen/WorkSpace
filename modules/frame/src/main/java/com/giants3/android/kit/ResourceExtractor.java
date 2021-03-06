package com.giants3.android.kit;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.util.SparseArray;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ResourceExtractor {

    public static Context context;
    public static String getString(int resId){
        return context.getString(resId);
    }

    public static String getString(int resId, Object... formatArgs){
        return context.getString(resId,formatArgs);
    }

    public static String[] getStringArray(int resId){
        return context.getResources().getStringArray(resId);
    }

    public static boolean getBoolean(int resId){
        return context.getResources().getBoolean(resId);
    }

    public static Integer getInteger(int integerId){
        return context.getResources().getInteger(integerId);
    }

    public static  float getDimension(int dimId)
    {
        return context.getResources().getDimension(dimId);
    }

    public static float getDimen(int dimId) {
        return getDimension(dimId);
    }



    public static  Drawable getFilteredDrawable(int color , int drawableId)
    {

        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return drawable.mutate();
    }

    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor( context,colorId);
    }


    public static ColorStateList getColorStateList(@ColorRes int colorId) {
        return ContextCompat.getColorStateList( context,colorId);
    }

    /**
     * 读取数组形式配置的资源id
     * @param res
     * @return
     */
    public static   int[] getArrayRes(@ArrayRes int res) {
        TypedArray ar = context.getResources().obtainTypedArray(res);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        return resIds;
    }


   static SparseArray<Drawable> drawableSparseArray=new SparseArray<>();

    public static Drawable getDrawable(@DrawableRes int drawableRes) {

        Drawable drawable = drawableSparseArray.get(drawableRes);
        if(drawable==null) {
            drawable = context.getResources().getDrawable(drawableRes);
            drawableSparseArray.put(drawableRes,drawable);
        }
        return drawable;

    }


}
