package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.OrderReportItem;

/**
 * 订单报表界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderReportViewer extends IViewer {

    void setData(RemoteData<OrderReportItem> erpOrderRemoteData);
}
