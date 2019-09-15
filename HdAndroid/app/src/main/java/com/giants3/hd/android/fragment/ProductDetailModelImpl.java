package com.giants3.hd.android.fragment;

import com.giants3.hd.android.mvp.NewModel;
import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.PClass;
import com.giants3.hd.entity.Pack;
import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.utils.StringUtils;

public class ProductDetailModelImpl implements ProductDetailModel {
    private ProductDetail productDetail;

    @Override
    public void setProductDetail(ProductDetail productDetail) {

        this.productDetail = productDetail;
    }
    @Override
    public void setNewPack(Pack newValue) {
        productDetail.product.pack = newValue;


    }
    @Override
    public void setPackQuantity(int newV) {
        productDetail.product.packQuantity = newV;
    }
    @Override
    public void setSpecCm(String newValue) {


        String spec = StringUtils.convertCmStringToInchString(newValue);
        productDetail.product.setSpecCm(newValue);
        productDetail.product.setSpec(spec);
    }

    @Override
    public void setFactory(Factory newValue) {



        productDetail.product.factoryCode=newValue.code;
        productDetail.product.factoryName=newValue.name       ;


    }

    @Override
    public void setNewPClass(PClass pClass) {

        productDetail.product.pClassId=pClass.id;
        productDetail.product.pClassName=pClass.name;

    }

    @Override
    public void setNewPackData(int insideBoxQuantity, int packQuantity, float packLong, float packWidth, float packHeight) {

        Product product = productDetail.product;
        product.insideBoxQuantity=insideBoxQuantity;
        product.packQuantity=packQuantity;
        product.packLong=packLong;
        product.packWidth=packWidth;
        product.packHeight=packHeight;

    }

    @Override
    public void setProductWeight(float newWeight) {
        productDetail.product.setWeight(newWeight);
    }
    @Override
    public void setProductPVersion(String newValue) {
        productDetail.product.setpVersion(newValue);
    }
    @Override
    public void setProductName(String newValue) {
        productDetail.product.setName(newValue);
    }
}
