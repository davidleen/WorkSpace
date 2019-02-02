package com.giants3.android.immersive;

import android.view.Window;
import android.view.WindowManager;

/**
 * 半透明状态栏适配   适用 sdk 19-23 之间的rom 。
 * Created by davidleen29 on 2016/10/8.
 */


public abstract  class TranslucentStatusBarCompatible  implements  BarCompatible{


    @Override
    public void setBackgroundColor(Window window, int color) {

        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            if (on) {
        winParams.flags |= bits;
//            } else {
//                winParams.flags &= ~bits;
//            }
        window.setAttributes(winParams);
    }
}
