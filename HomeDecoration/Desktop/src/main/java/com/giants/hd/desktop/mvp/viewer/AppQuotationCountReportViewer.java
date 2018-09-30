package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.noEntity.RemoteData;

import java.util.Map;

/**
 * Created by davidleen29 on 2018/9/17.
 */
public interface AppQuotationCountReportViewer  extends IViewer{
    void bindData(RemoteData<Map> data);
}
