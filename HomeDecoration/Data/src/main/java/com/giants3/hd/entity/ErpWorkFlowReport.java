package com.giants3.hd.entity;

import com.giants3.hd.noEntity.WorkFlowReportSummary;

import javax.persistence.*;

/**流程报告数据
 * Created by davidleen29 on 2017/2/28.
 */
@Entity(name="T_ErpWorkFlowReport")
public class ErpWorkFlowReport {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String workFlowCode;
    public int workFlowStep;
    public String workFlowName;
    public String osNo;
    public String prdNo;
    public int itm;
    public String pVersion;
    /**
     * 完成百分比。
     */
    public float percentage;


    /**
     * 当前流程指令单类型数量
     */
    public int typeCount;


    public  int produceType;
    public  String produceTypeName;



    public int productType;
    public String productTypeName;




    /**
     * 订单货款类型  工期限制使用
     */
    public int orderItemType;
    /**
     * 订单货款名称
     * 非咸康新单
     * 非咸康翻单
     * 咸康新单
     * 咸康翻单
     * 家具类
     * 灯具类
     */
    public String orderItemTypeName;


    /**
     * 产品类型代码
     */
    public String idx1;

    /**
     * 预警日期
     * 生产工期
     * 开始日期
     * 结束日期
     * 是否超期
     * 是否进入预警期
     * 超期天数
     */
    /**
     * 预警日期  还剩下多少天时候报警
     */
    public int alertDay;
    /**
     * 期限日期
     */
    public int limitDay;
    public long startDate;
    public String startDateString;
    public long endDate;
    public String endDateString;

    public boolean isOverDue=false;


    /**
     * 超期天数
     */
    public int overDueDay;


    /**
     * 已发送未接收数量
     */
    public int sendingQty;


    @Transient
    public WorkFlowReportSummary summary;
}
