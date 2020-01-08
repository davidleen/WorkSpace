package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 节点事件工作人员配置表
 *
 *    Created by davidleen29 on 2015/7/1.
 *
 */
@Entity(name = "T_WorkFlowEventWorker")
public class WorkFlowEventWorker implements Serializable {

    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     * 流程
     */
    public long workFlowEventId;

    /**
     *流程id
     */
    public String workFlowEventName;
    /**
     * 用户id
     */
    public long userId;
    /**
     * 用户name
     */
    public String  userName;












}
