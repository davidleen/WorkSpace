package com.giants3.hd.entity.app;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 移动端使用的数据 报价
 * Created by david on 2016/1/2.
 */
@Entity(name = "T_AppQuotation")
public class Quotation implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String qNumber;


    public long saleId;
    public String salesman;
    public String vDate;
    public String qDate;
    public long createTime;

    @Lob
    public String memo;
    public long customerId;
    public String customerCode;
    public String customerName="";
    public String customerAddress="";

    /**
     * 货柜类型
     */
    public String container;

    /**
     * 运价
     */
    public float freight;

    /**
     * 是否正式的报价单 即保存过的
     */
    public boolean formal;


    /**
     * 是否打印
     */
    public boolean printed;

    /**
     * 总数量
     */
    public int totalQuantity;
    /**
     * 总箱数
     */
    public int totalBoxCount;

    /**
     * 总体积
     */
    public float totalVolume;
   /**
     * 总金额
     */
    public float totalAmount;

    /**
     * 货柜剩余容量
     */
    public float containerRemainVolume;


    /**
     * 货柜使用数量
     */
    public int containerCount;

    /**
     * 总净重
     */
    public float totalWeight;


    /**
     * 总款数
     */

    public int itemCount;

    /**
     * 业务员email
     */
    public String email;

    /**
     * 唯一标识，不同数据库同步用。
     */
    public String uuid;


    /**
     * 展位号
     */
    public String booth="";
}
