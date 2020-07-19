package com.giants3.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * manifest文件处理帮助类。
 */
public class ManifestUtils {

    private static final String DIGITAL_PREFIX = "prefix_";

    /**
     * 获取Manifest Meta Data
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaData(Context context, String metaKey) {
        String name = context.getPackageName();
        ApplicationInfo appInfo;
        String msg = "";
        try {
            appInfo = context.getPackageManager().getApplicationInfo(name,
                    PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString(metaKey);
            if(msg!=null&&msg.startsWith(DIGITAL_PREFIX))//特殊字符适配， meta-data 不支持数字， 在数字前加上xxx表示。
            {
                msg=msg.substring(DIGITAL_PREFIX.length());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (msg==null) {
            return "";
        }

        return msg;
    }

    /**
     * 获得渠道号
     *
     * @param context
     * @param channelKey
     * @return
     */
    public static String getChannelNo(Context context, String channelKey) {
        return getMetaData(context, channelKey);
    }

    /**
     * 获得apk版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "";
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (version == null) {
            return "";
        }

        return version;
    }

    /**
     * 获得apk版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }
}
