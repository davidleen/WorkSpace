package com.giants3.android.reader;

import android.app.Application;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.network.ApiConnection;
import com.giants3.android.reader.domain.ResApiFactory;

/**
 * Created by davidleen29 on 2018/11/24.
 */

public class ApplicationInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ResApiFactory.getInstance().setResApi(new ApiConnection());
        HttpUrl.init(this);
        StorageUtils.setRoot("AAAAAAA");
    }
}
