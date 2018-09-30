package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface ListIPresenter<T> extends IPresenter {

    public void search(String key,long salesId, int pageIndex, int pageSize) ;



    public void onListItemClick(T data);
    }
