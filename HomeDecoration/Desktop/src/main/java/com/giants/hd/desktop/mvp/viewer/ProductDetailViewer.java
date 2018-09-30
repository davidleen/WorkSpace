package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.ProductDetail;

import java.util.List;

/**
 * Interface representing a View that will use to load data.
 */
public interface ProductDetailViewer extends IViewer {


    /**
     * 显示包装附件列表
     * @param attachStrings
     */
    void showPackAttachFiles(List<String> attachStrings);


          void setProductDetail(ProductDetail productDetail);
}
