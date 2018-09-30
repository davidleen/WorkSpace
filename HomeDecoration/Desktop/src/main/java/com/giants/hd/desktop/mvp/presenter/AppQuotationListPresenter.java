package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.WorkFlowWorker;
import com.giants3.hd.entity.app.Quotation;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface AppQuotationListPresenter extends IPresenter {
    void addOne();

    void showOne(Quotation quotation);

    void search(String key, String dateStart, String dateEnd, long id, int pageIndex, int pageSize);
}
