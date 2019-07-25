package com.giants.hd.desktop.mvp.productprocess;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ProductProcess;

import java.util.List;

public interface ProductProcessViewer  extends IViewer {
    void setData(List<ProductProcess> datas);
}
