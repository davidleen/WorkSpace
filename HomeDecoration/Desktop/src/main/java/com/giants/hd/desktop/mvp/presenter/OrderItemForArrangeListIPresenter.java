package com.giants.hd.desktop.mvp.presenter;

import com.giants3.hd.entity.ErpOrderItem;

/**
 * Created by davidleen29 on 2017/4/19.
 */
public interface OrderItemForArrangeListIPresenter extends ListIPresenter<ErpOrderItem> {
    void search(String key, int pageIndex, int pageSize);
}
