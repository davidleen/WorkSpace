package com.giants3.hd.entity;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品材料列表
 */
@Entity(name = "T_ProductMaterial")

@Table(
        indexes = {@Index(name = "materialIdIndex", columnList = "materialId", unique = false),
                @Index(name = "productIdIndex", columnList = "productId", unique = false)
                , @Index(name = "flowIdIndex", columnList = "flowId", unique = false)}
)

public class ProductMaterial implements Serializable, Summariable, Valuable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    /**
     * 产品id
     */
    @Basic
    public long productId;


    @Basic
    public int itemIndex;
    /**
     * 配给额度。  重要字段。
     */
    @Basic
    public float quota;
    /**
     * 材料id
     */
    @Basic
    public long materialId;

    /**
     * 材料编码
     */
    @Basic
    public String materialCode;

    /**
     * 材料名称
     */
    @Basic
    public String materialName;
    /**
     * 单位用量
     */
    @Basic
    public float quantity;
    /**
     * 材料规格长
     */
    @Basic
    public float mLong;
    /**
     * 材料规格 宽
     */
    @Basic
    public float mWidth;
    /**
     * 材料规格 高
     */
    @Basic
    public float mHeight;


    /**
     * 产品规格长
     */
    @Basic
    public float pLong;
    /**
     * 产品规格 宽
     */
    @Basic
    public float pWidth;
    /**
     * 产品规格 高
     */
    @Basic
    public float pHeight;
    /**
     * 毛料长度
     */
    @Basic
    public float wLong;
    /**
     * 毛料宽度
     */
    @Basic
    public float wWidth;
    /**
     * 毛料高度
     */
    @Basic
    public float wHeight;
    /**
     * 利用率
     */
    @Basic
    public float available;
    /**
     * 单位名称
     */
    @Basic
    public String unitName;
    /**
     * 材料类别   重要  参与定额计算
     */
    @Basic
    public int type;
    /**
     * 材料单价
     */
    @Basic
    public float price;
    /**
     * 金额字段  
     */
    @Basic
    public float amount;
    /**
     * 分件备注字段
     */
    @Basic
    public String memo;


    /**
     * 流程id
     */
    @Basic
    public long flowId;

    /**
     * 流程名称
     */
    @Basic
    public String flowName;






    /**
     * 副单位
     */
    public String unit2;
    /**
     * 副单价
     */
    public float price2;





    /**
     * 材质类型选择
     */
    @ManyToOne
    public PackMaterialType packMaterialType;


    /**
     * 包装材质位置选择  可选
     */
    @ManyToOne
    public PackMaterialPosition packMaterialPosition;


    /**
     * 包装材质大分类   当材质是包装类型  即  flowId=Flow.FLOW_PACK 时候 必填
     */
    @ManyToOne
    public PackMaterialClass packMaterialClass;


    /**
     * 材料分类 名称
     */
    @Basic
    public String className;


    /**
     * 材料分类id
     */
    @Basic
    public String classId;


    /**
     * 材料的换算单位  默认1；
     *
     * @return
     */
    @Basic
    public float unitRatio = 1;


    /**
     * 原材料材料规格
     */
    public String spec;

    /**
     * 板材类开法
     */
    public String cutWay;


    public PackMaterialType getPackMaterialType() {
        return packMaterialType;
    }

    public void setPackMaterialType(PackMaterialType packMaterialType) {
        this.packMaterialType = packMaterialType;
    }

    public PackMaterialPosition getPackMaterialPosition() {
        return packMaterialPosition;
    }

    public void setPackMaterialPosition(PackMaterialPosition packMaterialPosition) {
        this.packMaterialPosition = packMaterialPosition;
    }

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

    public float getQuota() {
        return quota;
    }

    public void setQuota(float quota) {
        this.quota = quota;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getmLong() {
        return mLong;
    }

    public void setmLong(float mLong) {
        this.mLong = mLong;
    }

    public float getmWidth() {
        return mWidth;
    }

    public void setmWidth(float mWidth) {
        this.mWidth = mWidth;
    }

    public float getmHeight() {
        return mHeight;
    }

    public void setmHeight(float mHeight) {
        this.mHeight = mHeight;
    }

    public float getwLong() {
        return wLong;
    }

    public void setwLong(float wLong) {
        this.wLong = wLong;
    }

    public float getwWidth() {
        return wWidth;
    }

    public void setwWidth(float wWidth) {
        this.wWidth = wWidth;
    }

    public float getwHeight() {
        return wHeight;
    }

    public void setwHeight(float wHeight) {
        this.wHeight = wHeight;
    }

    public float getAvailable() {
        return available;
    }

    public void setAvailable(float available) {
        this.available = available;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }


    public long getFlowId() {
        return flowId;
    }

    public void setFlowId(long flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public float getpLong() {
        return pLong;
    }

    public void setpLong(float pLong) {
        this.pLong = pLong;
    }

    public float getpWidth() {
        return pWidth;
    }

    public void setpWidth(float pWidth) {
        this.pWidth = pWidth;
    }

    public float getpHeight() {
        return pHeight;
    }

    public void setpHeight(float pHeight) {
        this.pHeight = pHeight;
    }


    public PackMaterialClass getPackMaterialClass() {
        return packMaterialClass;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductMaterial)) return false;

        ProductMaterial that = (ProductMaterial) o;

        if (id != that.id) return false;
        if (productId != that.productId) return false;
        if (Float.compare(that.quota, quota) != 0) return false;
        if (materialId != that.materialId) return false;
        if (Float.compare(that.quantity, quantity) != 0) return false;
        if (Float.compare(that.mLong, mLong) != 0) return false;
        if (Float.compare(that.mWidth, mWidth) != 0) return false;
        if (Float.compare(that.mHeight, mHeight) != 0) return false;
        if (Float.compare(that.pLong, pLong) != 0) return false;
        if (Float.compare(that.pWidth, pWidth) != 0) return false;
        if (Float.compare(that.pHeight, pHeight) != 0) return false;
        if (Float.compare(that.wLong, wLong) != 0) return false;
        if (Float.compare(that.wWidth, wWidth) != 0) return false;
        if (Float.compare(that.wHeight, wHeight) != 0) return false;
        if (Float.compare(that.available, available) != 0) return false;


        if (type != that.type) return false;
        if (Float.compare(that.price, price) != 0) return false;
        if (Float.compare(that.amount, amount) != 0) return false;
        if (flowId != that.flowId) return false;
        if (itemIndex != that.itemIndex) return false;
        if (Float.compare(that.unitRatio, unitRatio) != 0) return false;
        if (materialCode != null ? !materialCode.equals(that.materialCode) : that.materialCode != null) return false;
        if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
        if (unitName != null ? !unitName.equals(that.unitName) : that.unitName != null) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (flowName != null ? !flowName.equals(that.flowName) : that.flowName != null) return false;
        if (packMaterialType != null ? !packMaterialType.equals(that.packMaterialType) : that.packMaterialType != null)
            return false;
        if (packMaterialPosition != null ? !packMaterialPosition.equals(that.packMaterialPosition) : that.packMaterialPosition != null)
            return false;
        if (packMaterialClass != null ? !packMaterialClass.equals(that.packMaterialClass) : that.packMaterialClass != null)
            return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        return !(classId != null ? !classId.equals(that.classId) : that.classId != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (quota != +0.0f ? Float.floatToIntBits(quota) : 0);
        result = 31 * result + (int) (materialId ^ (materialId >>> 32));
        result = 31 * result + (materialCode != null ? materialCode.hashCode() : 0);
        result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
        result = 31 * result + (quantity != +0.0f ? Float.floatToIntBits(quantity) : 0);
        result = 31 * result + (mLong != +0.0f ? Float.floatToIntBits(mLong) : 0);
        result = 31 * result + (mWidth != +0.0f ? Float.floatToIntBits(mWidth) : 0);
        result = 31 * result + (mHeight != +0.0f ? Float.floatToIntBits(mHeight) : 0);
        result = 31 * result + (pLong != +0.0f ? Float.floatToIntBits(pLong) : 0);
        result = 31 * result + (pWidth != +0.0f ? Float.floatToIntBits(pWidth) : 0);
        result = 31 * result + (pHeight != +0.0f ? Float.floatToIntBits(pHeight) : 0);
        result = 31 * result + (wLong != +0.0f ? Float.floatToIntBits(wLong) : 0);
        result = 31 * result + (wWidth != +0.0f ? Float.floatToIntBits(wWidth) : 0);
        result = 31 * result + (wHeight != +0.0f ? Float.floatToIntBits(wHeight) : 0);
        result = 31 * result + (available != +0.0f ? Float.floatToIntBits(available) : 0);
        result = 31 * result + (unitName != null ? unitName.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + itemIndex;
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (amount != +0.0f ? Float.floatToIntBits(amount) : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        result = 31 * result + (int) (flowId ^ (flowId >>> 32));
        result = 31 * result + (flowName != null ? flowName.hashCode() : 0);
        result = 31 * result + (packMaterialType != null ? packMaterialType.hashCode() : 0);
        result = 31 * result + (packMaterialPosition != null ? packMaterialPosition.hashCode() : 0);
        result = 31 * result + (packMaterialClass != null ? packMaterialClass.hashCode() : 0);
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (unitRatio != +0.0f ? Float.floatToIntBits(unitRatio) : 0);

        return result;
    }

    @Override
    public boolean isEmpty() {


        //无设定材料  数据为空
        return (materialId <= 0);


    }


}