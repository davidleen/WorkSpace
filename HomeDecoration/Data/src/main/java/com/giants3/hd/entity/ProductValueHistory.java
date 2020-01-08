package com.giants3.hd.entity;

import javax.persistence.*;

@Entity(name="T_ProductValueHistory")
@Table(
        indexes = {@Index(name = "productIdIndex", columnList = "productId", unique = false),
                @Index(name = "productNameIndex", columnList = "name,pVersion", unique = false) }
)
public class ProductValueHistory {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;

    public String name;
    public String pVersion;
    public long productId;
    /**
     * 出口价
     */
    @Column(  precision = 50, scale = 2)
    public float  fob;

    /**
     * 出厂价
     */
    @Column(  precision = 50, scale = 2)
    public float price;
    /**
     * 成本
     */
    @Column(  precision = 50, scale = 2)
    public float cost;

    /**
     * 汇率
     */
    @Column(  precision = 50, scale = 2)
    public float exportRate;

    /**
     * 成本利润比值。
     */
    @Column(  precision = 50, scale = 2)
    public float cost_price_ratio=0.65f;



    /**
     * 咸康产品管理费用
     */
    @Column(  precision = 50, scale = 3)
    public float manageRatioXK=0.1f;


    /**
     * 普通产品管理费用
     */
    @Column(  precision = 50, scale = 3)
    public float manageRatioNormal=0.15f;


    /**
     * 附加值
     *
     */
    @Column(  precision = 50, scale = 2)
    public float  addition=0.15f;
    /**
     * 修改时间
     */
    public String dateString;
    public long dateTime;

    /**
     * 单位
     */
    public String unitName;

    /**
     * 更新方式
     */
    public String updateWay;
}
