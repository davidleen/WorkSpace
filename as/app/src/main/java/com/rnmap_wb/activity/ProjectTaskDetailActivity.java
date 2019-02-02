package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.utils.IntentConst;
import com.rnmap_wb.utils.StorageUtils;

import java.io.File;

import butterknife.Bind;

public class ProjectTaskDetailActivity extends BaseMvpActivity {


    private static final int RQUEST_MAP = 33333;
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


        kml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(task==null) return ;

                String filePath=StorageUtils.getFilePath(task.name+".kml");


                if(new File(filePath).exists())
                {

                    MapWorkActivity.start(ProjectTaskDetailActivity.this,task,filePath,RQUEST_MAP);

                }else {
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



                            Log.e();
                        }
                    });

                }



            }
        });


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
