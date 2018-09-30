package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.OrderAuth;
import com.giants3.hd.entity.User;

import java.util.List;

/**
 *
 *  订单 权限明细详情界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderAuthDetailViewer extends IViewer {

    /**
     * 显示关联的业务员
     * @param salesList
     */
    public void showAllSales(List<User> salesList);


    public void bindRelateSalesData(List<Integer> indexs);

    void showOrderAuthList(List<OrderAuth> orderAuths);
}
