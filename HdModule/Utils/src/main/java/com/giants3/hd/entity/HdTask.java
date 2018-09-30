package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by david on 2015/12/10.
 */

/**
 * 任务定时
 */
@Entity(name="T_HDTask")
public class HdTask implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public int taskType;
    public String taskName;
    public long startDate;
    public String dateString;
    public String activator;
    public String activateTime;
    @Lob
    public String memo;




    /**
     * 重复次数
     * -2 每月
     *-1 每星期
     * 0 表示每天
     * 1-n 表示重复N次
     *
     */
    public int repeatCount;

    /**
     * 执行次数
     *
     */
    public int executeCount;








    public static final int TYPE_SYNC_ERP=100;
    public static final String NAME_SYNC_ERP="ERP材料同步";


    public static final int TYPE_UPDATE_ATTACH=101;
    public static final String NAME_UPDATE_ATTACH="附件图片迁移";

    public static final int TYPE_UPDATE_WORK_FLOW_STATE=102;
    public static final String NAME_UPDATE_WORK_FLOW_STATE="在产货款状态更新";




}
