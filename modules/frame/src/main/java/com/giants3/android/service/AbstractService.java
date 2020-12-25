package com.giants3.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.giants3.android.frame.BuildConfig;
import com.giants3.android.frame.util.Log;


/**
 * 所有service 的基类
 * @param <T>
 */
public abstract class AbstractService<T extends IBinder> extends Service {




    @Override
    public void onCreate() {
        if(BuildConfig.DEBUG)
        {
            Log.e("onCreate");
        }
        super.onCreate();


    }

    protected abstract T createBinder();

    @Override
    public void onDestroy() {
        if(BuildConfig.DEBUG)
        {
            Log.e("onDestroy");
        }
        super.onDestroy();

    }

    @Nullable
    @Override
    public   T onBind(Intent intent)
    {
      return   createBinder();
    }

}
