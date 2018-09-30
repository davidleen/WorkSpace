package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 *
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderItemWorkFlowIPresenter extends IPresenter {


    void save();



    void reimportProduct();

    /**
     * 全部撤销排厂
     */
    void cancelArrange();
}
