package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowQueryPresenter extends IPresenter {


    void save(boolean updateCompletedOrderItem);

    void search(String key, int pageIndex , int pageSize);

    void queryOrderItems(String key,int workFlowState, int flowStep, int alertType,int pageIndex, int pageSize);
}
