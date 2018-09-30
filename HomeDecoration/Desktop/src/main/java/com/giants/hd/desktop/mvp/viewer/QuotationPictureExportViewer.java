package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.Product;

import java.util.List;

/**
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface QuotationPictureExportViewer extends IViewer {


    void showProducts(List<Product> products);
}
