package com.rnmap_wb.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.activity.mapwork.TileUrlHelper;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.dao.IDownloadTaskDao;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.android.entity.DownloadItem;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.map.KmlHelper;
import com.rnmap_wb.map.TileUtil;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OffLineHelper {
    public static void showOfflineAlert(final BaseMvpActivity activity, final Task task, final String kmlFilePath, final boolean viewNow) {

        final int minzoom = TileUrlHelper.MIN_OFFLINE_ZOOM;
        final int maxZoom = TileUrlHelper.MAX_OFFLINE_ZOOM;
        final String[] s = new String[maxZoom - minzoom];
        for (int i = 0; i < s.length; i++) {

            s[i] = String.valueOf((int) (minzoom + i + 1));
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle("下载kml对应的离线地图？(选择地图层级)")


                .setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(which);

                        Integer fromZoom = Integer.valueOf(s[which]);
                        startOffLineTask(activity, task, kmlFilePath, Math.max(minzoom, fromZoom - 1), Math.min(fromZoom + 1, maxZoom), viewNow);


                    }
                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//
//                        MapWorkActivity.start(ProjectTaskDetailActivity.this, task, kmlFilePath, RQUEST_MAP);
//
//
//                    }
//                } )
                .create();
        alertDialog.show();


    }

    public static void startOffLineTask(final BaseMvpActivity activity, final Task task, final String kmlFilePath, final int fromZoom, final int toZoom, final boolean viewNow) {
        activity.showWaiting("正在解析KML文件");
        new AsyncTask<Void, Void, KmlDocument>() {

            boolean parserResult;

            @Override
            protected KmlDocument doInBackground(Void... voids) {

                final KmlDocument kmlDocument = new KmlDocument();
                boolean b = false;
                try {
                    b = kmlDocument.parseKMLFile(new File(kmlFilePath));
                } catch (Throwable e) {
                    e.printStackTrace();
                }

//

                parserResult = b;
                if (!parserResult) return null;

                Log.e("parse result:" + b);
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {

                        initDownloadTask(task, kmlDocument, fromZoom, toZoom);
                    }
                });

                return kmlDocument;
            }

            @Override
            protected void onPostExecute(KmlDocument kmlDocument) {
                activity.hideWaiting();
                if (!parserResult) {
                    ToastHelper.show("kml文件解析失败");
                    return;
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (activity.isDestroyed()) return;
                }

                ToastHelper.show("kml离线地图已经加入下载队列。");
                if (viewNow)
                    MapWorkActivity.start(activity, task, kmlFilePath, ProjectTaskDetailActivity.RQUEST_MAP);


            }

        }
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    private static void initDownloadTask(final Task task, final KmlDocument kmlDocument, final int fromZoom, final int toZoom) {
        DaoManager.getInstance().beginTransaction();

        try {


            initDownloadTaskInTransaction(task, kmlDocument, fromZoom, toZoom);
            DaoManager.getInstance().commitTransaction();
        } catch (Throwable t) {


        } finally {

            DaoManager.getInstance().endTransaction();
        }

    }


    private static void initDownloadTaskInTransaction(final Task task, final KmlDocument kmlDocument, final int fromZoom, final int toZoom) {


        KmlHelper kmlHelper = new KmlHelper();
        List<GeoPoint> geoPoints = kmlHelper.getAllGeoPoint(kmlDocument);
        int[] xy = new int[2];


        DownloadTask downloadTask = new DownloadTask();
        downloadTask.setCreateTime(Calendar.getInstance().getTime().toString());
        downloadTask.setName(ProjectTaskDetailActivity.DOWNLOADNAME + "-" + task.name + "，层级：" + fromZoom + "-" + toZoom);

        downloadTask.setLatLngs("");
        downloadTask.setFromZoom(fromZoom);

        downloadTask.setToZoom(toZoom);
        IDownloadTaskDao downloadTaskDao;
        downloadTaskDao = DaoManager.getInstance().getDownloadTaskDao();
        Long id = downloadTaskDao.insert(downloadTask);
        downloadTask.setId(id);
        List<DownloadItem> downloadItems = new ArrayList<>();
        int totalCount = 0;

        for (GeoPoint geoPoint : geoPoints) {

            for (int z = fromZoom; z <= toZoom; z++) {


                LatLngUtil.getTileNumber(geoPoint.getLatitude(), geoPoint.getLongitude(), z, xy);

                DownloadItem downloadItem = new DownloadItem();
                downloadItem.setTaskId(id);
                downloadItem.setName(geoPoint.toString());
                downloadItem.setTileX(xy[0]);
                downloadItem.setTileY(xy[1]);
                downloadItem.setTileZ(z);
                String url = TileUrlHelper.getUrl(xy[0], xy[1], z);

                if (BuildConfig.DEBUG)
                    Log.e(url);
                downloadItem.setUrl(url);
                downloadItem.setDownloadFilePath(TileUtil.getFilePath(xy[0], xy[1], z));
                downloadItems.add(downloadItem);

                totalCount++;
                if (downloadItems.size() > 1000) {
                    DaoManager.getInstance().getDownloadItemDao().saveAll(downloadItems);
                    downloadItems.clear();
                }

            }

        }

        DaoManager.getInstance().getDownloadItemDao().saveAll(downloadItems);

        downloadTask.count = totalCount;
        downloadTaskDao.save(downloadTask);
    }


}
