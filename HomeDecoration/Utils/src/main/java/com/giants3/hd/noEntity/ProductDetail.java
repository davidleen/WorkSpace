package com.giants3.hd.noEntity;

import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品详细信息
 */
public class ProductDetail  implements Serializable {


    public ProductDetail() {

        product=new Product();
        product.xiankang=new Xiankang();
        conceptusMaterials=new ArrayList<>();
        assembleMaterials=new ArrayList<>();
        paints=new ArrayList<>();
        conceptusWages=new ArrayList<>();
        assembleWages=new ArrayList<>();
        packMaterials=new ArrayList<>();
        packWages=new ArrayList<>();

    }

    public Product product;


    /**
     * 记录表  该数据仅仅参与显示 不参与数据修改与否的比较
     */
    public ProductLog productLog;
    /**
     * 白胚材料列表
     */
    public List<ProductMaterial> conceptusMaterials;
    /**
     * 组装材料列表
     */
    public List<ProductMaterial> assembleMaterials;

    /**
     * 油漆材料工序列表
     */
    public  List<ProductPaint> paints;


    /**
     * 白胚工资列表
     */

    public List<ProductWage> conceptusWages;
    /**
     * 组装工资列表
     */
    public List<ProductWage> assembleWages;
    /**
     *
     *
     * 包装材料列表
     */
    public List<ProductMaterial> packMaterials;
    /**
     * 包装工资列表
     */
    public List<ProductWage> packWages;


    public List<Summariable> summariables=new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDetail)) return false;

        ProductDetail detail = (ProductDetail) o;

        if (product != null ? !product.equals(detail.product) : detail.product != null) return false;
        if (conceptusMaterials != null ? !conceptusMaterials.equals(detail.conceptusMaterials) : detail.conceptusMaterials != null)
            return false;
        if (assembleMaterials != null ? !assembleMaterials.equals(detail.assembleMaterials) : detail.assembleMaterials != null)
            return false;
        if (paints != null ? !paints.equals(detail.paints) : detail.paints != null) return false;
        if (conceptusWages != null ? !conceptusWages.equals(detail.conceptusWages) : detail.conceptusWages != null)
            return false;
        if (assembleWages != null ? !assembleWages.equals(detail.assembleWages) : detail.assembleWages != null)
            return false;
        if (packMaterials != null ? !packMaterials.equals(detail.packMaterials) : detail.packMaterials != null)
            return false;

        return !(packWages != null ? !packWages.equals(detail.packWages) : detail.packWages != null);

    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + (conceptusMaterials != null ? conceptusMaterials.hashCode() : 0);
        result = 31 * result + (assembleMaterials != null ? assembleMaterials.hashCode() : 0);
        result = 31 * result + (paints != null ? paints.hashCode() : 0);
        result = 31 * result + (conceptusWages != null ? conceptusWages.hashCode() : 0);
        result = 31 * result + (assembleWages != null ? assembleWages.hashCode() : 0);
        result = 31 * result + (packMaterials != null ? packMaterials.hashCode() : 0);
        result = 31 * result + (packWages != null ? packWages.hashCode() : 0);

        return result;
    }


    /**
     * 擦除相关记录id 信息 使其处于新增状态
     */
    public void swipe()
    {
        product.id=0;
        productLog=null;


        product.xiankang.setId(0);
        product.xiankang.xiankang_dengju.setId(0);
        product.xiankang.xiankang_jiaju.setId(0);
        product.xiankang.xiankang_jingza.setId(0);



        for(ProductMaterial productMaterial:conceptusMaterials)
        {
            productMaterial.productId=0;
            productMaterial.id=0;
        }


        for(ProductMaterial productMaterial:assembleMaterials)
        {
            productMaterial.productId=0;
            productMaterial.id=0;
        }

        for(ProductPaint productPaint:paints)
        {
            productPaint.productId=0;
            productPaint.id=0;
        }


        for(ProductWage productWage:conceptusWages)
        {
            productWage.productId=0;
            productWage.id=0;
        }

        for(ProductWage productWage:assembleWages)
        {
            productWage.productId=0;
            productWage.id=0;
        }

        for(ProductMaterial productMaterial:packMaterials)
        {
            productMaterial.productId=0;
            productMaterial.id=0;
        }

        for(ProductWage productWage:packWages)
        {
            productWage.productId=0;
            productWage.id=0;
        }

    }


}
