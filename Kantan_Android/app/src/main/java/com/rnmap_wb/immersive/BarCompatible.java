package com.rnmap_wb.immersive;

import android.content.Context;
import android.view.Window;

/**
 *  状态栏颜色背景控制接口
 *
 * Created by davidleen29 on 2016/10/8.
 */

public interface BarCompatible {


    public void setBackgroundColor(Window window, int color);

    /**
     * 设置状态栏字体 图案颜色方案 ，就目前系统只支持 light 与 not light 原生系统》23 或者 小米 魅族的 指定rom 才支持这个方法
     */
    void setTextMode(Window window,int textMode);


    public static final int BAR_MODE_LIGHT = 1;

    public static final int BAR_MODE_DARK = 2;

}
