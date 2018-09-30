package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowWorker;

import java.util.List;

/**
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowWorkerUpdateViewer extends IViewer {


    void bindWorkFlows(List<ErpWorkFlow> workFlows);

    void bindUsers(List<User> users);

    void bindData(WorkFlowWorker workFlowWorker);

    void getData(WorkFlowWorker workFlowWorker);
}
