package com.rnmap_wb.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.reader.domain.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.rnmap_wb.R;
import com.rnmap_wb.adapter.DownLoadItemAdapter;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.entity.DownloadItem;

import java.util.List;

import butterknife.Bind;

public class DownloadTaskItemListActivity extends BaseMvpActivity {


    public static final String KEY_TASK_ID = "TASK_ID";
    @Bind(R.id.listview)
    ListView listView;
    DownLoadItemAdapter adapter;

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





        adapter = new DownLoadItemAdapter(this);

        listView.setAdapter(adapter);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_task_list;
    }


    private void loadDownloadTask() {
        new AsyncTask<Void, Void, List<DownloadItem>>() {
            @Override
            protected List<DownloadItem> doInBackground(Void... voids) {
                long taskId = getIntent().getLongExtra(KEY_TASK_ID, 0);
                List<DownloadItem> downloadItems = GsonUtils.fromJson(GsonUtils.toJson(DaoManager.getInstance().getDownloadItemDao().findAllByTaskId(taskId)), new TypeToken<List<DownloadItem>>() {
                }.getType());


                return downloadItems;


            }

            @Override
            protected void onPostExecute(List<DownloadItem> downloadItems) {


                if (downloadItems != null)
                    adapter.setDataArray(downloadItems);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
}
