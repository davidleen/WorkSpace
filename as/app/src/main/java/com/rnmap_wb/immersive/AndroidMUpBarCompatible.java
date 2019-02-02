package com.rnmap_wb.immersive;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 安卓6.0 版本以上系统状态栏适配类。
 * <p>
 * <p>
 * Created by davidleen29 on 2016/10/8.
 */


public class AndroidMUpBarCompatible implements BarCompatible {
    /**
     * from   View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR since sdk21
     */
    public static int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0x00002000;
    /**
     * Flag indicating that this Window is responsible for drawing the background for the
     * system bars. If set, the system bars are drawn with a transparent background and the
     * corresponding areas in this window are filled with the colors specified in
     * {@link Window#getStatusBarColor()} and {@link Window#getNavigationBarColor()}.
     */
    public static int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000;


//    static
//    {
//
//        Field f= null;
//        try {
//            f =View.class.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR");
//            if(f!=null)
//            {
//                SYSTEM_UI_FLAG_LIGHT_STATUS_BAR= (int) f.get(null);
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            f =Window.class.getField("FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS");
//            if(f!=null)
//            {
//                FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS= (int) f.get(null);
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void setBackgroundColor(Window window, int color) {


        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        Method method = null;
        try {
            //    win.setStatusBarColor(Color.TRANSPARENT);
            method = Window.class.getMethod("setStatusBarColor", int.class);

            if (method != null) {
                method.invoke(window, color);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏字体 图案颜色方案 ，就目前系统只支持 light 与 not light
     *
     * @param textMode
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void setTextMode(Window window, int textMode) {


//        int systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                //  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | (textMode == BAR_MODE_LIGHT ? 0 : SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        ;
        int uiOptions = window.getDecorView().getSystemUiVisibility();

        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
//        if (isImmersiveModeEnabled) {
//            Log.i("Turning immersive mode mode off. ");
//        } else {
//            Log.i("Turning immersive mode mode on.");
//        }
        int systemUiVisibility = uiOptions;
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (textMode == BAR_MODE_LIGHT) {
            systemUiVisibility &= (~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            systemUiVisibility |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        }

        //   Log.i(     " Turning immersive SYSTEM_UI_FLAG_LIGHT_STATUS_BAR:"  +((systemUiVisibility&SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)==SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
//       Log.i(     " Turning immersive "  +(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR==SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
//        Log.i(     " Turning immersive "  +(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR==SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));

        window.getDecorView().setSystemUiVisibility(
                systemUiVisibility
        );


    }
}
