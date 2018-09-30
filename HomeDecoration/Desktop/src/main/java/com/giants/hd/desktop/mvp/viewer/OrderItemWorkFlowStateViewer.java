package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ErpOrderItemProcess;

import java.util.List;

/**
 *
 *   权限明细类界面展示曾
 * Created by davidleen29 on 2016/8/1.
 */
public interface OrderItemWorkFlowStateViewer extends IViewer {

    void setData(List<ErpOrderItemProcess> datas);
}
