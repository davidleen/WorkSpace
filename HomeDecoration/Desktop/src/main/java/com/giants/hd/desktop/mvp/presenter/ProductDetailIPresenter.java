package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.noEntity.ProductDetail;

import java.io.File;

/**
 * presenter layer of MVP  订单详情界面
 */
public interface ProductDetailIPresenter extends IPresenter {


    void addPackagePicture(File[] selectedFiles);

    void save(ProductDetail productDetail);

    void showProductWorkFlow();

    /**
     * 修复缩略图
     */
    void correctThumbnail(long productId);

    void correctProductStatistics();

    void viewValueHistory();
}
