package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**每个流程节点上 的事项
 *
 * Created by davidleen29 on 2017/3/20.
 */
@Entity(name = "T_WorkFlowEvent")
public class WorkFlowEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String name;



    //流程节点信息
    public long workFlowId;
    public int workFlowStep;
    public String workFlowName;







}
