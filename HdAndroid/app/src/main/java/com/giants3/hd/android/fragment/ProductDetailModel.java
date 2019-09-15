package com.giants3.hd.android.fragment;

import com.giants3.hd.android.mvp.NewModel;
import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.PClass;
import com.giants3.hd.entity.Pack;
import com.giants3.hd.noEntity.ProductDetail;

public interface ProductDetailModel extends NewModel {
    void setProductDetail(ProductDetail productDetail);

    void setPackQuantity(int newV);

    void setProductWeight(float newWeight);

    void setProductPVersion(String newValue);

    void setProductName(String newValue);

    void setNewPack(Pack newValue);

    void setSpecCm(String newValue);

    void setFactory(Factory newValue);

    void setNewPClass(PClass pClass);

    void setNewPackData(int insideBoxQuantity, int packQuantity, float packLong, float packWidth, float packHeight);
}
