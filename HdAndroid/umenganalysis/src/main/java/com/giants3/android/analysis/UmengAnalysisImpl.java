package com.giants3.android.analysis;

import android.content.Context;


import com.giants3.android.api.analytics.AnalysisApi;
import com.giants3.android.utils.ManifestUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by davidleen29 on 2018/3/31.
 */

public class UmengAnalysisImpl implements AnalysisApi {




    public UmengAnalysisImpl(Context context)
    {

//与微社区存在UMENG_APP_KEY上不一致的冲突
        // 初始化SDK
        String key=  ManifestUtils.getMetaData(context,"UMENG_APPKEY");
        String secret=  ManifestUtils.getMetaData(context,"UMENG_MESSAGE_SECRET");
        String chanel=ManifestUtils.getMetaData(context,"UMENG_CHANNEL");
        UMConfigure.init(context, key, chanel, UMConfigure.DEVICE_TYPE_PHONE, secret);
        // 选用MANUAL页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        MobclickAgent.setDebugMode(true);



    }
    @Override
    public void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        MobclickAgent.onPause(context);
    }
}
