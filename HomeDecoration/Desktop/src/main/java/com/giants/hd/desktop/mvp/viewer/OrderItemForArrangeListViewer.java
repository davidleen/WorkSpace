package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItem;

/**
 * Created by davidleen29 on 2017/4/19.
 */
public interface OrderItemForArrangeListViewer extends IViewer {
    void setData(RemoteData<ErpOrderItem> orderReportItemRemoteData);
}
