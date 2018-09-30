package com.giants.hd.desktop.mvp.presenter;


import com.giants.hd.desktop.mvp.IPresenter;

/**
 *
 * 订单报表展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderWorkFlowReportIPresenter extends IPresenter {


    /**
     * 查询未出库货款
     */
    void searchUnDoneOrder( );



    /**
     * 导出报表
     */
    void export();

    void search(String key, int pageIndex, int pageSize);
}
