package com.giants.hd.desktop.frames;

import com.giants3.hd.entity.ErpOrder;

/**
* Created by david on 2016/3/29.
*/
public interface  OrderListAdapter
{
    public void search(String key, long salesId, int pageIndex, int pageSize);

    void loadOrderDetail(ErpOrder erpOrder);

}
