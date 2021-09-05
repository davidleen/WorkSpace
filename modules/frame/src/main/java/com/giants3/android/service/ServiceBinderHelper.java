package com.giants3.android.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.giants3.thread.ThreadConst;


/**
 * bindservice 帮助类。
 * <p>
 * <p>
 * bindservice 异步处理，并提供回调方法。
 */
public class ServiceBinderHelper {

    private Context context;
    private Class<?> serviceClass;
    private ServiceConnection serviceConnection;


    public ServiceBinderHelper(Context context, Class<?> serviceClass) {
        this.context = context.getApplicationContext();
        this.serviceClass = serviceClass;
    }


    private AsyncTask<Void, Void, Boolean> binderTask;

    public void bindService(final BindResultListener bindResultListener) {
        bindService(null, bindResultListener);
    }

    public void bindService(final Bundle bundle, final BindResultListener bindResultListener) {


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bindResultListener.onServiceConnected(name, service);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bindResultListener.onServiceDisconnected(name);

            }
        };


        binderTask = new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void[] objects) {
                Intent intent = new Intent(context, serviceClass);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }

                boolean binded = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


                return binded;
            }

            @Override
            protected void onCancelled(Boolean serviceBindResult) {
                super.onCancelled(serviceBindResult);
                if (serviceBindResult)
                    unbindService();

            }

            @Override
            protected void onPostExecute(Boolean o) {
                super.onPostExecute(o);
                if (isCancelled()) {
                    return;
                }
                bindResultListener.onBindResult(o);
            }
        };
        binderTask.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);


    }


    /**
     * 绑定回调 增加绑定结果接口
     */
    public interface BindResultListener extends ServiceConnection {


        void onBindResult(boolean bindResult);

    }


    public void unbindService() {


        clearBindTask();


        try {
            if (serviceConnection != null)
                context.unbindService(serviceConnection);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private void clearBindTask() {

        if (binderTask != null) {
            try {
                binderTask.cancel(true);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            binderTask = null;
        }


    }


}
