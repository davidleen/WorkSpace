package com.giants3.android.frame.util;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by david on 2016/1/2.
 */
public class ToastHelper {

    public static Context mContext;

    public static void  init(Context context)
    {
        mContext=context;
    }


    public static final void show(String message)
    {

        Toast.makeText(mContext,message
        ,Toast.LENGTH_LONG).show();
    }

    public static final void showShort(String message)
    {

        Toast.makeText(mContext,message
                ,Toast.LENGTH_SHORT).show();
    }


    public static final   void  show(@StringRes int resId)
    {

        Toast.makeText(mContext,resId
                ,Toast.LENGTH_SHORT).show();
    }
}
