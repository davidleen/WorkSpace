package com.giants3.hd.entity;

import javax.persistence.*;

/**
 * 产品生产备注
 * Created by davidleen29 on 2017/1/14.
 */
@Entity(name="T_ProductWorkMemo")
public class ProductWorkMemo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;

    public String productName;
    public String pVersion;
    public String workFlowCode;
    public  String workFlowName;
    public int workFlowStep;


    @Lob
    public String memo;





    public long lastModifierId;
    public String lastModifierName;
    public String lastModifyTimeString;
    public  long  lastModifyTime;

}
