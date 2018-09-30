package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单表细项目  额外附加数据存在本系统的
 */
@Entity(name = "T_OrderItem")
public class OrderItem implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    //订单单号
    @Basic
    public String osNo;

    //订单序号
    @Basic
    public int itm;


    /**
     * 验货日期
     */
    public String verifyDate;

    /**
     *  出柜日期
     */
    public String sendDate;

    /**
     * 包装信息
     */
    @Basic @Lob
    public String packageInfo;


    /**
     * 唛头
     */
    public String  maitou;


    /**
     * 挂钩说明
     */
    public String   guagou ;





    //系统数据 附加，方便报表生成
    public String batNo;
    public int qty;
    public String prdNo;
    public  String pVersion;

    public String thumbnail;
    public String url;
    public String ut;






}
