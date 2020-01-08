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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getpVersion() {
        return pVersion;
    }

    public void setpVersion(String pVersion) {
        this.pVersion = pVersion;
    }

    public int getInBoxCount() {
        return inBoxCount;
    }

    public void setInBoxCount(int inBoxCount) {
        this.inBoxCount = inBoxCount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPackQuantity() {
        return packQuantity;
    }

    public void setPackQuantity(int packQuantity) {
        this.packQuantity = packQuantity;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public float getBoxLong() {
        return boxLong;
    }

    public void setBoxLong(float boxLong) {
        this.boxLong = boxLong;
    }

    public float getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(float boxWidth) {
        this.boxWidth = boxWidth;
    }

    public float getBoxHeight() {
        return boxHeight;
    }

    public void setBoxHeight(float boxHeight) {
        this.boxHeight = boxHeight;
    }

    public float getVolumePerBox() {
        return volumePerBox;
    }

    public void setVolumePerBox(float volumePerBox) {
        this.volumePerBox = volumePerBox;
    }

    public float getWeightPerBox() {
        return weightPerBox;
    }

    public void setWeightPerBox(float weightPerBox) {
        this.weightPerBox = weightPerBox;
    }

    public float getAmountSum() {
        return amountSum;
    }

    public void setAmountSum(float amountSum) {
        this.amountSum = amountSum;
    }

    public float getVolumeSum() {
        return volumeSum;
    }

    public void setVolumeSum(float volumeSum) {
        this.volumeSum = volumeSum;
    }

    public float getWeightSum() {
        return weightSum;
    }

    public void setWeightSum(float weightSum) {
        this.weightSum = weightSum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceOrigin() {
        return priceOrigin;
    }

    public void setPriceOrigin(float priceOrigin) {
        this.priceOrigin = priceOrigin;
    }

    public float getVolumeSize() {
        return volumeSize;
    }

    public void setVolumeSize(float volumeSize) {
        this.volumeSize = volumeSize;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getConstitute() {
        return constitute;
    }

    public void setConstitute(String constitute) {
        this.constitute = constitute;
    }

    public String getMirrorSize() {
        return mirrorSize;
    }

    public void setMirrorSize(String mirrorSize) {
        this.mirrorSize = mirrorSize;
    }

    public long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(long quotationId) {
        this.quotationId = quotationId;
    }

    public int getItm() {
        return itm;
    }

    public void setItm(int itm) {
        this.itm = itm;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
