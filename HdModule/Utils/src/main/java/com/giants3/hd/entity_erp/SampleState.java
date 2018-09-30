package com.giants3.hd.entity_erp;

/**
 *
 * 样品状态
 * Created by davidleen29 on 2017/11/9.
 */
public class SampleState {

    public String  ltime;
    /**
     * 状态  LN  借出  LB 在库
     */
    public String BL_ID;
    public String factory;
    public String   wareHouse;
    public String prdNo;
    public String pVersion;


    /** 是否有背板信息
     */
    public String bb;

    /**
     * 背板编号
     */
    public String ypbb;




    public static final String BB_TRUE="T";
    public static final String BB_FALSE="F";
    public static final String STATE_LN="LN";
    public static final String STATE_LB="LB";
    public static final String STATE_PC="PC";
}
