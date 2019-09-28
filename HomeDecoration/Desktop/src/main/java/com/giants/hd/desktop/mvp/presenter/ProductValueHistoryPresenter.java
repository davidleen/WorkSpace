package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.ProductValueHistory;

public interface ProductValueHistoryPresenter  extends IPresenter {
    void findHistoryData(ProductValueHistory item);

    void search(String keyText, boolean isAccurate, int pageIndex, int pageSize);
}
