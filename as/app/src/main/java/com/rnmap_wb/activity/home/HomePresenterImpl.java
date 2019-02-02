package com.rnmap_wb.activity.home;

import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.mvp.Viewer;

public class HomePresenterImpl  extends BasePresenter<HomeViewer,HomeModel>implements HomePresenter {
    @Override
    public HomeModel createModel() {
        return new HomeModel() {
        };
    }

    @Override
    public void attachView(HomeViewer view) {

    }

    @Override
    public void start() {

    }
}
