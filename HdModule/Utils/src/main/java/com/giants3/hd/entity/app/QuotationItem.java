package com.giants3.hd.entity.app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 移动端使用的数据 报价子项
 * Created by david on 2016/1/2.
 */
@Entity(name = "T_AppQuotationItem")
public class QuotationItem implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     *
     */

    public long productId = -1;


    /**
     *
     */

    public String productName;


    public String pVersion;

    /**
     * 箱数
     */

    public int inBoxCount;


    /**
     * 数量
     */
    public int qty;


    /**
     * 包装箱数
     */

    public int packQuantity;


    /**
     * 箱子规格
     */

    public String packageSize;



    public float	boxLong;

    public float boxWidth;

    public float boxHeight	;

    public float volumePerBox	;

    public  float weightPerBox;

    /**
     * 金额
     */
    public  float amountSum;
    /**
     * 总体积
     */
    public float  volumeSum;
    /**
     * 总净重
     */
    public float weightSum;



    /**
     * 单位
     */

    public String unit;

    /**
     * 成本价
     */

    public float cost;


    /**
     * 单价
     */

    public float price;

    /**
     * 单价
     */

    public float priceOrigin;


    /**
     * 立方数
     */

    public float volumeSize;
    /**
     * 净重
     */

    public float weight;


    /**
     * 货品规格
     */

    public String spec;


    /**
     * 材质
     */

    public String constitute;


    /**
     * 镜面尺寸
     */

    public String mirrorSize;


    public long quotationId;


    /**
     * 项次
     */
    public int itm;


    public String memo;


    /**
     * 缩略图
     */
    public String thumbnail;
    /**
     * 图片url
     */
    public String photoUrl;

}
