package com.giants.hd.desktop.model;

import com.giants3.hd.entity.Material;

/**
 * 材料相关的接口，  表格的基本元素是material
 */
public interface Materialable {

    /**
     *   设置材料
     * @param material
     * @param rowIndex  模型行号
     */
    public
    void setMaterial(Material material,int rowIndex);
}
