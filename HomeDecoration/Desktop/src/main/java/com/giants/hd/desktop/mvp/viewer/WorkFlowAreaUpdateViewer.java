package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.WorkFlowArea;


/**
 * Created by davidleen29 on 2017/6/17.
 */
public interface WorkFlowAreaUpdateViewer extends IViewer {


    void bindData(WorkFlowArea workFlowArea);

    void getData(WorkFlowArea data);
}