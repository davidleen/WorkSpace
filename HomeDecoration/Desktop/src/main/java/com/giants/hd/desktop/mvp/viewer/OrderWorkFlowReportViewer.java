package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpOrderItemProcess;

/**
 *
 *    订单生产流程报表界面层接口
 * Created by davidleen29 on 2016/9/25.
 */
public interface OrderWorkFlowReportViewer extends IViewer {

    void setData(RemoteData<ErpOrderItem> erpOrderRemoteData);

    void setUnCompleteData(RemoteData<ErpOrderItemProcess> remoteData);
}
