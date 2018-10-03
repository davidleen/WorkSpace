package com.giants3.hd.android.mvp.searchcustomer;

import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.noEntity.RemoteData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/10/3.
 */

public class PresenterImpl  extends BasePresenter<SearchCustomerMvp.Viewer, SearchCustomerMvp.Model> implements SearchCustomerMvp.Presenter {

    private String keyString;

    @Override
    public void start() {


        UseCaseFactory.getInstance().createGetCustomerListUseCase( ).execute(new RemoteDataSubscriber<Customer>(this) {
            @Override
            protected void handleRemoteData(RemoteData data) {



                getModel().setRemoteData(data);
                if (data.isSuccess()) {
                    getView().bindDatas(data.datas);
                }

            }


        });
        getView().showWaiting();


    }

    @Override
    public void search() {


        List<Customer> customers=new ArrayList<>();
        List<Customer> datas = getModel().getDatas();
        if(datas==null) return;
        String keylower = keyString.toLowerCase();
        for(Customer customer: datas)
        {

            if(customer.name.toLowerCase().contains(keylower)||customer.code.toLowerCase().contains(keylower))
            {
                customers.add(customer);
            }
        }


        getView().bindDatas(customers);






    }

    @Override
    public void searchMore() {

    }

    @Override
    public boolean canSearchMore() {
        return false;
    }

    @Override
    public void setKeyWord(String s) {

        keyString = s;
    }

    @Override
    public SearchCustomerMvp.Model createModel() {
        return new ModelImpl();
    }
}
