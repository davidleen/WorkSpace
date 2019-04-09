package com.giants3.hd.android.mvp.quotation;


import android.os.Bundle;

import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.android.mvp.PageModel;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.QuotationXKItem;
import com.giants3.hd.noEntity.QuotationDetail;

import java.util.List;

/**
 * 代办流程消息列表
 * Created by davidleen29 on 2017/5/23.
 */

public interface QuotationDetailMVP {


    interface Model extends PageModel<QuotationDetail> {


    }

    interface Presenter extends NewPresenter<QuotationDetailMVP.Viewer> {

          void init(Quotation quotation);
          void loadData();

        void loadProductAndCalculatePrice(QuotationXKItem o, float newRatio, ItemListAdapter<QuotationXKItem> quotationItemItemListAdapter);
        void loadProductAndCalculatePrice(QuotationItem o, float newRatio, ItemListAdapter<QuotationItem> quotationItemItemListAdapter);

        void verify();


        void unVerify();

        boolean isVerifiedQuotation();
    }

    interface Viewer extends NewViewer {


        void bindData(QuotationDetail data);

        void showUnSaveAlert();







        void updateListData();

    }
}
