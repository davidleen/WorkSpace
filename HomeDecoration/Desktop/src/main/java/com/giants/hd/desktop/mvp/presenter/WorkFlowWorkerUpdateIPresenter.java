package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowWorkerUpdateIPresenter extends IPresenter {
    void save();

    void delete();

    /**
     * 挑选配置加工户
     */
    void pickjgh();
}
