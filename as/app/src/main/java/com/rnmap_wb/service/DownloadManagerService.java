package com.rnmap_wb.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.giants3.android.frame.util.Log;
import com.giants3.android.network.ApiConnection;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.dao.IDownloadItemDao;
import com.rnmap_wb.android.entity.DownloadItem;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.utils.IntentConst;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadManagerService extends Service {


    private static final int MSG_STATE_CHANGE = 99;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case MSG_STATE_CHANGE:

                        DownloadTask task = (DownloadTask) msg.obj;

                        if (downLoadTaskMonitor.listener != null) {
                            downLoadTaskMonitor.listener.onTaskStateChange(task.getId(), task.getPercent(), task.getDownloadedCount(), task.getCount());
                        }


                        break;

                }
            }
        };


        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {

            @Override
            public void run() {

                List<DownloadTask> downloadTasks = DaoManager.getInstance().getDownloadTaskDao().loadAll();

                for (DownloadTask downloadTask : downloadTasks) {
                    if (downloadTask.getState() == 0) {

                        startATask(downloadTask.getId());


                    }
                }

            }
        } );


    }


    DownLoadTaskMonitor downLoadTaskMonitor = new DownLoadTaskMonitor();
    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return downLoadTaskMonitor;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);

        long taskId = intent.getLongExtra(IntentConst.KEY_TASK_ID, 0);
        if (taskId > 0) {


            startATask(taskId);

        }
        return result;
    }

    private void startATask(long taskId) {
        MapTileDownloadThread thread = new MapTileDownloadThread(taskId);
        downloadingThread.put(taskId, thread);
        thread.start();

    }

    public class MapTileDownloadThread extends Thread {


        private long taskId;

        public MapTileDownloadThread(long taskId) {


            this.taskId = taskId;
        }

        boolean destroyed = false;

        @Override
        public void run() {


            ApiConnection apiConnection = new ApiConnection();

            //  IMbTilesDao mapTileDao = DaoManager.getInstance().getMapTileDao();
            DownloadTask downloadTask = DaoManager.getInstance().getDownloadTaskDao().load(taskId);
            IDownloadItemDao downloadItemDao = DaoManager.getInstance().getDownloadItemDao();


            List<DownloadItem> items = null;
            do {

                if (destroyed) break;
                items = downloadItemDao.findUnCompleteByTaskId(taskId, 50);
                for (DownloadItem downloadItem : items) {

                    if (destroyed) break;


                    String downloadFilePath = downloadItem.getDownloadFilePath();
                    boolean exist = new File(downloadFilePath).exists();
                    if (!exist) {
                        Log.e("downloading:" + downloadItem.getUrl() + ",toPath:" + downloadItem.getDownloadFilePath());
                        try {
                            apiConnection.download(downloadItem.getUrl(), downloadFilePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    if (destroyed) break;

                    if (new File(downloadFilePath).exists() && downloadItem.getState() != 2) {

                        downloadItem.setState(2);
                        DaoManager.getInstance().getDownloadItemDao().save(downloadItem);
                        downloadTask.downloadedCount++;
                        downloadTask.percent = (float) downloadTask.downloadedCount / downloadTask.count;
                        DaoManager.getInstance().getDownloadTaskDao().save(downloadTask);
                        Message message = handler.obtainMessage();
                        message.what = MSG_STATE_CHANGE;
                        message.obj = downloadTask;
                        handler.sendMessage(message);

                    }


//
//                    //查找对应的数据库文件
//                    boolean exist = mapTileDao.exist(downloadItem.getTileX(), downloadItem.getTileY(), downloadItem.getTileZ());
//                    if(!exist)
//                    {
//
//                        try {
//                            byte[] bytes = apiConnection.get(downloadItem.getUrl());
//
//                            MbTiles mbTiles=new MbTiles();
//                            mbTiles.tile_column=downloadItem.getTileX();
//                            mbTiles.tile_row=downloadItem.getTileY();
//                            mbTiles.zoom_level=downloadItem.getTileZ();
//                            mbTiles.tile_data=bytes;
//                            mapTileDao.save(mbTiles);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        catch (Throwable t)
//                        {
//                            t.printStackTrace();
//                        }
//                    }
//
//                    if (destroyed) break;
//                    if (mapTileDao.exist(downloadItem.getTileX(), downloadItem.getTileY(), downloadItem.getTileZ()) && downloadItem.getState() != 2) {
//                        downloadItem.setState(2);
//                        DaoManager.getInstance().getDownloadItemDao().save(downloadItem);
//                        downloadTask.downloadedCount++;
//                        downloadTask.percent = (float) downloadTask.downloadedCount / downloadTask.count;
//                        DaoManager.getInstance().getDownloadTaskDao().save(downloadTask);
//                        Message message = handler.obtainMessage();
//                        message.what = MSG_STATE_CHANGE;
//                        message.obj = downloadTask;
//                        handler.sendMessage(message);
//
//                    }
//
//
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } while (items.size() > 0);


            if (items != null && items.size() == 0 && downloadTask.downloadedCount >= downloadTask.count) {
                downloadTask.setState(DownloadTask.STATE_COMPLETE);
                DaoManager.getInstance().getDownloadTaskDao().save(downloadTask);
                Message message = handler.obtainMessage();
                message.what = MSG_STATE_CHANGE;
                message.obj = downloadTask;
                handler.sendMessage(message);
            }


        }

        public void setDestroy(boolean b) {

            try {
                interrupt();
            } catch (Throwable t) {
            }
            try {

                destroyed = true;
                join();
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }


    private Map<Long, MapTileDownloadThread> downloadingThread = new HashMap<>();


    private class DownLoadTaskMonitor extends Binder implements DownLoadBinder {
        public DownLoadListener listener;


        @Override
        public void startDownLoad(long downLoadTaskId) {


            if (downloadingThread.get(downLoadTaskId) != null) {

                return;
            }
            startATask(downLoadTaskId);


        }

        @Override
        public void stopDownLoad(long downLoadTaskId) {


            MapTileDownloadThread thread = downloadingThread.get(downLoadTaskId);
            if (thread != null) {
                thread.setDestroy(true);
                downloadingThread.remove(downLoadTaskId);
            }

        }

        @Override
        public boolean isDownloading(long downloadTaskId) {

            MapTileDownloadThread thread = downloadingThread.get(downloadTaskId);
            return thread != null;

        }

        @Override
        public void setListener(DownLoadListener listener) {

            this.listener = listener;
        }
    }


}
