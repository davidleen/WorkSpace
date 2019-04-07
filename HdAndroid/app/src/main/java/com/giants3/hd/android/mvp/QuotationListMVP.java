package com.giants3.hd.android.mvp;


import com.giants3.hd.entity.Quotation;

import java.util.List;

/**
 * 估价报价
 * Created by davidleen29 on 2017/5/23.
 */

public interface QuotationListMVP {


    interface Model extends PageModel<Quotation> {


    }

    interface Presenter extends NewPresenter<QuotationListMVP.Viewer> {


        void searchData();

        void loadMoreData();

        void setKey(String text);


    }

    interface Viewer extends NewViewer {


        void bindData(List<Quotation> datas);
    }
}
