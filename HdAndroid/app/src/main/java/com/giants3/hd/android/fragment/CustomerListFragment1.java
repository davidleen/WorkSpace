package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.activity.CustomerEditActivity;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.events.CustomerUpdateEvent;
import com.giants3.hd.android.mvp.customer.CustomerListMVP;
import com.giants3.hd.android.mvp.customer.CustomerListPresenterImpl;
import com.giants3.hd.android.widget.RefreshLayoutConfig;
import com.giants3.hd.entity.Customer;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;


/**
 * 广交会报价
 * Created by davidleen29 on 2017/6/3.
 */

public class CustomerListFragment1 extends BaseMvpFragment<CustomerListMVP.Presenter> implements CustomerListMVP.Viewer {

    private static final int REQUEST_CUSTOMER_DETAIL_OPERATE = 9993;
    ItemListAdapter<Customer> adapter;
     @BindView(R.id.swipeLayout)
    TwinklingRefreshLayout swipeLayout;
     @BindView(R.id.list)
    ListView listView;
     @BindView(R.id.search_text)
    EditText search_text;
     @BindView(R.id.add)
    View add;

    TextWatcher watcher;


    @Override
    protected CustomerListMVP.Presenter createPresenter() {
        return new CustomerListPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_customer_list, null);
    }

    @Override
    protected void initView() {

        RefreshLayoutConfig.config(swipeLayout);



        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                Customer customer = (Customer) parent.getItemAtPosition(position);
                CustomerEditActivity.start(CustomerListFragment1.this, REQUEST_CUSTOMER_DETAIL_OPERATE, customer);


            }
        });
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (search_text != null && getPresenter() != null) {
                    String text = search_text.getText().toString().trim();
                    getPresenter().setKey(text);

                }
                doSearch();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        search_text.addTextChangedListener(watcher);
        search_text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        search_text.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerEditActivity.start(CustomerListFragment1.this, REQUEST_CUSTOMER_DETAIL_OPERATE);
            }
        });

        swipeLayout.setTargetView(listView);
        swipeLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {


                getPresenter().searchData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {


                getPresenter().loadMoreData();
            }
        });

        doSearch();

    }


    private void doSearch() {
        search_text.removeCallbacks(searchRunnable);
        search_text.postDelayed(searchRunnable, 1500);
    }

    @Override
    public void bindData(List<Customer> datas) {

        adapter.setDataArray(datas);

        swipeLayout.finishRefreshing();
    }


    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {


            //   showWaiting();
            searchData();


        }
    };


    private void searchData() {


//        swipeLayout.onRefresh(swipeLayout);

        if (swipeLayout != null)
            swipeLayout.startRefresh();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CUSTOMER_DETAIL_OPERATE:
                searchData();

                break;
        }

    }


    /**
     * @param event
     * @author davidleen29
     */
    public void onEvent(CustomerUpdateEvent event) {

        searchData();
    }
}
