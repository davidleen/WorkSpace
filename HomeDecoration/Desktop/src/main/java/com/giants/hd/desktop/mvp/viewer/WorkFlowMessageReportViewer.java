package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.WorkFlowArea;
import com.giants3.hd.entity.WorkFlowEvent;
import com.giants3.hd.entity.WorkFlowMessage;

import java.util.List;

/**
 *  消息 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowMessageReportViewer extends IViewer {


    void bindData(List<WorkFlowMessage> datas);


}
