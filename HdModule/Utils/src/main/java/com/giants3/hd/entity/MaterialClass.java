package com.giants3.hd.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 材料分类  根据编码头四位进行区分
 */
@Entity(name = "T_MaterialClass")
public class MaterialClass implements Serializable, Valuable {


    /**
     * 所有板材类型的编码规则数组  编码头四位
     */
    public static final String[] CLASS_BANGLEI_MU = new String[]{"0601", "0602", "0604", "0607"};
    public static final String[] CLASS_BANGLEI_TIE = new String[]{"0101", "0102", "0106"};
    /**
     * 所有板材类型的编码规则数组  编码头四位
     */
    public static final String[] CLASS_BANGLEI   ;
    static
    {
        int muLength=CLASS_BANGLEI_MU.length;
        int tieLength=CLASS_BANGLEI_TIE.length;


        CLASS_BANGLEI=new String[muLength+tieLength];
        for (int i = 0; i < muLength; i++) {
            CLASS_BANGLEI[i]=CLASS_BANGLEI_MU[i];
        }
        for (int i = 0; i < tieLength; i++) {
            CLASS_BANGLEI[muLength+i]=CLASS_BANGLEI_TIE[i];
        }
    }
    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    /**
     * 编码头四位（不含C）
     */
    @Basic

    public String code;


    /**
     * 毛长
     */
    @Basic
    public float wLong;

    /**
     * 毛长
     */
    @Basic
    public float wWidth;


    /**
     * 毛长
     */
    @Basic
    public float wHeight;


    /**
     * 损耗率
     */
    @Basic
    public float discount = 0;


    /**
     * 利用率  默认
     */
    @Basic
    public float available = 1;


    @Basic
    public int type;
    /**
     * 分类名称
     */
    @Basic
    public String name;


    /**
     * 备注说明
     */
    @Basic
    public String memo;

    @Override
    public String toString() {
        return "  [" + code + "]     " + name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MaterialClass)) return false;

        MaterialClass that = (MaterialClass) o;

        if (id != that.id) return false;
        if (Float.compare(that.wLong, wLong) != 0) return false;
        if (Float.compare(that.wWidth, wWidth) != 0) return false;
        if (Float.compare(that.wHeight, wHeight) != 0) return false;
        if (Float.compare(that.discount, discount) != 0) return false;
        if (Float.compare(that.available, available) != 0) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(memo != null ? !memo.equals(that.memo) : that.memo != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (wLong != +0.0f ? Float.floatToIntBits(wLong) : 0);
        result = 31 * result + (wWidth != +0.0f ? Float.floatToIntBits(wWidth) : 0);
        result = 31 * result + (wHeight != +0.0f ? Float.floatToIntBits(wHeight) : 0);
        result = 31 * result + (discount != +0.0f ? Float.floatToIntBits(discount) : 0);
        result = 31 * result + (available != +0.0f ? Float.floatToIntBits(available) : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(code) && StringUtils.isEmpty(name);
    }
}
