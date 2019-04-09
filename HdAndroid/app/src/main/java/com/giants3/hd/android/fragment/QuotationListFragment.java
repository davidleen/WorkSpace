package com.giants3.hd.android.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.activity.QuotationDetailActivity;
import com.giants3.hd.android.adapter.QuotationListAdapter;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.Quotation;


/**
 * 报价列表 fragment
 */
public class QuotationListFragment extends ListFragment<Quotation> {


    public QuotationListFragment() {

    }

    @Override
    protected void onListItemClick(Quotation item) {


        //调整act
        Intent intent = new Intent(getActivity(), QuotationDetailActivity.class);
        intent.putExtra(QuotationDetailFragment.ARG_ITEM, GsonUtils.toJson(item));
        startActivity(intent);

    }


    public static QuotationListFragment newInstance(String param1, String param2) {
        QuotationListFragment fragment = new QuotationListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        adapter = new QuotationListAdapter(getActivity());
    }


    @Override
    protected AbstractAdapter<Quotation> getAdapter() {
        return adapter;
    }

    @Override
    protected UseCase getUserCase(String key, int pageIndex, int pageSize) {
        return UseCaseFactory.getInstance().createGetQuotationList(key, pageIndex, pageSize);
    }


}
