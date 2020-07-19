package com.giants3.android;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;


public class Utils {


    /*
     *  使某个  actionId 动作 静默一段时间
     */
    public final static void disableAction(int actionId, int disableTimeInMillisecond) {
        ActionRepeatController.disableAction(actionId, disableTimeInMillisecond);
    }


    /*
     *  判断 同一个 actionId 动作 能否运行执行
     *  repeatDisableInMillisecond 一个动作操作后 在规定时间内不允许再执行
     *  return true 动作在规定时间内没有重复，可以执行
     *  return false 动作在规定时间内有重复，不能执行
     */
    public final static boolean isActionEnable(int actionId, int repeatDisableInMillisecond) {
        return ActionRepeatController.isActionEnable(actionId, repeatDisableInMillisecond);
    }

    /*
     *  判断 同一个 actionId 动作 能否运行执行
     *  return true 动作在1S内没有重复，可以执行
     *  return false 动作在1S内有重复，不能执行
     */
    public final static boolean isActionEnableInOneSecond(int actionId) {
        return ActionRepeatController.isActionEnableInOneSecond(actionId);
    }




    //webview 5.0+问题适配
    public static Context getFixedContext(Context context) {
        // Android Lollipop 5.0 & 5.1
        try {
            if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23)
                return context.createConfigurationContext(new Configuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }
}
