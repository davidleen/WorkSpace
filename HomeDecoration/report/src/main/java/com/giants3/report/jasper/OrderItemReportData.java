package com.giants3.report.jasper;

import java.util.List;

/**
 * Created by david on 2016/3/26.
 */
public class OrderItemReportData {

    public String orderName;
    public String prdName;
    public String unit;
    public int itemQty;

    /**
     * 订单总金额
     */
    public float amount;

    /**
     * 单pc总费用
     */
    public float  pcAmount;

    /**
     * 材料费
     */
    public float materialCost;
/**
     * 工资
     */
    public float salary;


    /**
     * 制表日期
     */
    public String reportDate;
    /**
     * 成品交期
     */
    public  String deliverDate;

    /**
     * 图片路径
     */
    public String url;


    /**
     * 订单产品的颜色细项
     */
    public List<OrderProductPaint> paints;

}
