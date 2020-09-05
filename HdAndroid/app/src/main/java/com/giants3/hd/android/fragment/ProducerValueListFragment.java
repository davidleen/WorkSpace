package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.interf.TitleAble;
import com.giants3.hd.android.R;
import com.giants3.hd.android.activity.FragmentWrapActivity;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.adapter.ProducerValueReportAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.app.AProduct;
import com.giants3.hd.noEntity.ProducerValueItem;
import com.giants3.hd.noEntity.ProducerValueReport;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProducerValueListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProducerValueListFragment extends ListFragment<ProducerValueItem> {

    public  static final String PARA_JGH = "PARA_JGH";
    public static String PARAM_NAME="PARA_NAME";


    private String jgh;
    private String name;


    public ProducerValueListFragment() {
//        // Required empty public constructor
//        setFragmentListener(new OnProductFragmentInteractionListener() {
//            @Override
//            public void onFragmentInteraction(AProduct data) {
//                //调整act
//                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                intent.putExtra(ProductDetailPresenter.ARG_ITEM, GsonUtils.toJson(data));
//                startActivityForResult(intent, REQUEST_PRODUCT_DETAIL_EDIT);
//            }
//        });
    }


    public static ProducerValueListFragment newInstance(String jgh, String name) {
        ProducerValueListFragment fragment = new ProducerValueListFragment();
        Bundle args = new Bundle();
        args.putString(PARA_JGH, jgh);
        args.putString(PARAM_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jgh = getArguments().getString(PARA_JGH);
            name = getArguments().getString(PARAM_NAME);
            if(getActivity() instanceof TitleAble)
            {
               // getActivity().setTitle();
                ((TitleAble) getActivity()).setTitle(name+"的在产明细");
            }
        }


    }


    @Override
    protected boolean supportSearch() {
        return false;
    }

    @Override
    protected AbstractAdapter<ProducerValueItem> getAdapter() {
        ItemListAdapter<ProducerValueItem> adapter = new ItemListAdapter<>(getContext());
        adapter.setTableData(TableData.resolveData(getContext(), R.array.table_producer_value_item));
        return adapter;
    }

    @Override
    protected UseCase getUserCase(String key, int pageIndex, int pageSize) {
        return UseCaseFactory.getInstance().createGetUseCase(HttpUrl.getProducerValueItems(jgh), ProducerValueItem.class);
    }


    public interface OnProductFragmentInteractionListener extends OnFragmentInteractionListener<AProduct> {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onListItemClick(ProducerValueItem item) {






    }
}
