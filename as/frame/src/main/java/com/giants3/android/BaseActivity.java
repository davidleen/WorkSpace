package com.giants3.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.giants3.android.frame.BuildConfig;
import com.giants3.android.mvp.AndroidRouter;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.mvp.Viewer;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {

        ButterKnife.unbind(this);

        super.onDestroy();


    }

}
