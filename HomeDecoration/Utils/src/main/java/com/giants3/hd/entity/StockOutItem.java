package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *   erp出库表  额外附加数据存在本系统的
 */
@Entity(name="T_StockOutItem")
public class StockOutItem implements Serializable {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    //出库单号
    @Basic
    public String ckNo;

    //出库列表序号
    @Basic
    public int itm;


    /**
     * 产品描述
     */
    @Basic
    public String  describe;


    /**
     * 柜号
     */
    @Basic
    public String  guihao;


    /**
     * 封签号
     */
    @Basic
    public String  fengqianhao;


    /**
     * 柜型号。
     */
    public String guixing;
    /**
     * 出库数量
     */
    public int stockOutQty;


    public boolean subRecord;

    public String psNo;


}
