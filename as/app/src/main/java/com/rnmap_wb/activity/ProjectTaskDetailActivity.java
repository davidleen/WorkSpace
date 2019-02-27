package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.giants3.android.ToastHelper;
import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.reader.domain.DefaultUseCase;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.adapter.ProjectReplyAdapter;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.data.ProjectReply;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.service.SynchronizeCenter;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.IntentConst;
import com.rnmap_wb.utils.StorageUtils;

import java.io.File;

import butterknife.Bind;

public class ProjectTaskDetailActivity extends BaseMvpActivity {


    public  static final int RQUEST_MAP = 33333;
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
    @Bind(R.id.replyList)
    ListView replyList;
    @Bind(R.id.view)
    View view;

    Task task;

    ProjectReplyAdapter adapter ;

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
        final String filePath =SynchronizeCenter.getKmlFilePath(task); ;

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
                                OffLineHelper.showOfflineAlert(ProjectTaskDetailActivity.this, task, filePath,true);
                            }


                        }
                    });

                }


            }
        });
        kml.setVisibility(!new File(filePath).exists()?View.VISIBLE:View.GONE);
        kml.setVisibility( View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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

                            MapWorkActivity.start(ProjectTaskDetailActivity.this, task, filePath, RQUEST_MAP);

                        }
                    });

                }

            }
        });



        offLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filePath =SynchronizeCenter.getKmlFilePath(task);  ;

                OffLineHelper.showOfflineAlert(ProjectTaskDetailActivity.this, task,filePath,false);

            }
        });


          adapter = new ProjectReplyAdapter(this);
        replyList.setAdapter(adapter);
        //loadReplyList();
    }

    private void loadReplyList() {




        String url=HttpUrl.getProjectReplyList();
        String cacheFilePath=StorageUtils.getFilePath("cache"+File.separator+url.hashCode());
        UseCaseFactory.getInstance().createPostUseCase(url,cacheFilePath,ProjectReply.class).execute(new DefaultUseCaseHandler<RemoteData<ProjectReply>>() {
            @Override
            public void onError(Throwable e) {

                ToastHelper.show(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<ProjectReply> remoteData) {

                if(remoteData.isSuccess()) {
                    adapter.setDataArray(remoteData.data);
                }else
                {
                    ToastHelper.show(remoteData.errmsg);
                }

            }
        });
    }




    private void showTask(Task task) {

        name.setText(task.name);
        file.setText(task.kml_name==null?task.kml:task.kml_name);
        memo.setText(task.memo);
        createTime.setText(task.created);
        sender.setText(task.user_name==null?"":task.user_name);


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
