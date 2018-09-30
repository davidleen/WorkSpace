package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.WorkFlowWorker;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowWorkerIPresenter extends IPresenter {
    void addOne();

    void showOne(WorkFlowWorker workFlowWorker);
}
