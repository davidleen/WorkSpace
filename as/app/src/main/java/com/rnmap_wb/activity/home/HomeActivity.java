package com.rnmap_wb.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giants3.android.ToastHelper;
import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.frame.util.Log;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.BaseMvpActivity;
import com.rnmap_wb.activity.DownloadTaskListActivity;
import com.rnmap_wb.activity.LoginActivity;
import com.rnmap_wb.activity.ProjectTaskDetailActivity;
import com.rnmap_wb.adapter.HomeTaskAdapter;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.service.SynchronizeCenter;
import com.rnmap_wb.url.HttpUrl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;

public class HomeActivity extends BaseMvpActivity<HomePresenter> implements HomeViewer {


    private static final int REQUEST_LOGIN = 999;
    private static final int REQUEST_MAP = 998;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.downloadtask)
    View downloadtask;

    AbstractAdapter<Task> homeTaskAdapter;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);

    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenterImpl();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SynchronizeCenter.synchronize();
        getNavigationController().setTitle(getString(R.string.home));
        getNavigationController().setLeftView(R.drawable.icon_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDrawer();


            }
        });

        homeTaskAdapter = new HomeTaskAdapter(this);
        listView.setAdapter(homeTaskAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Task task = (Task) parent.getItemAtPosition(position);

                ProjectTaskDetailActivity.start(HomeActivity.this, task, REQUEST_MAP);


            }
        });
        downloadtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadTaskListActivity.start(HomeActivity.this);


            }
        });
        reloadData();

//        Intent serviceIntent = new Intent(this, DownloadManagerService.class);
//        serviceIntent.putExtra(IntentConst.KEY_WAKE_DOWNLOAD,99l);
//        startService(serviceIntent);
    }

    @Override
    protected boolean supportDrawer() {
        return true;
    }

    @Override
    protected int getDrawContent() {
        return R.layout.activiity_home_drawer;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activiity_home;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LOGIN:

                    reloadData();
                    break;
            }
        }
    }

    private void reloadData() {
        UseCaseFactory.getInstance().createPostUseCase(HttpUrl.getProjectTasks(), Task.class).execute(new DefaultUseCaseHandler<RemoteData<Task>>() {


            @Override
            public void onError(Throwable e) {

                Log.e(e);
            }

            @Override
            public void onNext(RemoteData<Task> remoteData) {


                if (remoteData.isSuccess()) {
                    List<Task> data = remoteData.data;

                    Task[] sortlist=new Task[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        sortlist[i]=data.get(i);

                    }
                    Arrays.sort(sortlist, new Comparator<Task>() {
                        @Override
                        public int compare(Task task1, Task task2) {

                            int i = task1.dir_id.compareTo(task2.dir_id);
                            if(i!=0) return i;
                            int result=task1.created.compareTo(task2.created);
                                    return result;

                        }
                    });
                    data=Arrays.asList(sortlist);


                    homeTaskAdapter.setDataArray(data);
                }
                else if (remoteData.errno == RemoteData.CODE_UNLOGIN || remoteData.errno == 9998) {
                    LoginActivity.start(HomeActivity.this, REQUEST_LOGIN);
                } else

                    ToastHelper.show(remoteData.errmsg);
            }


        });
    }
}
