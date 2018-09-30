package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.WorkFlowArranger;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowArrangerIPresenter extends IPresenter {
    void addOne();

    void showOne(WorkFlowArranger workFlowWorker);
}
