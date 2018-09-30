package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.StockXiaoku;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface StockXiaokuIPresenter<T>  extends IPresenter {


    public void search(String key,int pageIndex, int pageSize) ;

    void onListItemClick(StockXiaoku xiaoku);
}
