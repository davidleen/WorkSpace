package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *  erp材料数据 临时存放表
 */
 @Entity(name="T_PRDT_TEMP")
public class Prdt implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


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



    public boolean isNew;
    public boolean isPriceChange;
    public boolean isMemoChange;
    public boolean isDataChang;
}
