package com.giants3.hd.entity_erp;

import java.io.Serializable;

/**
 * Erp 数据库出库数据
 * Created by davidleen29 on 2016/7/16.
 */
public class ErpStockOut   implements Serializable {


    public String ck_no;
    public String ck_dd;
    public String cus_no;
    public String mdg;
    public String tdh;
    public String gsgx;



    //业务员
    public String sal_no;
    //业员名字
    public String sal_name;
    //业员中文名
    public String sal_cname;
    //客户地址
    public String adr2;
    //客户电话
    public String tel1;
    //客户传真
    public String fax;


    /**
     * 客户名称
     */
    public String  name;

    /**
     * 发票客户名称
     */
    public String fp_name;

    //本系统增加字段

    public String zhengmai;
    public String cemai;
    public String neheimai;
    public String memo;

    public String attaches;



}
