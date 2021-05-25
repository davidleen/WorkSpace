package com.giants3.android.window;

import android.app.Activity;
import android.view.WindowManager;

public class WindowHelper {


    public static void fullScreen(Activity activity, boolean on) {


        WindowManager.LayoutParams params = activity.getWindow().getAttributes();

        if (on) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {

            params.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        activity.getWindow().setAttributes(params);


    }
}
