package com.rnmap_wb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
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


        long taskId = getIntent().getLongExtra(KEY_TASK_ID, 0);

        List<DownloadItem> downloadItems = DaoManager.getInstance().getDownloadItemDao().findAllByTaskId(taskId);

        DownLoadItemAdapter adapter = new DownLoadItemAdapter(this);
        adapter.setDataArray(downloadItems);
        listView.setAdapter(adapter);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_task_list;
    }
}
