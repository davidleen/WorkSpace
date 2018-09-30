package com.giants3.hd.android.mvp.customer;


import com.giants3.hd.android.mvp.AppQuotationMVP;
import com.giants3.hd.android.mvp.PageModelImpl;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.noEntity.NameCard;

/**
 * Created by davidleen29 on 2016/10/10.
 */

public class ModelImpl extends PageModelImpl<Customer> implements CustomerEditMVP.Model {


    private Customer customer;

    private NameCard lastRequestNameCard;

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {

        this.customer = customer;
    }


    @Override
    public void bindNameCard(NameCard nameCard) {



        if(customer==null)
        {
            customer=new Customer();

        }

        customer.name=nameCard.name;
        customer.title=nameCard.title;
        customer.addr=nameCard.address;
        customer.tel=nameCard.telephone;
        customer.homePage=nameCard.url;
        customer.email=nameCard.email;
        customer.nation=nameCard.nation;
        customer.company=nameCard.company;
        customer.orignData=nameCard.orginData;
        customer.nameCardFileUrl=nameCard.pictureUrl;

        lastRequestNameCard=null;

    }

    @Override
    public NameCard getLastRequestNameCard() {
        return lastRequestNameCard;
    }

    @Override
    public void setLastRequestNameCard(NameCard nameCard) {
        lastRequestNameCard=nameCard;
    }
}
