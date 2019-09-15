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
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.activity.OffLineHelper;
import com.rnmap_wb.activity.mapwork.TileUrlHelper;
import com.rnmap_wb.android.idao.DaoManager;
import com.rnmap_wb.android.idao.IDownloadItemDao;
import com.rnmap_wb.android.idao.IDownloadTaskDao;
import com.rnmap_wb.android.entity.DownloadItem;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.utils.IntentConst;
import com.rnmap_wb.utils.SettingContent;
import com.rnmap_wb.utils.TelephoneUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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


        tryStartDownloadJob();


    }

    private void tryStartDownloadJob() {
        if (TelephoneUtil.isWifiEnable() && SettingContent.getInstance().autoDownloadOnWifi()) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    IDownloadTaskDao downloadTaskDao = DaoManager.getInstance().getDownloadTaskDao();
                    List<DownloadTask> downloadTasks = downloadTaskDao.loadAll();

                    for (DownloadTask downloadTask : downloadTasks) {
                        if (downloadTask.getState() == DownloadTask.STATE_NONE ) {
                            startATask(downloadTask.getId());


                        }
                    }

                }
            });
        }
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

        if (intent == null) return result;
        long taskId = intent.getLongExtra(IntentConst.KEY_TASK_ID, 0);
        if (taskId > 0) {
            startATask(taskId);
        } else {

            tryStartDownloadJob();
        }


        return result;
    }

    private void startATask(long taskId) {


        if (downloadingThread.get(taskId) == null) {

            MapTileDownloadThread thread = new MapTileDownloadThread(taskId);
            downloadingThread.put(taskId, thread);
            thread.start();
        }

    }

    public class MapTileDownloadThread extends Thread {


        private long taskId;

        public MapTileDownloadThread(long taskId) {


            this.taskId = taskId;
        }

        public AtomicBoolean destroy = new AtomicBoolean();

        @Override
        public void run() {


            ApiConnection apiConnection = new ApiConnection();

            //  IMbTilesDao mapTileDao = DaoManager.getInstance().getMapTileDao();
            IDownloadTaskDao downloadTaskDao = DaoManager.getInstance().getDownloadTaskDao();
            DownloadTask downloadTask = downloadTaskDao.load(taskId);
            downloadTask.setState(DownloadTask.STATE_DOWNLOADING);
            downloadTaskDao.save(downloadTask);
            IDownloadItemDao downloadItemDao = DaoManager.getInstance().getDownloadItemDao();


            List<DownloadItem> items = null;
            do {

                if (destroy.get()) break;
                items = downloadItemDao.findUnCompleteByTaskId(taskId, 50);
                for (DownloadItem downloadItem : items) {

                    if (destroy.get()) break;




                    String downloadFilePath = downloadItem.getDownloadFilePath();
                    boolean exist = new File(downloadFilePath).exists();
                    if (!exist) {



                        for (int i = 0; i < TileUrlHelper.MAX_MT_COUNT; i++) {
                            String url= TileUrlHelper.getUrl(downloadItem.getTileX(),downloadItem.getTileY(),downloadItem.getTileZ(),i);
                            if(BuildConfig.DEBUG)
                                Log.e("downloading:" +url + ",toPath:" + downloadItem.getDownloadFilePath());
                            try {
                                apiConnection.download(url, downloadFilePath);
                                if(new File(downloadFilePath).exists())
                                    break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    if (destroy.get()) break;

                    if (new File(downloadFilePath).exists() && downloadItem.getState() != DownloadItem.STATE_DONE) {

                        downloadItem.setState(DownloadItem.STATE_DONE);
                        DaoManager.getInstance().getDownloadItemDao().save(downloadItem);
                        downloadTask.downloadedCount++;
                        downloadTask.percent = (float) downloadTask.downloadedCount / downloadTask.count;
                        downloadTaskDao.save(downloadTask);
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
                downloadTaskDao.save(downloadTask);
                Message message = handler.obtainMessage();
                message.what = MSG_STATE_CHANGE;
                message.obj = downloadTask;
                handler.sendMessage(message);
            }


        }

        public void setDestroy() {


            destroy.set(true);
            interrupt();
            try {
                join();
            } catch (InterruptedException e) {
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
                thread.setDestroy();
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
