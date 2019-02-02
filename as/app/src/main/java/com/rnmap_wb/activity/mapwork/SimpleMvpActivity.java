package com.rnmap_wb.activity.mapwork;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.giants3.android.BaseActivity;
import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Model;
import com.giants3.android.mvp.Presenter;
import com.giants3.android.mvp.Viewer;
import com.rnmap_wb.activity.BaseMvpActivity;

public abstract   class SimpleMvpActivity  extends BaseMvpActivity<BasePresenter> {


    @Override
    protected BasePresenter createPresenter() {
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
}
