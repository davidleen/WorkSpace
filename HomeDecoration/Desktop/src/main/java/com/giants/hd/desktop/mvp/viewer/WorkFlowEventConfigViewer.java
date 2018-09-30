package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.WorkFlowArea;
import com.giants3.hd.entity.WorkFlowEvent;

import java.util.List;

/**
 * 产品关联的生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowEventConfigViewer extends IViewer {


    void bindData(List<WorkFlowEvent> datas);

    void bindArea(List<WorkFlowArea> areas);
}
