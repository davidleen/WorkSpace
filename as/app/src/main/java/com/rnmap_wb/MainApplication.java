package com.rnmap_wb;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.network.ApiConnection;
import com.giants3.android.reader.domain.ResApiFactory;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.data.LoginResult;
import com.rnmap_wb.helper.AndroidUtils;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.SessionManager;
import com.rnmap_wb.utils.StorageUtils;

public class MainApplication extends Application {


    public static Context baseContext;

    @Override
    public void onCreate() {
        super.onCreate();
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
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
}
