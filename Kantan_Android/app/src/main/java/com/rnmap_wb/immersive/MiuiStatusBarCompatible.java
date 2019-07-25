package com.rnmap_wb.immersive;


import android.os.Build;
import android.view.View;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

/**
 * 小米 机型的 状态栏适配
 * <p>
 * 触发条件  sdk《21 》==19
 * <p>
 * <p>
 * Created by davidleen29 on 2016/10/8.
 * 2016/10/8
 */


public class MiuiStatusBarCompatible extends TranslucentStatusBarCompatible {


    /**
     * 设置状态栏字体 图案颜色方案 ，就目前系统只支持 light 与 not light
     *
     * @param window
     * @param textMode
     */
    @Override
    public void setTextMode(Window window, int textMode) {


        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, textMode == BAR_MODE_DARK ? darkModeFlag : 0, darkModeFlag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
            if (BAR_MODE_DARK == textMode) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }
}
