package com.giants3.hd.entity;

import com.giants3.hd.noEntity.ProduceType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单货款的生产进度数据
 * Created by davidleen29 on 2017/1/1.
 */
@Entity(name="T_OrderItemWorkFlow")
public class OrderItemWorkFlow {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    public long orderItemId;
    public long orderId;
    public String orderName;

    public String productFullName;
    public int orderItemIndex;

    String bat_no;




    public String workFlowSteps;
    public String workFlowNames;
    /**
     * 0，0，1；0，1，1  格式， 表示对应的类型是否配置改流程  {@see productTypes} 0,0,0 不经过该流程
     */
    public String configs;
    /**
     * 二级流程配置  铁or木orPU 或者任意组合。 ；隔开
     * <p/>
     * 流程类型，如果是细分类型。 1，2，3 形式
     */
    public  String productTypes;
    public  String productTypeNames;


    /**
     *    胚体产品类型对应的厂家ids  加工户
     */
    public String conceptusFactoryIds;
    /**
     *  胚体产品类型对应的厂家名称 加工户
     */
    public String conceptusFactoryNames;



    /**
     *产品进度描述
     */
    public String workFlowDiscribe;



    /**
     *  生产方式
     *  @see  ProduceType
     *
     */
    public int produceType;

    /**
     * 生产厂家  外厂 ，自制为空
     */
    public String produceFactoryId;
    /**
     * 生产厂家  外厂 ，自制为空
     */
    public String produceFactoryName;


    /**
     * 汇总节点
     */
    public int groupStep;

}
