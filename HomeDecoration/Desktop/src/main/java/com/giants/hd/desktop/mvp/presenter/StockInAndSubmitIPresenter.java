package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface StockInAndSubmitIPresenter<T>  extends IPresenter {


    public void search(String key,String startDate,String endDate) ;

    /**
     * 格式1 导出
     */
    void exportExcel();

    /**
     * 格式2
     */
    void exportExcel2();
}
