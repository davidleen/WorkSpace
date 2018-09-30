package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ProductProcess;

/**
 * 工序相关接口 支持模型工序插入。
 */
public interface Processable {



    /**
     *   设置材料
     * @param process
     * @param rowIndex  模型行号
     */
    public
    void setProcess(ProductProcess process,int rowIndex);
}
