package com.giants.hd.desktop.mvp.productprocess;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.ProductProcess;

public interface ProductProcessPresenter  extends IPresenter {
    void addOne();

    void updateItem(ProductProcess item);
}
