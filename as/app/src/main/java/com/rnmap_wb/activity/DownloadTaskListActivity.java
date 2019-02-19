package com.rnmap_wb.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.reader.domain.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.rnmap_wb.R;
import com.rnmap_wb.adapter.DownLoadTaskAdapter;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.service.DownLoadBinder;
import com.rnmap_wb.service.DownloadManagerService;

import java.util.List;

import butterknife.Bind;

public class DownloadTaskListActivity extends BaseMvpActivity implements DownLoadBinder.DownLoadListener {

    @Bind(R.id.listview)
    ListView listView;

    private DownLoadBinder mBinder;
    private boolean mBound;
    MyServiceConnection conn = new MyServiceConnection();
    DownLoadTaskAdapter adapter;

    public static void start(Activity activity) {

        Intent intent = new Intent(activity, DownloadTaskListActivity.class);
        activity.startActivity(intent);

    }

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

        getNavigationController().setTitle("离线地图管理");
        getNavigationController().setLeftView(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new DownLoadTaskAdapter(this);
        adapter.setOnTaskStateChangeListener(new DownLoadTaskAdapter.TaskStateChangeListener() {
            @Override
            public void onTaskStateChange(DownloadTask downloadTask) {


                if (downloadTask.getState() == DownloadTask.STATE_DOWNLOADING) {
                    downloadTask.setState(DownloadTask.STATE_STOP);
                    mBinder.stopDownLoad(downloadTask.getId());
                    DaoManager.getInstance().getDownloadTaskDao().save(downloadTask);
                    adapter.notifyDataSetChanged();
                } else if (downloadTask.getState() == DownloadTask.STATE_STOP) {

                    downloadTask.setState(DownloadTask.STATE_DOWNLOADING);
                    mBinder.startDownLoad(downloadTask.getId());
                    DaoManager.getInstance().getDownloadTaskDao().save(downloadTask);
                    adapter.notifyDataSetChanged();

                }


            }
        });

        listView.setAdapter(adapter);
        loadData();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                DownloadTask itemAtPosition = (DownloadTask) parent.getItemAtPosition(position);
//                Intent intent = new Intent(DownloadTaskListActivity.this, DownloadTaskItemListActivity.class);
//                intent.putExtra(DownloadTaskItemListActivity.KEY_TASK_ID, itemAtPosition.getId());
//
//                startActivity(intent);
//
//
//            }
//        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final DownloadTask task = (DownloadTask) parent.getItemAtPosition(position);

                AlertDialog alertDialog = new AlertDialog.Builder(parent.getContext()).setMessage("是否删除任务?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        adapter.removeItem(task);
                        adapter.notifyDataSetChanged();

                        new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                DaoManager.getInstance().getDownloadItemDao().deleteItemByTaskId(task.getId());
                                DaoManager.getInstance().getDownloadTaskDao().delete(task);
                                mBinder.stopDownLoad(task.getId());


                                return null;
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    }
                }).create();
                alertDialog.show();

                return false;
            }


        });
        bindService();
    }


    private void loadData() {


        showWaiting();
        new AsyncTask<Void, Void, List<DownloadTask>>() {
            @Override
            protected List<DownloadTask> doInBackground(Void... voids) {

                List<DownloadTask> downloadTasks = GsonUtils.fromJson(GsonUtils.toJson(DaoManager.getInstance().getDownloadTaskDao().loadAll()), new TypeToken<List<DownloadTask>>() {
                }.getType());


                return downloadTasks;
            }

            @Override
            protected void onPostExecute(List<DownloadTask> downloadTasks) {
                hideWaiting();
                if (downloadTasks != null)
                    adapter.setDataArray(downloadTasks);
            }

        }

                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void bindService() {

        Intent intent = new Intent(this, DownloadManagerService.class);

        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_task_list;
    }

    @Override
    public void onTaskStateChange(long taskId, float percent, int downloadCount, int totalCount) {


        adapter.updateTaskState(taskId, percent, downloadCount, totalCount);


    }


    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {


            mBinder = (DownLoadBinder) binder;
            mBinder.setListener(DownloadTaskListActivity.this);

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.

            mBound = false;
        }

    }


    @Override
    protected void onDestroy() {

        unbindService();
        super.onDestroy();
    }

    private void unbindService() {

        if (mBound) {
            mBinder.setListener(null);
            unbindService(conn);

            mBound = false;
        }


    }

}
