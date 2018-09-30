package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface SubWorkFlowListIPresenter extends IPresenter {


    void save(boolean updateCompletedOrderItem);

    void search(String key, String dateStart, String dateEnd);
}
