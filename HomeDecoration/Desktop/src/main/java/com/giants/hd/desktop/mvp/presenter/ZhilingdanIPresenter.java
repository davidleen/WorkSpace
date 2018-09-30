package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 *
 */
public interface ZhilingdanIPresenter extends IPresenter {


    void search(String key, String startDateString, String endDateString);

    void showAll(boolean allSelected, boolean  caigouSelected, boolean jinhuoSelected);
}
