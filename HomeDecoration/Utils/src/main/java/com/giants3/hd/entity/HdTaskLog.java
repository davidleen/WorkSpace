package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  任务运行记录
 */
@Entity(name="T_HDTaskLog")
public class HdTaskLog implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * 任务id
     */
    public long taskId;

    /**
     * 任务类型名称
     */
    public String  taskTypeName;


    public long executeTime;
    public String executeTimeString;

    /**
     *   耗时 秒为单位
     */
    public long timeSpend;




    /**
     * 任务状态
     */
    public int state;


    /**
     * 状态名称
     */

    public String stateName;










    /**
     * 任务执行失败
     */
    public static final int STATE_FAIL=2;

    public static final int STATE_SUCCESS = 1;
    /**
     * 执行错误信息
     */
    public String errorMessage;
}
