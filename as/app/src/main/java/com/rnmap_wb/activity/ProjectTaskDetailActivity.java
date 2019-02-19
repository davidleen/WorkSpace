package com.rnmap_wb.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.activity.mapwork.TileUrlHelper;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.dao.IDownloadTaskDao;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.android.entity.DownloadItem;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.map.KmlHelper;
import com.rnmap_wb.map.TileUtil;
import com.rnmap_wb.utils.IntentConst;
import com.rnmap_wb.utils.StorageUtils;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

public class ProjectTaskDetailActivity extends BaseMvpActivity {


    private static final int RQUEST_MAP = 33333;
    public static final String DOWNLOADNAME = "KML区域下载";
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.sender)
    TextView sender;
    @Bind(R.id.createTime)
    TextView createTime;
    @Bind(R.id.memo)
    TextView memo;
    @Bind(R.id.file)
    TextView file;
    @Bind(R.id.kml)
    View kml;
    @Bind(R.id.offLine)
    View offLine;
    @Bind(R.id.view)
    View view;

    Task task;

    @Override
    protected Presenter createPresenter() {
        return new BasePresenter() {
            @Override
            public Model createModel() {
                return null;
            }

            @Override
            public void start() {

            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationController().setTitle("任务详情");
        getNavigationController().setLeftView(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        task = GsonUtils.fromJson(getIntent().getStringExtra(IntentConst.KEY_TASK_DETAIL), Task.class);

        showTask(task);
        final String filePath = StorageUtils.getFilePath(task.name + ".kml");

        kml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (task == null) return;




                if (new File(filePath).exists()) {

                    MapWorkActivity.start(ProjectTaskDetailActivity.this, task, filePath, RQUEST_MAP);

                } else {
                    showWaiting();
                    UseCaseFactory.getInstance().createDownloadUseCase(task.kml, filePath).execute(new UseCaseHandler() {
                        @Override
                        public void onError(Throwable e) {
                            hideWaiting();
                            ToastHelper.show(e.getMessage());
                        }

                        @Override
                        public void onNext(Object object) {

                        }

                        @Override
                        public void onCompleted() {

                            hideWaiting();

                            if (DaoManager.getInstance().getDownloadTaskDao().findByName(DOWNLOADNAME + filePath).size() > 0) {
                                MapWorkActivity.start(ProjectTaskDetailActivity.this, task, filePath, RQUEST_MAP);

                            } else {
                                showOfflineAlert(task, filePath,true);
                            }


                        }
                    });

                }


            }
        });
        kml.setVisibility(!new File(filePath).exists()?View.VISIBLE:View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MapWorkActivity.start(ProjectTaskDetailActivity.this, task, filePath, RQUEST_MAP);
            }
        });



        offLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filePath = StorageUtils.getFilePath(task.name + ".kml");

                showOfflineAlert(task,filePath,false);

            }
        });

    }


    private void showOfflineAlert(final Task task, final String kmlFilePath,final  boolean viewNow) {

        final int minzoom = TileUrlHelper.MIN_OFFLINE_ZOOM;
        final int maxZoom = TileUrlHelper.MAX_OFFLINE_ZOOM;
        final String[] s = new String[maxZoom - minzoom];
        for (int i = 0; i < s.length; i++) {

            s[i] = String.valueOf((int) (minzoom + i+1));
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("下载kml对应的离线地图？(选择地图层级)")


                .setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(which);

                        Integer fromZoom = Integer.valueOf(s[which]);
                        startOffLineTask(task, kmlFilePath, Math.max(minzoom, fromZoom - 1), Math.min(fromZoom + 1, maxZoom),viewNow);


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

    private void startOffLineTask(final Task task, final String kmlFilePath, final int fromZoom, final int toZoom, final boolean viewNow) {
        showWaiting();
        new AsyncTask<Void, Void, KmlDocument>() {

            boolean parserResult;

            @Override
            protected KmlDocument doInBackground(Void... voids) {

                KmlDocument kmlDocument = new KmlDocument();
                boolean b = false;
                try {
                    b = kmlDocument.parseKMLFile(new File(kmlFilePath));
                } catch (Throwable e) {
                    e.printStackTrace();
                }

//
                if (!b) {
                      b = kmlDocument.parseKMLStream(getResources().openRawResource(R.raw.k00002), null);
                    //b = kmlDocument.parseKMLStream(getResources().openRawResource(R.raw.campus), null);
                   //  b = kmlDocument.parseKMLStream(getResources().openRawResource(R.raw.kmlgeometrytest), null);
                }


                parserResult = b;

                Log.e("parse result:" + b);


                KmlHelper kmlHelper = new KmlHelper();
                List<GeoPoint> geoPoints = kmlHelper.getAllGeoPoint(kmlDocument);
                int[] xy = new int[2];

                DownloadTask downloadTask = new DownloadTask();
                downloadTask.setCreateTime(Calendar.getInstance().getTime().toString());
                downloadTask.setName(DOWNLOADNAME +"-"+ task.name + "，层级：" + fromZoom + "-" + toZoom);

                downloadTask.setLatLngs("");
//                int fromZoom = 1;
//                int toZoom = 29;
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


                return kmlDocument;
            }

            @Override
            protected void onPostExecute(KmlDocument kmlDocument) {

                hideWaiting();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (isDestroyed()) return;
                }

                ToastHelper.show("kml离线地图已经加入下载队列。");
                if(viewNow)
                    MapWorkActivity.start(ProjectTaskDetailActivity.this, task, kmlFilePath, RQUEST_MAP);


            }

        }
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    private void showTask(Task task) {

        name.setText(task.name);
        file.setText(task.kml);
        memo.setText(task.memo);
        createTime.setText(task.created);
        sender.setText(task.user_id);


    }

    public static void start(Activity activity, Task task, int requestCode) {

        Intent intent = new Intent(activity, ProjectTaskDetailActivity.class);
        intent.putExtra(IntentConst.KEY_TASK_DETAIL, GsonUtils.toJson(task));
        activity.startActivityForResult(intent, requestCode);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_project_task_detail;
    }
}
