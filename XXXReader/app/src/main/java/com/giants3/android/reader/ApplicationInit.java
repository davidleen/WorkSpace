package com.giants3.android.reader;

import android.app.Application;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.android.kit.ResourceExtractor;
import com.giants3.android.network.ApiConnection;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.storage.KVFactory;
import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.SimpleRouterCallback;
import com.xxx.reader.Utils;

/**
 * Created by davidleen29 on 2018/11/24.
 */

public class ApplicationInit extends Application  implements RouterCallbackProvider {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {// 调试状态下  连接 数据库查看工具
            //chrome 浏览器下 输入以下地址：	chrome://inspect/#devices
//			com.facebook.stetho.Stetho.initializeWithDefaults(this);
        }
        KVFactory.init(this);
        ResApiFactory.getInstance().setResApi(new ApiConnection());
        ToastHelper.init(this);
        HttpUrl.init(this);
        StorageUtils.setRoot("AAAAAAA");
        ResourceExtractor.context=this;
        Utils.init(getApplicationContext());


    }

    @Override
    public RouterCallback provideRouterCallback() {
        return new SimpleRouterCallback() {
            @Override
            public boolean beforeOpen(Context context, Uri uri) {
//                context.startActivity(new Intent(context, LaunchActivity.class));
                // 是否拦截，true 拦截，false 不拦截
                return false;
            }

            @Override
            public void afterOpen(Context context, Uri uri) {
            }

            @Override
            public void notFound(Context context, Uri uri) {
//                context.startActivity(new Intent(context, NotFoundActivity.class));

                Log.e("not found :"+uri.toString());
            }

            @Override
            public void error(Context context, Uri uri, Throwable e) {
//                context.startActivity(ErrorStackActivity.makeIntent(context, uri, e));
                Log.e("error :"+uri.toString());
                Log.e(e);
            }
        };
    }
}
