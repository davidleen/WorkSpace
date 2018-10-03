package com.giants3.hd.android.events;

import com.giants3.hd.entity.Customer;

/**
 * Created by davidleen29 on 2018/3/23.
 */

public class CustomerUpdateEvent {
    public  Customer customer;
    public CustomerUpdateEvent()
    {}
    public CustomerUpdateEvent(Customer customer) {

        this.customer = customer;
    }
}
