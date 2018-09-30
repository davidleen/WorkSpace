package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单表  额外附加数据存在本系统的
 */
@Entity(name = "T_Order")
public class Order implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    //订单单号
    @Basic
    public String osNo;


    /**
     * 业务员编号
     */
    @Basic
    public String sal_no;

    /**
     * 业务员
     */
    @Basic
    public String sal_name;
    /**
     * 业务员中文名
     */
    @Basic
    public String sal_cname;
    /**
     * 正唛
     */
    @Basic
    public String zhengmai;
    /**
     * 侧唛
     */
    @Basic
    public String cemai;
    /**
     * 内盒麦
     */
    @Basic
    public String neheimai;


    /**
     * 左麦
     */
    @Basic
    public String zuomai;
    /**
     * 右麦
     */
    @Basic
    public String youmai;
    /**
     * 备注
     */
    @Basic
    public String memo;
    /**
     * 附件列表
     */
    @Basic
    @Lob
    public String attaches;

    /**
     * 客户号
     */
    @Basic
    public String cus_no;

}
