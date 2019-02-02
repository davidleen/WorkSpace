package com.giants3.android.immersive;

import android.view.Window;
import android.view.WindowManager;

import com.giants3.android.frame.util.Log;

import java.lang.reflect.Field;

/**
 * 魅族机型适配状态栏
 * <p>
 * Created by HP on 2016/10/8.
 */


public class MeizuBarCompatible extends TranslucentStatusBarCompatible {

    /**
     * 设置状态栏字体 图案颜色方案 ，就目前系统只支持 light 与 not light
     *
     * @param window
     * @param textMode
     */
    @Override
    public void setTextMode(Window window, int textMode) {


        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                int newValue = value;
                if (textMode == BAR_MODE_DARK) {
                    newValue |= bit;
                } else {
                    newValue &= ~bit;
                }
                if (newValue != value) {
                    meizuFlags.setInt(lp, newValue);
                    window.setAttributes(lp);
                }

            } catch (Exception e) {
                Log.e("MeiZu  setStatusBarDarkIcon: failed");
            }
        }


    }
}
