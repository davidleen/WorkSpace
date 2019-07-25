package com.rnmap_wb.service;

import android.os.IBinder;


public interface DownLoadBinder extends IBinder {


    public void startDownLoad(long downLoadTaskId);

    public void stopDownLoad(long downLoadTaskId);


    public boolean isDownloading(long downloadTaskId);


    public void setListener(DownLoadListener listener);


    interface DownLoadListener {


        void onTaskStateChange(long taskId, float percent, int  downloadCount, int totalCount);

    }

}
