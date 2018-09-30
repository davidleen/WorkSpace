package com.giants3.hd.entity;

/**
 * 流程配置统一抽取数据， 产品 与 订单item 中 都有这个数据
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowArrangeData {




    public String workFlowSteps;
    public String workFlowNames;
    /**
     * 0，0，1；0，1，1  格式， 表示对应的类型是否配置改流程  {@link WorkFlowArrangeData#productTypes} 0,0,0 表示不在细分， 需要流程汇总数据处理。
     *
     * 分号隔开的个数 与   {@link WorkFlowArrangeData#workFlowSteps} 一致
     * 逗号隔开个数 与    {@link WorkFlowArrangeData#productTypes}  一致
     */
    public String configs;
    /**
     * 二级流程配置  铁or木orPU 或者任意组合。 ；隔开
     * <p/>
     * 流程类型，如果是细分类型。 1，2，3 形式
     */
    public  String productTypes;
    public  String productTypeNames;

}
