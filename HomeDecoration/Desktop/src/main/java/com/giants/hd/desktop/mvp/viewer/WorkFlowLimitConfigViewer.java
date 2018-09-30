package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.WorkFlowTimeLimit;

import java.util.List;

/**
 * 产品关联的生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowLimitConfigViewer extends IViewer {


    public void bindData(List<WorkFlowTimeLimit> workFlowWorkers);

}
