package com.giants3.hd.android.mvp.searchcustomer;

import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.android.mvp.PageModel;
import com.giants3.hd.entity.Customer;

import java.util.List;

/**
 * Created by davidleen29 on 2018/10/3.
 */

public class SearchCustomerMvp {


    interface Model extends PageModel<Customer> {



    }

    public interface Presenter extends NewPresenter<Viewer> {


        void search( );

        void searchMore();

        boolean canSearchMore();

        void setKeyWord(String s);


    }

    public interface Viewer extends NewViewer {


        void bindDatas(List<Customer> datas);
    }
}
