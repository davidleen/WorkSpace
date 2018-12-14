package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;

import com.giants3.hd.android.R;
import com.giants3.hd.android.activity.CustomerEditActivity;
import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.Customer;

/**
 * 广交会报价
 * Created by davidleen29 on 2017/6/3.
 */

public class CustomerListFragment extends ListFragment<Customer> {

    private static final int REQUEST_CUSTOMER_DETAIL_OPERATE = 9993;


    @Override
    protected AbstractAdapter<Customer> getAdapter() {

        ItemListAdapter<Customer> adapter;
        adapter = new ItemListAdapter(getActivity());
        adapter.setTableData(TableData.resolveData(getActivity(), R.array.table_customer_list));
        return adapter;
    }

    @Override
    protected void initView() {


    }


    protected void addNewOne() {
        CustomerEditActivity.start(CustomerListFragment.this, REQUEST_CUSTOMER_DETAIL_OPERATE);

    }


    protected void onListItemClick(Customer item) {


        CustomerEditActivity.start(CustomerListFragment.this, REQUEST_CUSTOMER_DETAIL_OPERATE, item);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CUSTOMER_DETAIL_OPERATE:


                break;
        }

    }





    @Override
    protected UseCase getUserCase(String key, int pageIndex, int pageSize) {

     return    UseCaseFactory.getInstance().createGetCustomerListUseCase(key ,pageIndex,pageSize);


    }

    @Override
    protected boolean supportAdd() {
        return true;
    }
}
