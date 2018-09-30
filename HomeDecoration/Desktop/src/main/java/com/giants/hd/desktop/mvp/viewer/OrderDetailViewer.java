package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.ErpOrderDetail;

import java.util.List;

/**
 * 订单详情 界面展示接口
 * Created by davidleen29 on 2016/7/26.
 */
public interface OrderDetailViewer extends IViewer {

    /**
     * 显示订单数据
     * @param orderDetail
     */
    void setOrderDetail(ErpOrderDetail orderDetail);

    void setEditable(boolean b);

    void showAttachFiles(List<String> attachStrings);

    void setPriceVisible(boolean priceVisible);

    void setCanViewProduct(boolean b);
}
