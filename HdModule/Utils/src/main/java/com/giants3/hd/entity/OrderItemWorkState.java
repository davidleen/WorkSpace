package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单货款的生产进度数据
 * Created by davidleen29 on 2017/1/1.
 */
@Entity(name="T_OrderItemWorkState")
public class OrderItemWorkState {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    public String osNo;
    public int itm;

    /**
     *产品进度描述
     */
    public String workFlowDescribe;




    /**
     * 订单状态 {@link ErpWorkFlow#STATE_COMPLETE}
     * 订单状态 {@link ErpWorkFlow#STATE_WORKING}
     */
    public int workFlowState;


    /**
     * 当前执行到的流程序号
     */
    public int maxWorkFlowStep;
    public String maxWorkFlowName;

    public String maxWorkFlowCode;


    /**
     * 当前流程的超期时间
     */
    public int currentOverDueDay;



    public String url;

    public String prdNo;
    /**
     * 预计流程总计提前或者 超期完成时间  负数表示提前  正数表示超期
     */
    public int totalLimit;
    /**
     * 当前流程期限
     */
    public int currentLimitDay;
    /**
     * 当前流程预警时间
     */
    public int currentAlertDay;


    /**
     * 1当前订单流程
     * 2当前流程开始日期
     * 3当前流程工期
     * 4 当前预警日期
     * 5是否已经进入预警期
     * 6结束日期
     * 7是否超期
     */

}
