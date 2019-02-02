package com.giants3.android.immersive;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 系统判定 是否支持状态栏改变字体颜色的系统。
 *
 *
 *
 */
public class RomUtils {

    class AvailableRomType {
        public static final int MIUI = 1;
        public static final int FLYME = 2;
        public static final int ANDROID_NATIVE = 3;
        public static final int NA = 4;
    }

    public static boolean isLightStatusBarAvailable () {

         if(isStatusBarSpecialMobile()) return false;
        if (isMIUIV6OrAbove() || isFlymeV4OrAbove() || isAndroidMOrAbove()) {
            return true;
        }
        return false;
    }

    private static boolean isStatusBarSpecialMobile() {
        String manufacturer = Build.MANUFACTURER;

        if (!TextUtils.isEmpty(manufacturer) && manufacturer.equalsIgnoreCase("LeMobile")) {


            if(Build.DISPLAY.startsWith("WAXCNFN58"))
                return true;
        }
        if (!TextUtils.isEmpty(manufacturer)){
            if (manufacturer.equalsIgnoreCase("HUAWEI") && Build.VERSION.SDK_INT >=24) {
                if(Build.DISPLAY.startsWith("MHA")) // mate9
                    return true;
                if(Build.DISPLAY.startsWith("WAS"))  //   荣耀v8
                    return true;
                if(Build.DISPLAY.startsWith("VKY")) //   p10
                    return true;
            }
        }
        return false;
    }

    public static int getLightStatusBarAvailableRomType() {
        if (isMIUIV6OrAbove()) {
            return AvailableRomType.MIUI;
        }

        if (isFlymeV4OrAbove()) {
            return AvailableRomType.FLYME;
        }

        if (isAndroidMOrAbove()) {
            return AvailableRomType.ANDROID_NATIVE;
        }

        return AvailableRomType.NA;
    }

    //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
    //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
    public static boolean isFlymeV4OrAbove() {
        String displayId = Build.DISPLAY;

          if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断是否是特定型号的乐视品牌手机
     * 目前 eui 5.8 的透明状态栏效果异常。  LEX720 等
     *
     * @return
     */
    public static boolean isSpecialLeMobile() {
        String manufacturer = Build.MANUFACTURER;


        if (!TextUtils.isEmpty(manufacturer) && manufacturer.equalsIgnoreCase("LeMobile")) {


            if(Build.DISPLAY.startsWith("WAXCNFN58"))
               return true;
        }
        return false;
    }

    //MIUI V6对应的versionCode是4
    //MIUI V7对应的versionCode是5
    public static boolean isMIUIV6OrAbove() {
        String miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code");
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                int miuiVersionCode = Integer.parseInt(miuiVersionCodeStr);
                if (miuiVersionCode >= 4) {
                    return true;
                }
            } catch (Exception e) {}
        }
        return false;
    }
    /**
     * M comes after L.
     * Build.VERSION_CODES.M
     */
    private static final int M = 23;
    //Android Api 23以上
    public static boolean isAndroidMOrAbove() {
        if (Build.VERSION.SDK_INT >= M) {
            return true;
        }
        return false;
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
            try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }
}