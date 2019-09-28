package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ProductValueHistory;

public interface ProductValueHistoryViewer  extends IViewer {
    void bindData(RemoteData<ProductValueHistory> remoteData);
      void setSearchable(boolean showSearch);
}
