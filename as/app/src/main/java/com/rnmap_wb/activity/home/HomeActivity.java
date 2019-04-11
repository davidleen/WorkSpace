package com.rnmap_wb.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.AboutActivity;
import com.rnmap_wb.activity.BaseMvpActivity;
import com.rnmap_wb.activity.DownloadTaskListActivity;
import com.rnmap_wb.activity.FeedBackDialog;
import com.rnmap_wb.activity.LoginActivity;
import com.rnmap_wb.activity.OffLineHelper;
import com.rnmap_wb.activity.ProjectTaskDetailActivity;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.adapter.HomeTaskAdapter;
import com.rnmap_wb.android.data.Directory;
import com.rnmap_wb.android.data.LoginResult;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.android.data.VersionData;
import com.rnmap_wb.helper.AndroidUtils;
import com.rnmap_wb.service.SynchronizeCenter;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.SessionManager;
import com.rnmap_wb.utils.SettingContent;
import com.rnmap_wb.utils.StorageUtils;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

public class HomeActivity extends BaseMvpActivity<HomePresenter> implements HomeViewer, View.OnClickListener {


    private static final int REQUEST_LOGIN = 999;
    private static final int REQUEST_MAP = 998;
    public static String PARAM_TASK ="PARAM_TASK";


    @Bind(R.id.listview)
    ExpandableListView listView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.mytask)
    View mytask;

    @Bind(R.id.switchId)
    View switchId;

    @Bind(R.id.about)
    View about;

    @Bind(R.id.update)
    View update;
    @Bind(R.id.changePass)
    View changePass;
    @Bind(R.id.myMessage)
    View myMessage;

    @Bind(R.id.userName)
    TextView userName;

    @Bind(R.id.email)
    TextView email;

    @Bind(R.id.downloadtask)
    View downloadtask;

    @Bind(R.id.report)
    View report;

    HomeTaskAdapter homeTaskAdapter;

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

        homeTaskAdapter = new HomeTaskAdapter(this, new HomeTaskAdapter.ItemListener() {
            @Override
            public void onFeedBack(final Task task) {
                if (SynchronizeCenter.waitForFeedBack(task)) return;
                FeedBackDialog.start(HomeActivity.this, task);


            }

            @Override
            public void download(final Task task) {
                final String filePath = SynchronizeCenter.getKmlFilePath(task);
                if (new File(filePath).exists()) {

                    OffLineHelper.showOfflineAlert(HomeActivity.this, task, filePath, false);


                } else {
                    showWaiting("正在下载KML文件");
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


                            OffLineHelper.showOfflineAlert(HomeActivity.this, task, filePath, false);
                        }
                    });

                }
            }

            @Override
            public void view(final Task task) {

                final String filePath = SynchronizeCenter.getKmlFilePath(task);
                ;
                if (new File(filePath).exists()) {
                    MapWorkActivity.start(HomeActivity.this, task, filePath, 0);
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
                            MapWorkActivity.start(HomeActivity.this, task, filePath, 0);

                        }
                    });

                }


            }

            @Override
            public void onDetail(Task task) {
                ProjectTaskDetailActivity.start(HomeActivity.this, task, REQUEST_MAP);
            }
        });
        listView.setAdapter(homeTaskAdapter);

        downloadtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadTaskListActivity.start(HomeActivity.this);


            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkUpdate(false);
            }
        });
        mytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDrawer();


            }
        });

        myMessage.setOnClickListener(this);

        switchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginActivity.start(HomeActivity.this, REQUEST_LOGIN);

            }
        });
        changePass.setOnClickListener(this);
        report.setOnClickListener(this);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AboutActivity.start(HomeActivity.this);
            }
        });

        LoginResult loginUser = SessionManager.getLoginUser(HomeActivity.this);
        userName.setText(loginUser == null ? "" : loginUser.name);
        email.setText(loginUser == null ? "" : loginUser.email);
        reloadData();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });

        //读取更新数据
        checkUpdate(true);



        handleIntent(getIntent());

//        Intent serviceIntent = new Intent(this, DownloadManagerService.class);
//        serviceIntent.putExtra(IntentConst.KEY_WAKE_DOWNLOAD,99l);
//        startService(serviceIntent);
    }


    private void checkUpdate(final boolean silence) {
        UpdateAppManager build = new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                .setPost(true)
                //更新地址
                .setUpdateUrl(HttpUrl.getAppUpdateInfo())
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil()).build();
        build.checkNewApp(new UpdateCallback() {

            @Override
            protected void noNewApp(String error) {

                if (!silence) {
                    ToastHelper.show(error);
                }
            }

            @Override
            protected UpdateAppBean parseJson(String json) {
                UpdateAppBean updateAppBean = new UpdateAppBean();

                VersionData versionData = GsonUtils.fromJson(json, VersionData.class);
                updateAppBean
                        //（必须）是否更新Yes,No
                        .setUpdate(versionData.version_num > AndroidUtils.getVersionCode() ? "Yes" : "No")
                        //（必须）新版本号，
                        .setNewVersion(versionData.version)
                        //（必须）下载地址
                        .setApkFileUrl(versionData.down_url)
                        //（必须）更新内容
                        .setUpdateLog(versionData.message)
                        //大小，不设置不显示大小，可以不设置
                        .setTargetSize("")
                        //是否强制更新，可以不设置
                        .setConstraint(false)
                        //设置md5，可以不设置
                        .setNewMd5("");

                return updateAppBean;

            }
        });


        build.update();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {


        String taskString=intent.getStringExtra(PARAM_TASK);
        if(!StringUtil.isEmpty(taskString))
        {
            Task  task=GsonUtils.fromJson(taskString,Task.class);
            ProjectTaskDetailActivity.start(HomeActivity.this, task, REQUEST_MAP);
        }
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


                    LoginResult loginUser = SessionManager.getLoginUser(HomeActivity.this);
                    userName.setText(loginUser == null ? "" : loginUser.name);


                    UseCaseFactory.getInstance().createPostUseCase(HttpUrl.uploadDeviceToken(SettingContent.getInstance().getToken()), Void.class).execute(new DefaultUseCaseHandler<RemoteData<Void>>() {

                        @Override
                        public void onError(Throwable e) {

                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(RemoteData<Void> remoteData) {


                            if (remoteData.isSuccess()) {


                            } else {
                                Log.e(remoteData.errmsg);
                            }
                        }


                    });

                    reloadData();
                    break;
            }
        }
    }

    private void reloadData() {


        String projectTaskUrl = HttpUrl.getProjectTasks();
        String cacheFile = StorageUtils.getFilePath("cache" + File.separator + String.valueOf(projectTaskUrl.hashCode()));
        UseCaseFactory.getInstance().createPostUseCase(projectTaskUrl, cacheFile, Directory.class).execute(new DefaultUseCaseHandler<RemoteData<Directory>>() {


            @Override
            public void onError(Throwable e) {
                swipeLayout.setRefreshing(false);
                Log.e(e);
            }

            @Override
            public void onNext(RemoteData<Directory> remoteData) {
                swipeLayout.setRefreshing(false);

                if (remoteData.isSuccess()) {
                    List<Directory> data = remoteData.data;


                    homeTaskAdapter.setDatas(data);
                    if (data.size() > 0)
                        listView.expandGroup(0);
                } else if (remoteData.errno == RemoteData.CODE_UNLOGIN || remoteData.errno == 9998) {
                    LoginActivity.start(HomeActivity.this, REQUEST_LOGIN);
                } else

                    ToastHelper.show(remoteData.errmsg);
            }


        });
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.changePass:

                break;
            case R.id.myMessage:

                break;

            case R.id.report:

                break;
        }
    }


    @Override
    public void onBackPressed() {

        if (getDrawerLayout() != null && getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        } else {

            if (checkBack()) {
                super.onBackPressed();

            } else {
                ToastHelper.show("再次点击返回键退出应用");
            }


        }
    }


    //上次返回键点击时间
    long lastBackPressTime;


    public boolean checkBack() {


        long time = Calendar.getInstance().getTimeInMillis();

        if (time - lastBackPressTime < 2000) {
            return true;
        }
        lastBackPressTime = time;
        return false;


    }
}
