package com.giants3.hd.entity;

import com.giants3.hd.noEntity.ProduceType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 与产品关联的订单流程数据
 * Created by davidleen29 on 2016/12/29.
 */
@Entity(name = "T_WorkFlowProduct")
public class WorkFlowProduct implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public long productId;
    public String productName;

    public String productPVersion;


    /**
     * 二级流程配置  铁or木orPU 或者任意组合。 ；隔开
     * <p/>
     * 流程类型，如果是细分类型。 1，2，3 形式
     */
    public String productTypes;
    public String productTypeNames;


    /**
     * 对应流程step列表  符号；隔开
     */
    public String workFlowSteps;
    /**
     * 对应流程name列表  符号；隔开
     */
    public String workFlowNames;
    /**
     * 0，0，1；0，1，1  格式， 表示对应的类型是否配置改流程  {@link WorkFlowArrangeData#productTypes} 0,0,0 表示不在细分， 需要流程汇总数据处理。
     * <p/>
     * 分号隔开的个数 与   {@link WorkFlowArrangeData#workFlowSteps} 一致
     * 逗号隔开个数 与    {@link WorkFlowArrangeData#productTypes}  一致
     */
    public String configs;

    /**
     *  生产方式
     *  @see  ProduceType
     *
     */
    public int produceType;


    /**
     * 汇总节点
     */
    public int groupStep;
}
