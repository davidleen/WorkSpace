package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * 流程节点工作人员定位
 *
 *    Created by davidleen29 on 2015/7/1.
 *
 */
@Entity(name = "T_WorkFlowWorker")
public class WorkFlowWorker implements Serializable {

    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     * 流程
     */
    public String workFlowCode;
    /**
     * 流程
     */
    public int workFlowStep;

    /**
     *流程id
     */
    public String workFlowName;
    /**
     * 用户id
     */
    public long userId;
    /**
     * 用户name
     */
    public String  userName;

    public  boolean tie;
    public  boolean mu;
    public  boolean pu;


    public boolean receive;

    public boolean send;





    public int produceType;
    public String produceTypeName;


    /**
     * 限定只处理部分加工户的订单.
     */
    public String jghnames;/**
     * 限定只处理部分加工户的订单.
     */
    public String jghncodes;

}
