package com.giants3.hd.noEntity;

import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.entity_erp.ErpStockOutItem;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 出库详情列表
 * Created by davidleen29 on 2016/7/18.
 */
public class ErpStockOutDetail   implements Serializable {

    public ErpStockOut erpStockOut;
    public List<ErpStockOutItem> items;

}
