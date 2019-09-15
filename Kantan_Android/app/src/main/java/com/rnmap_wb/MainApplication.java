package com.rnmap_wb;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.network.ApiConnection;
import com.giants3.android.push.PushProxy;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.rnmap_wb.android.idao.DaoManager;
import com.rnmap_wb.android.data.LoginResult;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.helper.AndroidUtils;
import com.rnmap_wb.message.NotificationUtils;
import com.rnmap_wb.message.PushManager;
import com.rnmap_wb.service.DownloadManagerService;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.SessionManager;
import com.rnmap_wb.utils.SettingContent;
import com.rnmap_wb.utils.StorageUtils;

public class MainApplication extends Application {


    public static Context baseContext;

    @Override
    public void onCreate() {
        super.onCreate();


        if (BuildConfig.DEBUG) {// 调试状态下  连接 数据库查看工具
            //chrome 浏览器下 输入以下地址：	chrome://inspect/#devices
            //com.facebook.stetho.Stetho.initializeWithDefaults(this);
        }
        baseContext = this;

        StorageUtils.setRoot("rnmap_wb");
        ResApiFactory.getInstance().setResApi(new ApiConnection());
        LoginResult loginUser = SessionManager.getLoginUser(this);
        if (loginUser != null) {
            ResApiFactory.getInstance().getResApi().setHeader("x-token", loginUser.token);
        }
        ToastHelper.init(this);
        HttpUrl.init(this);
        Utils.init(this);
        DaoManager.getInstance().init(this);
        AndroidUtils.init(this);


        try {
            startService(new Intent(this, DownloadManagerService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initPush();
    }


    private void initPush() {
        NotificationUtils.createNotificationChanel(this);
        PushManager.getInstance().init(this);


        PushProxy.config(this, new com.rnmap_wb.android.api.push.RegisterCallback() {
            @Override
            public void onSuccess(final String... deviceToken) {
                SettingContent.getInstance().setDeviceToken(deviceToken[0]);
                UseCaseFactory.getInstance().createPostUseCase(HttpUrl.uploadDeviceToken(deviceToken[0]), Void.class).execute(new DefaultUseCaseHandler<RemoteData<Void>>() {

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RemoteData<Void> remoteData) {


                        if (remoteData.isSuccess()) {


                        } else {
                            Log.e(remoteData.errmsg);
                        }
                    }


                });
                Log.e(SettingContent.getInstance().getToken());
            }

            @Override
            public void onFail(String... message) {
                for (int i = 0; i < message.length; i++) {
                    Log.e(message[i]);

                }

            }
        }, new com.rnmap_wb.android.api.push.MessageCallback() {
            @Override
            public void onMessageReceived(String message) {


                //ToastHelper.show(message);
                Task task   = null;
                try {
                    task = GsonUtils.fromJson(message, Task.class);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                PushManager.getInstance().handleMessage(task);
            }
        });

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
}
