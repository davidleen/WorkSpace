package com.giants3.hd.android.entity;

import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.Flow;
import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.entity.ProductPaint;
import com.giants3.hd.entity.ProductWage;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.ProductDetail;

import java.util.List;

/**
 * c产品信息 单例模式   整个应用只有一个产品处于 查看编辑状态
 * Created by david on 2016/4/13.
 */
public class ProductDetailSingleton {


    public static final int PRODUCT_MATERIAL_CONCEPTUS = 1;
    public static final int PRODUCT_MATERIAL_ASSEMBLE = 2;
    public static final int PRODUCT_MATERIAL_PAINT = 3;
    public static final int PRODUCT_MATERIAL_PACK = 4;
    private ProductDetail originProductDetail;
    private ProductDetail productDetail;

    private boolean isEdit;


    public static ProductDetailSingleton singleton = new ProductDetailSingleton();

    public static ProductDetailSingleton getInstance() {
        return singleton;

    }


    public void setProductDetail(ProductDetail productDetail) {
        if (productDetail == null) {
            originProductDetail = null;
        } else {

            try {
                originProductDetail = GsonUtils.fromJson(GsonUtils.toJson(productDetail), ProductDetail.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        this.productDetail = productDetail;

    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public boolean hasModifyDetail() {

        if (originProductDetail == null) return false;

        return !originProductDetail.equals(productDetail);
    }

    public ProductPaint getProductPaint(int position) {
        return productDetail.paints.get(position);
    }


    public ProductMaterial getProductMaterial(int materialType, int position) {


        switch (materialType) {
            case PRODUCT_MATERIAL_CONCEPTUS:
                return productDetail.conceptusMaterials.get(position);
            case PRODUCT_MATERIAL_PACK:
                return productDetail.packMaterials.get(position);

            case PRODUCT_MATERIAL_ASSEMBLE:
                return productDetail.assembleMaterials.get(position);


        }

        return null;
    }

    public ProductWage getProductWage(int materialType, int position) {


        switch (materialType) {
            case PRODUCT_MATERIAL_CONCEPTUS:
                return productDetail.conceptusWages.get(position);
            case PRODUCT_MATERIAL_PACK:
                return productDetail.packWages.get(position);

            case PRODUCT_MATERIAL_ASSEMBLE:
                return productDetail.assembleWages.get(position);

        }

        return null;
    }

    public int addNewProductMaterial(int materialType) {

        switch (materialType) {
            case PRODUCT_MATERIAL_CONCEPTUS: {
                ProductMaterial productMaterial = new ProductMaterial();
                productMaterial.flowId = Flow.FLOW_CONCEPTUS;
                productDetail.conceptusMaterials.add(productMaterial);
                return productDetail.conceptusMaterials.indexOf(productMaterial);
            }
            case PRODUCT_MATERIAL_PACK: {

                ProductMaterial productMaterial = new ProductMaterial();
                productMaterial.flowId = Flow.FLOW_PACK;


                productDetail.packMaterials.add(productMaterial);

                return productDetail.packMaterials.indexOf(productMaterial);
            }


            case PRODUCT_MATERIAL_ASSEMBLE: {
                ProductMaterial productMaterial = new ProductMaterial();
                productMaterial.flowId = Flow.FLOW_ASSEMBLE;
                productDetail.assembleMaterials.add(productMaterial);
                return productDetail.assembleMaterials.indexOf(productMaterial);
            }

            case PRODUCT_MATERIAL_PAINT: {
                ProductPaint productPaint = new ProductPaint();
                productPaint.flowId = Flow.FLOW_PAINT;
                productDetail.paints.add(productPaint);
                return productDetail.paints.indexOf(productPaint);
            }


        }
        return -1;

    }

    public void addNewProductWage(int materialType, int position) {
        ProductWage productWage = new ProductWage();
        List<ProductWage> productWages=null ;
        switch (materialType) {
            case PRODUCT_MATERIAL_CONCEPTUS: {

                productWage.flowId = Flow.FLOW_CONCEPTUS;
                productWages= productDetail.conceptusWages;

                break;
            }
            case PRODUCT_MATERIAL_PACK: {
                productWage.flowId = Flow.FLOW_PACK;
                productWages=productDetail.packWages ;
                break;

            }


            case PRODUCT_MATERIAL_ASSEMBLE: {

                productWage.flowId = Flow.FLOW_ASSEMBLE;
                productWages=productDetail.assembleWages;
                break;

            }





        }
        if(productWages!=null) {
            productWages.add(position, productWage);
            int index=0;
            for(ProductWage wage:productWages)
            {
                wage.itemIndex=index++;
            }
        }

    }

    public void deleteProductWage(int productMaterialType, int wagePosition) {
        switch (productMaterialType) {
            case PRODUCT_MATERIAL_CONCEPTUS: {

                productDetail.conceptusWages.remove(wagePosition);
                break;
            }
            case PRODUCT_MATERIAL_PACK: {

                productDetail.packWages.remove(wagePosition);
                break;

            }


            case PRODUCT_MATERIAL_ASSEMBLE: {

                productDetail.assembleWages.remove(wagePosition);
                break;

            }


        }


        ProductAnalytics.updateProductStatistics(productDetail, getGlobalData());
    }

    private GlobalData getGlobalData() {
        return SharedPreferencesHelper.getInitData().globalData;
    }


}
