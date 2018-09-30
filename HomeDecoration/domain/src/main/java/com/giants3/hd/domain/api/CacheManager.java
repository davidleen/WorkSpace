package com.giants3.hd.domain.api;

import com.giants3.hd.noEntity.BufferData;

/**
 * Created by davidleen29 on 2015/8/8.
 */
public class CacheManager {
    private static CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private CacheManager() {
    }


    public  BufferData bufferData;

    /**
     * 是否查看订单单价
     * @return
     */
    public boolean isOrderPriceVisible()
    {
        if(bufferData==null||bufferData.orderAuth==null) return false;

       return  bufferData.orderAuth.fobVisible;
    }

    /**
     * 是否可以查看出库单单价
     * @return
     */
    public boolean isStockOutPriceVisible() {
        if(bufferData==null||bufferData.stockOutAuth==null) return false;
        return bufferData.stockOutAuth.fobVisible;

    }
}
