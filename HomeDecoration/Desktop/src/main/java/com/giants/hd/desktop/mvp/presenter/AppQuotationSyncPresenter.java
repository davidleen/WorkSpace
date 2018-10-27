package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.WorkFlowWorker;
import com.giants3.hd.entity.app.Quotation;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface AppQuotationSyncPresenter extends IPresenter {


    void beginAsync(String urlHead, String startDate, String endDate);

    void beginAsyncPicture(String remoteResource,String filterKey,boolean shouldOverride);

    void initData();

    void beginAsyncProduct(String remoteResource, String filterKey, boolean shouldOverride);
}
