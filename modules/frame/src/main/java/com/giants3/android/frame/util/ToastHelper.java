package com.giants3.android.frame.util;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;

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

        Toast toast = Toast.makeText(mContext, message
                , Toast.LENGTH_LONG);
        showToast(toast);
    }

    public static final void showShort(String message)
    {

        Toast toast = Toast.makeText(mContext, message
                , Toast.LENGTH_SHORT);

        showToast(toast);
    }


    public static final   void  show(@StringRes int resId)
    {

        Toast toast = Toast.makeText(mContext, resId
                , Toast.LENGTH_SHORT);

        showToast( toast);
    }

    private static final void showToast(Toast toast)
    {


        if (toast != null) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                //7.0系统  toastshow 后 ui线程超时2秒以上就会崩溃。这个做个代理 ， 拦截崩溃。
                hookToast(toast);
            }

            toast.show();
        }


    }


    //hoke 处理， 拦截系统的崩溃
    public static void hookToast(Toast toast) {
        Class<Toast> cToast = Toast.class;
        try {
            //TN是private的
            Field fTn = cToast.getDeclaredField("mTN");
            fTn.setAccessible(true);

            //获取tn对象
            Object oTn = fTn.get(toast);
            //获取TN的class，也可以直接通过Field.getType()获取。
            Class<?> cTn = oTn.getClass();
            Field fHandle = cTn.getDeclaredField("mHandler");

            //重新set->mHandler
            fHandle.setAccessible(true);
            fHandle.set(oTn, new HandlerProxy((Handler) fHandle.get(oTn)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static class HandlerProxy extends Handler {

        private Handler mHandler;

        public HandlerProxy(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                mHandler.handleMessage(msg);
            } catch (WindowManager.BadTokenException e) {
                //ignore
            }
        }
    }
}
