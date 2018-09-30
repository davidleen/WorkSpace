package com.giants3.hd.entity;

import com.giants3.hd.noEntity.ProductType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 流程工期設定数据
 * Created by davidleen29 on 2017/12/24.
 */
@Entity(name = "T_WorkFlowTimeLimit")
public class WorkFlowTimeLimit {
    private static final String FANDAN = "翻单";
    private static final String XINDAN = "翻单";
    private static final String CUS_308 = "308";
    private static final String CODE_DENGJU = "mj07";
    private static final String CODE_JIAJU = "mj06";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     * 订单货款类型
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
//     * 流程step  这个值 有  mu/tie    <<3  + workflowstep 组成
//     */
//
//    public int combinedWorkFlowStep;
//
//    /**
//     * 白胚(木)  白胚（tie） 颜色（木）  颜色（铁） 组装包装
//     */
//    public String combinedWorkFlowName;

    /**
     *  白胚加工(木) 流程限制時間
     */
    public int limit_mu_baipeijg;
    /**
     *  白胚加工(木) 流程预警時間
     */
    public int alert_mu_baipeijg;

    /**
     * 白胚加工（tie）流程限制時間
     */
    public int limit_tie_baipeijg;

    /**
     * 白胚加工（tie）流预警時間
     */
    public int alert_tie_baipeijg;




    /**
     *  白胚(木) 流程限制時間
     */
    public int limit_mu_baipei;
    /**
     *  白胚(木) 流程预警時間
     */
    public int alert_mu_baipei;

    /**
     * 白胚（tie）流程限制時間
     */
    public int limit_tie_baipei;

    /**
     * 白胚（tie）流预警時間
     */
    public int alert_tie_baipei;
    /**
     * 颜色（木）流程限制時間
     */
    public int limit_mu_yanse;
    /**
     * 颜色（木）流程预警時間
     */
    public int alert_mu_yanse;
    /**
     * 颜色（铁）流程限制時間
     */
    public int limit_tie_yanse; /**
     * 颜色（铁）流程预警時間
     */
    public int alert_tie_yanse;
    /**
     * 组装包装 木件 流程限制時間
     */
    public int limit_baozhuang;

/**
     * 组装包装  木件  流程预警時間
     */
    public int  alert_baozhuang;
    /**
     * 组装包装 铁件 流程限制時間
     */
    public int limit_tie_baozhuang;

/**
     * 组装包装 铁件 流程预警時間
     */
    public int  alert_tie_baozhuang;





    public static int Order_ITEM_TYPE_JIAJU = 1;
    public static String Order_ITEM_TYPE_NAME_JIAJU = "家具类";
    public static int Order_ITEM_TYPE_DENGJU = 2;
    public static String Order_ITEM_TYPE_NAME_DENGJU = "灯具类";
    public static int Order_ITEM_TYPE_XIANKANG = 3;
    public static String Order_ITEM_TYPE_NAME_XIANKANG = "咸康翻单";
    public static int Order_ITEM_TYPE_XIANKANG_NEW = 4;
    public static String Order_ITEM_TYPE_NAME_XIANKANG_NEW = "咸康新单";
    public static int Order_ITEM_TYPE_NORMAL = 5;
    public static String Order_ITEM_TYPE_NAME_NORMAL = "非咸康翻单";
    public static int Order_ITEM_TYPE_NORMAL_NEW = 6;
    public static String Order_ITEM_TYPE_NAME_NORMAL_NEW = "非咸康新单";

    public static final int[] ORDER_ITEM_TYPES = new int[]{
            Order_ITEM_TYPE_JIAJU,
            Order_ITEM_TYPE_DENGJU,
            Order_ITEM_TYPE_XIANKANG,
            Order_ITEM_TYPE_XIANKANG_NEW,
            Order_ITEM_TYPE_NORMAL,
            Order_ITEM_TYPE_NORMAL_NEW

    };
    public static final String[] ORDER_ITEM_TYPE_NAMES = new String[]{
            Order_ITEM_TYPE_NAME_JIAJU,
            Order_ITEM_TYPE_NAME_DENGJU,
            Order_ITEM_TYPE_NAME_XIANKANG,
            Order_ITEM_TYPE_NAME_XIANKANG_NEW,
            Order_ITEM_TYPE_NAME_NORMAL,
            Order_ITEM_TYPE_NAME_NORMAL_NEW

    };


    public static int COMBINED_WORK_FLOW_STEP_MU_BAIPEI = ProductType.TYPE_MU << 16 | ErpWorkFlow.STEP_PEITI;

    public static String COMBINED_WORK_FLOW_NAME_MU_BAIPEI = ProductType.TYPE_MU_NAME + ErpWorkFlow.NAME_BAIPEI;

    public static int COMBINED_WORK_FLOW_STEP_TIE_BAIPEI = ProductType.TYPE_TIE << 16 | ErpWorkFlow.STEP_PEITI;
    public static String COMBINED_WORK_FLOW_NAME_TIE_BAIPEI = ProductType.TYPE_TIE_NAME + ErpWorkFlow.NAME_BAIPEI;

    public static int COMBINED_WORK_FLOW_STEP_MU_YANSE = ProductType.TYPE_MU << 16 | ErpWorkFlow.STEP_YANSE;
    public static String COMBINED_WORK_FLOW_NAME_MU_YANSE = ProductType.TYPE_MU_NAME + ErpWorkFlow.NAME_YANSE;

    public static int COMBINED_WORK_FLOW_STEP_TIE_YANSE = ProductType.TYPE_TIE << 16 | ErpWorkFlow.STEP_YANSE;
    public static String COMBINED_WORK_FLOW_NAME_TIE_YANSE = ProductType.TYPE_TIE_NAME + ErpWorkFlow.NAME_YANSE;


    public static int COMBINED_WORK_FLOW_STEP_BAOZHUANG = ErpWorkFlow.STEP_BAOZHUANG;
    public static String COMBINED_WORK_FLOW_NAME_BAOZHUANG = ErpWorkFlow.NAME_BAOZHUANG;


    public static final int[] COMBINED_WORK_FLOW_STEPS = new int[]
            {
                    COMBINED_WORK_FLOW_STEP_MU_BAIPEI
                    , COMBINED_WORK_FLOW_STEP_TIE_BAIPEI
                    , COMBINED_WORK_FLOW_STEP_MU_YANSE
                    , COMBINED_WORK_FLOW_STEP_TIE_YANSE
                    , COMBINED_WORK_FLOW_STEP_BAOZHUANG
            };

    public static final String[] COMBINED_WORK_FLOW_NAMES = new String[]
            {
                    COMBINED_WORK_FLOW_NAME_MU_BAIPEI
                    , COMBINED_WORK_FLOW_NAME_TIE_BAIPEI
                    , COMBINED_WORK_FLOW_NAME_MU_YANSE
                    , COMBINED_WORK_FLOW_NAME_TIE_YANSE
                    , COMBINED_WORK_FLOW_NAME_BAOZHUANG
            };


    public static class OrderItemType
    {
        public int orderItemType;
        public String orderItemTypeName;
        /**
         * 产品类型  mj tj 开头  分别表示木件  铁件
         */
        public String idx1;
    }



    public static OrderItemType findOrderItemTypeForTimeLimit(String scsx ,String cus_no,String idx1 )
    {
        WorkFlowTimeLimit.OrderItemType orderItemType=new  WorkFlowTimeLimit.OrderItemType();
        orderItemType.idx1=idx1;
        if(CODE_JIAJU.equalsIgnoreCase(idx1))
        {
            orderItemType.orderItemType=WorkFlowTimeLimit.Order_ITEM_TYPE_JIAJU;
            orderItemType.orderItemTypeName=WorkFlowTimeLimit.Order_ITEM_TYPE_NAME_JIAJU;

        }else
        if(CODE_DENGJU.equalsIgnoreCase(idx1))
        {
            orderItemType.orderItemType=WorkFlowTimeLimit.Order_ITEM_TYPE_DENGJU;
            orderItemType.orderItemTypeName=WorkFlowTimeLimit.Order_ITEM_TYPE_NAME_DENGJU;
        }else
        if(CUS_308.equalsIgnoreCase(cus_no))
        {
            if(XINDAN.equals(scsx))
            {
                orderItemType.orderItemType=WorkFlowTimeLimit.Order_ITEM_TYPE_XIANKANG;
                orderItemType.orderItemTypeName=WorkFlowTimeLimit.Order_ITEM_TYPE_NAME_XIANKANG;
            }else
            {
                orderItemType.orderItemType=WorkFlowTimeLimit.Order_ITEM_TYPE_XIANKANG_NEW;
                orderItemType.orderItemTypeName=WorkFlowTimeLimit.Order_ITEM_TYPE_NAME_XIANKANG_NEW;
            }
        }else
        {
            if(FANDAN.equals(scsx))
            {
                orderItemType.orderItemType=WorkFlowTimeLimit.Order_ITEM_TYPE_NORMAL;
                orderItemType.orderItemTypeName=WorkFlowTimeLimit.Order_ITEM_TYPE_NAME_NORMAL;
            }else
            {
                orderItemType.orderItemType=WorkFlowTimeLimit.Order_ITEM_TYPE_NORMAL_NEW;
                orderItemType.orderItemTypeName=WorkFlowTimeLimit.Order_ITEM_TYPE_NAME_NORMAL_NEW;
            }

        }

        return orderItemType;


    }
}
