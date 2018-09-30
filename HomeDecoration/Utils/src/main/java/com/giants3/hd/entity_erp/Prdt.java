package com.giants3.hd.entity_erp;

import java.io.Serializable;

/**
 *  erp 系统货品数据
 */
//@Entity(name="PRDT")
public class Prdt implements Serializable{



//    @Id

    public String prd_no;
    public String name;
    public String ut;
    public String knd;
    public String spec;
    public String rem;

    public float price;
    /**
     * 停用日期
     */
    public long nouse_dd;





    public float wLong;
    public float wWidth;
    public float wHeight;
    public float available=1;
    public float discount=0;
    public int type;



    public String classId;
    public String className;
}
