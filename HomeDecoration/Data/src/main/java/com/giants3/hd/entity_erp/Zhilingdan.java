package com.giants3.hd.entity_erp;

/**
 *
 * 制令单  状态
 * Created by davidleen29 on 2017/3/9.
 */
public class Zhilingdan {


    public String mo_dd;


    public String mo_no;

    public  String  prd_no;
    public  String  prd_name;

    public  String  real_prd_name;


    public String prd_mark;

    public int qty_rsv;

    public String  mrp_no;

    public String os_no;
    public String caigou_no;
    public int  caigouQty;
    public String caigou_dd;


    public String jinhuo_no;
    public int jinhuoQty;



    public String jinhuo_dd;


    /**
     * 进货超期时间限制 采购到进货
     */
    public float  need_days;
    /**
     * 采购超期时间限制 下单到采购
     */
    public int need_dd;


    public boolean isCaigouOverDue;
    public boolean isJinhuoOverDue;
}
