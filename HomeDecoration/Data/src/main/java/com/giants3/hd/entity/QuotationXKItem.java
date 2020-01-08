package com.giants3.hd.entity;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 咸康报价明细列表
 */
@Entity(name="T_QuotationXKItem")
public class QuotationXKItem implements Serializable,Valuable {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    /**
     *
     */
    @Basic
    public long productId=-1;


    /**
     */
    @Basic
    public String productName;

//    @Lob  @Basic
//    public byte[]  productPhoto;


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
     * 单价
     */
    @Basic
    public float price;



    /**
     * 立方数
     */
    @Basic
    public float volumeSize;
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








    @Basic @Lob

    public String memo;


/**
 * 以下是第二个货号属性  普通包装 --》  加强包装
 */


    /**
     *
     */
    @Basic
    public long productId2=-1;


    /**
     *
     */
    @Basic
    public String productName2;

//    @Lob  @Basic
//    public byte[]  productPhoto2;


    @Basic
    public String pVersion2;

    @Basic
    public int  inBoxCount2;


    /**
     * 包装箱数
     */
    @Basic
    public int packQuantity2;


    /**
     * 箱子规格
     */
    @Basic
    public String packageSize2;


    /**
     * 单位
     */
    @Basic
    public String unit2;

    /**
     * 成本价
     */
    @Basic
    public float cost2;


    /**
     * 单价
     */
    @Basic
    public float price2;



    /**
     * 立方数
     */
    @Basic
    public float volumeSize2;
    /**
     *净重
     */
    @Basic
    public float weight2;



    /**
     *货品规格
     */
    @Basic
    public String spec2;



    /**
     *材质
     */
    @Basic
    public String constitute2;


    /**
     *镜面尺寸
     */
    @Basic
    public String mirrorSize2;








    @Basic @Lob

    public String memo2;















    @Basic
    public long quotationId;


    /**
     * 序号， 表示顺序 不参与equals
     */
    @Basic
    public int iIndex;




    public String thumbnail;

    @Basic
    public String photoUrl;
    public String thumbnail2;
    @Basic
    public String photo2Url;


    @Override
    public boolean isEmpty() {
        return productId<=0&&productId2<=0 ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuotationXKItem)) return false;

        QuotationXKItem item = (QuotationXKItem) o;

        if (id != item.id) return false;
        if (productId != item.productId) return false;
        if (inBoxCount != item.inBoxCount) return false;
        if (packQuantity != item.packQuantity) return false;
        if (Float.compare(item.cost, cost) != 0) return false;
        if (Float.compare(item.cost_price_ratio, cost_price_ratio) != 0) return false;
        if (Float.compare(item.price, price) != 0) return false;
        if (Float.compare(item.volumeSize, volumeSize) != 0) return false;
        if (Float.compare(item.weight, weight) != 0) return false;
        if (productId2 != item.productId2) return false;
        if (inBoxCount2 != item.inBoxCount2) return false;
        if (packQuantity2 != item.packQuantity2) return false;
        if (Float.compare(item.cost2, cost2) != 0) return false;
        if (Float.compare(item.price2, price2) != 0) return false;
        if (Float.compare(item.volumeSize2, volumeSize2) != 0) return false;
        if (Float.compare(item.weight2, weight2) != 0) return false;
        if (quotationId != item.quotationId) return false;
        if (productName != null ? !productName.equals(item.productName) : item.productName != null) return false;
//        if (!Arrays.equals(productPhoto, item.productPhoto)) return false;
        if (pVersion != null ? !pVersion.equals(item.pVersion) : item.pVersion != null) return false;
        if (packageSize != null ? !packageSize.equals(item.packageSize) : item.packageSize != null) return false;
        if (unit != null ? !unit.equals(item.unit) : item.unit != null) return false;
        if (spec != null ? !spec.equals(item.spec) : item.spec != null) return false;
        if (constitute != null ? !constitute.equals(item.constitute) : item.constitute != null) return false;
        if (mirrorSize != null ? !mirrorSize.equals(item.mirrorSize) : item.mirrorSize != null) return false;
        if (memo != null ? !memo.equals(item.memo) : item.memo != null) return false;
        if (productName2 != null ? !productName2.equals(item.productName2) : item.productName2 != null) return false;
//        if (!Arrays.equals(productPhoto2, item.productPhoto2)) return false;
        if (pVersion2 != null ? !pVersion2.equals(item.pVersion2) : item.pVersion2 != null) return false;
        if (packageSize2 != null ? !packageSize2.equals(item.packageSize2) : item.packageSize2 != null) return false;
        if (unit2 != null ? !unit2.equals(item.unit2) : item.unit2 != null) return false;
        if (spec2 != null ? !spec2.equals(item.spec2) : item.spec2 != null) return false;
        if (constitute2 != null ? !constitute2.equals(item.constitute2) : item.constitute2 != null) return false;
        if (mirrorSize2 != null ? !mirrorSize2.equals(item.mirrorSize2) : item.mirrorSize2 != null) return false;
        return !(memo2 != null ? !memo2.equals(item.memo2) : item.memo2 != null);

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
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        result = 31 * result + (int) (productId2 ^ (productId2 >>> 32));
        result = 31 * result + (productName2 != null ? productName2.hashCode() : 0);
//        result = 31 * result + (productPhoto2 != null ? Arrays.hashCode(productPhoto2) : 0);
        result = 31 * result + (pVersion2 != null ? pVersion2.hashCode() : 0);
        result = 31 * result + inBoxCount2;
        result = 31 * result + packQuantity2;
        result = 31 * result + (packageSize2 != null ? packageSize2.hashCode() : 0);
        result = 31 * result + (unit2 != null ? unit2.hashCode() : 0);
        result = 31 * result + (cost2 != +0.0f ? Float.floatToIntBits(cost2) : 0);
        result = 31 * result + (price2 != +0.0f ? Float.floatToIntBits(price2) : 0);
        result = 31 * result + (volumeSize2 != +0.0f ? Float.floatToIntBits(volumeSize2) : 0);
        result = 31 * result + (weight2 != +0.0f ? Float.floatToIntBits(weight2) : 0);
        result = 31 * result + (spec2 != null ? spec2.hashCode() : 0);
        result = 31 * result + (constitute2 != null ? constitute2.hashCode() : 0);
        result = 31 * result + (mirrorSize2 != null ? mirrorSize2.hashCode() : 0);
        result = 31 * result + (memo2 != null ? memo2.hashCode() : 0);
        result = 31 * result + (int) (quotationId ^ (quotationId >>> 32));
        return result;
    }


    public String getFullProductName() {
        String pName=  !StringUtils.isEmpty(productName)?pVersion:productName2;
        String pVName=!StringUtils.isEmpty(pVersion)?pVersion:pVersion2;
        return  pName+(StringUtils.isEmpty(pVName)?"":("-"+pVName));
    }
}
