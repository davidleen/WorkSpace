package com.giants3.hd.android.mvp.customer;


import android.os.Bundle;

import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.android.mvp.PageModel;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.noEntity.NameCard;

import java.io.File;

/**
 * Created by davidleen29 on 2018/3/11.
 */

public interface CustomerEditMVP {


    interface Model extends PageModel<Customer> {


        boolean hasModify();
        Customer getCustomer();

        void setCustomer(Customer customer);


        void bindNameCard(NameCard nameCard);

        NameCard getLastRequestNameCard();

        void setLastRequestNameCard(NameCard nameCard);

        String getOriginData();

        void restoreCustomer(Customer customer, String oldData);
    }

    interface Presenter extends NewPresenter<Viewer> {


        void save();

        void updateValue(String codeText, String nameText, String telText, String faxText, String emailText, String addressText, String nationText);

        void initCustomer(Customer customer);

        void scanNameCard(File newPath);

        void retryLastRequest();

        void deleteCustomer();

        boolean restoreInstance(Bundle savedInstance);

        void saveInstance(Bundle savedInstance);
    }

    interface Viewer extends NewViewer {


        void finish();

        void bindData(Customer customer,NameCard lastRequestCard);
    }
}
