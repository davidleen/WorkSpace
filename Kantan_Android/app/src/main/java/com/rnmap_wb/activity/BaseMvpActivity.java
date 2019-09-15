package com.rnmap_wb.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.giants3.android.BaseActivity;
import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.mvp.Viewer;
import com.giants3.android.push.PushProxy;
import com.rnmap_wb.R;
import com.rnmap_wb.analytics.AnalysisFactory;
import com.rnmap_wb.immersive.SmartBarUtils;
import com.rnmap_wb.widget.NavigationBarController;

import java.util.Calendar;

public abstract class BaseMvpActivity<P extends Presenter> extends BaseActivity implements Viewer {
    P presenter;
    NavigationBarController controller;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PushProxy.onAppStart();
        SmartBarUtils.setTranslucentStatus(this, true);
        if (SmartBarUtils.hasSmartBar())
            SmartBarUtils.hideActionBarByActivity(this);

        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_root, null);
        FrameLayout frameLayout = contentView.findViewById(R.id.activity_content);

        int contentLayoutId = getContentLayoutId();
        if(contentLayoutId!=0) {
            View contentLayout = LayoutInflater.from(this).inflate(contentLayoutId, null);
            frameLayout.addView(contentLayout);
        }


        if(supportDrawer())
        {

             drawerLayout=new DrawerLayout(this);
            drawerLayout.addView(contentView);
            View drawer=LayoutInflater.from(this).inflate(getDrawContent(),null);
            drawer.setClickable(true);
            drawer.findViewById(R.id.drawer_head).setPadding(0,SmartBarUtils.getStatusBarHeight(this),0,0);
            DrawerLayout.LayoutParams  layoutParams=new DrawerLayout.LayoutParams(Utils.dp2px(this,300),DrawerLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity=Gravity.START;
            drawerLayout.addView(drawer,layoutParams);

            contentView=drawerLayout;



        }
        setContentView(contentView);


        controller = new NavigationBarController(this);

        presenter = createPresenter();
        presenter.attachView(this);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        AnalysisFactory.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalysisFactory.getInstance().onPause(this);
    }

    protected abstract @LayoutRes   int getContentLayoutId();

    /**
     * 是否支持侧边栏
     * @return
     */
    protected  boolean supportDrawer()
    {
        return false;
    }

    /**
     * 侧边栏的 布局
     * @return
     */
    protected   @LayoutRes   int getDrawContent()
    {
        return 0;
    }

    protected  DrawerLayout getDrawerLayout()
    {
        return drawerLayout;
    }

    protected  void openDrawer()
    {
        drawerLayout.openDrawer(Gravity.START);
    }

    protected  void closeDrawer()
    {
        drawerLayout.closeDrawer(Gravity.START);
    }

    protected NavigationBarController getNavigationController() {
        return controller;
    }

    protected abstract P createPresenter();
    ProgressDialog progressDialog;

    @Override
    public void hideWaiting() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    @Override
    public void showMessage(String message) {

        ToastHelper.show(message);
    }

    @Override
    public void showWaiting() {

        showWaiting("");

    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void showWaiting(String message) {
        hideWaiting();
        progressDialog = new ProgressDialog(this);
        if(!StringUtil.isEmpty(message))
            progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public Context getContext() {
        return this;
    }



}
