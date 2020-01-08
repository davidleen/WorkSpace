package com.giants3.hd.entity;

import javax.persistence.*;

/**
 * 材料类型（统计方面使用）
 */

@Entity(name="T_MaterialType")
public class MaterialType {




    /**
     *
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    /**
     * 类型编号
     */
    @Basic
    public int typeId;

    /**
     * 类型名称
     */
    @Basic
    public String typeName;


    @Override
    public String toString() {
        return  "  ["+typeId+"]    "+typeName;
    }
}
