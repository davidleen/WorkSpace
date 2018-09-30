package com.giants.hd.desktop.model;

import com.giants3.hd.entity.Product;

/**
 *
 * 支持设置product 的接口
 * Created by davidleen29 on 2015/7/1.
 */
public interface Productable {

    /**
     *   设置材料
     * @param product
     * @param rowIndex  模型行号
     */
    public    void setProduct(Product product, int rowIndex);
}
