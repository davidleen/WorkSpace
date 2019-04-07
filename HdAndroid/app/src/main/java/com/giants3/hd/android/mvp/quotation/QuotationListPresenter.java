package com.giants3.hd.android.mvp.quotation;

import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.QuotationListMVP;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.noEntity.RemoteData;

import java.util.List;

public class QuotationListPresenter   extends BasePresenter<QuotationListMVP.Viewer, QuotationListMVP.Model> implements QuotationListMVP.Presenter {

    @Override
    public QuotationListMVP.Model createModel() {
        return new QuotationListModel();
    }

    @Override
    public void searchData() {

    }

    @Override
    public void loadMoreData() {

    }

    @Override
    public void setKey(String text) {

    }

    @Override
    public void start() {

    }
}
