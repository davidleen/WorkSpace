package com.rnmap_wb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.giants3.android.frame.util.Log;

/**
 * 手机相关功能的处理。统一处理 异常拦截   。
 * <p>
 * 权限申请
 * <p>
 * Created by davidleen29 on 2017/7/11.
 */

public class PhoneStateHandler {


    public static String getSubscriberId(Context context) {

        if (!hasPermision(context)) return "";
        try {
            TelephonyManager tm = getManager(context);
            return tm.getSubscriberId();
        } catch (Throwable t) {
            Log.e(t);
        }
        return "";
    }


    private static TelephonyManager getManager(Context context) {


        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        return tm;
    }

    public static String getDeviceId(Context context) {
        if (!hasPermision(context)) return "";
        try {
            TelephonyManager tm = getManager(context);
            return tm.getSubscriberId();
        } catch (Throwable t) {
            Log.e(t);
        }
        return "";
    }


    /**
     * 统一处理
     *
     * @param context
     * @param listener
     * @param events
     */
    public static void listener(Context context, PhoneStateListener listener, int events) {

        if (!hasPermision(context)) return;
        ;
        TelephonyManager tm = getManager(context);


        try {
            tm.listen(listener, events);
        } catch (Throwable t) {
            Log.e(t);
        }


    }


    /**
     * 权限判断
     *
     * @param context
     * @return
     */

    public static boolean hasPermision(Context context) {

        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()));
        return permission;
    }

    public static String getSimOperator(Context context) {
//        if(!hasPermision(context)) return "";

        try {
            TelephonyManager tm = getManager(context);
            return tm.getSimOperator();
        } catch (Throwable t) {
            Log.e(t);
        }
        return "";
    }

    public static String getLine1Number(Context ctx) {
        if (!hasPermision(ctx)) return "";
        try {
            TelephonyManager tm = getManager(ctx);
            return tm.getLine1Number();
        } catch (Throwable t) {
            Log.e(t);
        }
        return "";
    }

    public static String getSimSerialNumber(Context ctx) {
        if (!hasPermision(ctx)) return "";
        try {
            TelephonyManager tm = getManager(ctx);
            return tm.getSimSerialNumber();
        } catch (Throwable t) {
            Log.e(t);
        }
        return "";
    }

    public static String getImei(Context context) {

        return getDeviceId(context);

    }

    public static String getPhoneNumber(Context context) {
        if (!hasPermision(context)) return "";
        try {
            TelephonyManager tm = getManager(context);
            return tm.getLine1Number();
        } catch (Throwable t) {
            Log.e(t);
        }
        return "";
    }

    /**
     * 获取电话号码， 去除区号
     *
     * @param context
     * @return
     */
    public static String getShortPhoneNumber(Context context) {

        String phonenum = getPhoneNumber(context);
        if (phonenum == null) phonenum = "";
        if (phonenum.length() > 11) {
            //联通即国内号码， 简单取后11位置
            phonenum = phonenum.substring(phonenum.length() - 11, phonenum.length());
        }
        return phonenum;
    }




}
