package com.rnmap_wb.android.api;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.Context;

public class ManifestUtils {

    public static String getAppMetaData(Context context,String key)
    {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(appInfo==null) return "";
        return  appInfo.metaData.getString(key);
    }
}
