package com.giants3.hd.entity;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 报价明细列表
 */
@Entity(name="T_QuotationItem")
public class QuotationItem implements Serializable,Valuable {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;





    /**
     *
     */
    @Basic
    public long productId=-1;


    /**
     *
     */
    @Basic
    public String productName;




    @Basic
    public String pVersion;

    @Basic
    public int  inBoxCount;


    /**
     * 包装箱数
     */
    @Basic
    public int packQuantity;


    /**
     * 箱子规格
     */
    @Basic
    public String packageSize;


    /**
     * 单位
     */
    @Basic
    public String unit;

    /**
     * 成本价
     */
    @Basic
    public float cost;

    /**
     * 成本利润比值
     */
    public float cost_price_ratio;

    /**
     * 单价（默认是fob值）可以通过修改成本利润比值调整
     */
    @Basic
    public float price;


    /**
     * 立方数
     */
    @Basic
    public float volumeSize;

    /**
     * 是否开模？
     */
    @Basic
    public boolean kd;
    /**
     *净重
     */
    @Basic
    public float weight;



    /**
     *货品规格
     */
    @Basic
    public String spec;



    /**
     *材质
     */
    @Basic
    public String constitute;


    /**
     *镜面尺寸
     */
    @Basic
    public String mirrorSize;


    @Basic
    public long quotationId;


    @Basic @Lob
    public String memo;


    /**
     * 序号字段   该字段目前不参与equals
     */
    @Basic
    public int  iIndex;

    @Basic
    public String thumbnail;
    @Basic
    public String photoUrl;


    @Override
    public boolean isEmpty() {
        return productId<=0||StringUtils.isEmpty(productName);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuotationItem)) return false;

        QuotationItem item = (QuotationItem) o;

        if (id != item.id) return false;
        if (productId != item.productId) return false;
        if (inBoxCount != item.inBoxCount) return false;
        if (packQuantity != item.packQuantity) return false;
        if (Float.compare(item.cost, cost) != 0) return false;
        if (Float.compare(item.cost_price_ratio, cost_price_ratio) != 0) return false;
        if (Float.compare(item.price, price) != 0) return false;
        if (Float.compare(item.volumeSize, volumeSize) != 0) return false;
        if (Float.compare(item.weight, weight) != 0) return false;
        if (quotationId != item.quotationId) return false;
        if (productName != null ? !productName.equals(item.productName) : item.productName != null) return false;
//        if (!Arrays.equals(productPhoto, item.productPhoto)) return false;
        if (pVersion != null ? !pVersion.equals(item.pVersion) : item.pVersion != null) return false;
        if (packageSize != null ? !packageSize.equals(item.packageSize) : item.packageSize != null) return false;
        if (unit != null ? !unit.equals(item.unit) : item.unit != null) return false;
        if (spec != null ? !spec.equals(item.spec) : item.spec != null) return false;
        if (constitute != null ? !constitute.equals(item.constitute) : item.constitute != null) return false;
        if (mirrorSize != null ? !mirrorSize.equals(item.mirrorSize) : item.mirrorSize != null) return false;
        return !(memo != null ? !memo.equals(item.memo) : item.memo != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
//        result = 31 * result + (productPhoto != null ? Arrays.hashCode(productPhoto) : 0);
        result = 31 * result + (pVersion != null ? pVersion.hashCode() : 0);
        result = 31 * result + inBoxCount;
        result = 31 * result + packQuantity;
        result = 31 * result + (packageSize != null ? packageSize.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (cost != +0.0f ? Float.floatToIntBits(cost) : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (volumeSize != +0.0f ? Float.floatToIntBits(volumeSize) : 0);
        result = 31 * result + (weight != +0.0f ? Float.floatToIntBits(weight) : 0);
        result = 31 * result + (spec != null ? spec.hashCode() : 0);
        result = 31 * result + (constitute != null ? constitute.hashCode() : 0);
        result = 31 * result + (mirrorSize != null ? mirrorSize.hashCode() : 0);
        result = 31 * result + (int) (quotationId ^ (quotationId >>> 32));
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }



    public String  getFullProductName()
    {
        return  productName+(StringUtils.isEmpty(pVersion)?"":("-"+pVersion));
    }
}
