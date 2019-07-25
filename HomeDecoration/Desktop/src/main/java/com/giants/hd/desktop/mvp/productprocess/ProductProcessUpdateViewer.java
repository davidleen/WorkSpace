package com.giants.hd.desktop.mvp.productprocess;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ProductProcess;

public interface ProductProcessUpdateViewer extends IViewer {

    void bindData(ProductProcess productProcess);

}
