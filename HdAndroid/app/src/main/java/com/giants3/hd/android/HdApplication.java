package com.giants3.hd.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.giants3.android.api.push.MessageCallback;
import com.giants3.android.api.push.RegisterCallback;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.push.PushProxy;
import com.giants3.hd.android.helper.AndroidUtils;
import com.giants3.hd.android.helper.BitmapToolkit;
import com.giants3.hd.android.helper.ConnectionHelper;
import com.giants3.hd.android.helper.NotificationUtils;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.message.PushManager;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.app.PushMessage;
import com.nostra13.universalimageloader.core.LibContext;

;

/**
 * Created by david on 2016/1/2.
 */
public class HdApplication extends Application {
    public static final String TAG = "Application";
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */

    public static Context baseContext;

    @Override
    public void onCreate() {
        super.onCreate();
        baseContext = this;

        StorageUtils.setRoot(getResources().getString(R.string.sd_root));
        ToastHelper.init(this);
        BitmapToolkit.init(this);
        SharedPreferencesHelper.init(this);


        ConnectionHelper.init(this);
        HttpUrl.init(this);
        Utils.init(this);
        AndroidUtils.init(this);
        NotificationUtils.createNotificationChanel(this);
        PushManager.getInstance().init(this);


        boolean autoUpdates = BuildConfig.AUTO_UPDATES;


        PushProxy.config(this, new RegisterCallback() {
            @Override
            public void onSuccess(String... deviceToken) {
                SystemConst.device_token = deviceToken[0];
                Log.e(TAG, SystemConst.device_token);
            }

            @Override
            public void onFail(String... message) {
                for (int i = 0; i < message.length; i++) {
                    Log.e(TAG, message[i]);

                }

            }
        }, new MessageCallback() {
            @Override
            public void onMessageReceived(String message) {


                //ToastHelper.show(message);
                PushMessage pushMessage = null;
                try {
                    pushMessage = GsonUtils.fromJson(message, PushMessage.class);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                PushManager.getInstance().handleMessage(pushMessage);
            }
        });

        LibContext.init(this);
    }


}



