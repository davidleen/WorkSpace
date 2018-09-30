package com.giants3.hd.server.entity;

/**
 *
 * 产品 流程 定义表      流程X排厂类型
 * Created by davidleen29 on 2017/4/1.
 */
public class ProductWorkFlowRelation {


    public long  id;

    public long workFlowId;
    public String workFlowName;


    public long arrangeTypeId;
    public String arrangeTypeName;



    public  long productId;

    public  String productName;
    public String pVersion;



}
