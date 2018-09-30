package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.ErpOrder;

/**
 *
 * 订单报表展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderReportIPresenter extends IPresenter {


    void search(long userId, String dateStart,String dateEnd, int pageIndex, int pageSize);

    void loadOrderDetail(ErpOrder erpOrder);

    /**
     * 导出报表
     */
    void export();
}
