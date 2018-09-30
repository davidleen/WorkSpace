package com.giants3.hd.entity;

/**
 * Created by davidleen29 on 2016/11/20.
 */
public class StockSubmit {


    /**
     * 单号
     */
    public String no;

    /**
     * 产品图片url
     */
    public String url;

    /**
     * 日期
     */
    public String dd;
    /**
     * 订单号
     */
    public String so_no;

    /**
     * 订单项次
     */
    public int itm;
    /**
     * 产品编号
     */
    public String prd_no;
    /**
     * 产品名称
     */
    public String prd_name;
    /**
     * 产品备注
     */
    public String prd_mark;
    /**
     * bom清单id
     */
    public String id_no;
    /**
     * 客号
     */
    public String bat_no;
    /**
     * 客户订单号
     */
    public String cus_os_no;
    /**
     * 订单数量
     */
    public int qty;


    /**
     * 客户
     */
    public String cus_no;

    /**
     * 客户箱子规格
     */
    public String khxg;
    /**
     * 装箱数
     */
    public int so_zxs;
    /**
     * 箱子体积
     */
    public float xgtj;


    /**
     * 箱数   =  qty/so_zxs
     */
    public int xs;
    /**
     * 总体积  xgtj* xs
     */
    public float zxgtj;


    /**
     * 单价  每立方米
     */

    public float price = 0;


    /**
     * 类型  1本厂缴库  2外仓入库，  3出库到车柜
     */
    public int type;


    /**
     * 总价  总体积*单价
     */
    public float cost;


    /**
     * 类型对应的文字说明  1本厂缴库  2外仓入库，  3出库到车柜
     */
    public String area;


    /**
     * 货物传递的部门  本厂  外厂
     */
    public String dept;








    //以下字段 from  StockXiaoku


    /**
     * 第  柜
     */
    public float xhdg;


    /**
     * 封签
     */
    public String xhfq;
    ;

    /**
     * 柜号
     */
    public String xhgh;


    /**
     * 柜型号
     */
    public String xhgx;


    /**
     * 票号
     */
    public String xhph;


    /**
     * 拖车公司
     */
    public  String tcgs;
}
