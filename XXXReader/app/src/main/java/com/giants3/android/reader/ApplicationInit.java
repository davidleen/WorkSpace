package com.giants3.android.reader;

import android.app.Application;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.network.ApiConnection;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.R;
import com.xxx.reader.Utils;

/**
 * Created by davidleen29 on 2018/11/24.
 */

public class ApplicationInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {// 调试状态下  连接 数据库查看工具
            //chrome 浏览器下 输入以下地址：	chrome://inspect/#devices
//			com.facebook.stetho.Stetho.initializeWithDefaults(this);
        }
        ResApiFactory.getInstance().setResApi(new ApiConnection());
        HttpUrl.init(this);
        StorageUtils.setRoot("AAAAAAA");
        Utils.init(getApplicationContext());
    }
}
