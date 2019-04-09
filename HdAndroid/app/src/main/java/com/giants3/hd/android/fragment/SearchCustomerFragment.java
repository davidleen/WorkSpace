package com.giants3.hd.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.giants3.android.frame.util.Utils;
import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.android.mvp.searchcustomer.SearchCustomerMvp;
import com.giants3.hd.entity.Customer;

import java.util.List;

import butterknife.Bind;

/**
 * 查找客户Fragment
 */
public class SearchCustomerFragment extends BaseDialogFragment<SearchCustomerMvp.Presenter> implements SearchCustomerMvp.Viewer {

    private static final String ARG_AVAILABLE_ITEMS = "ARG_AVAILABLE_ITEMS";


    @Bind(R.id.key)
    EditText editText;
    @Bind(R.id.list)
    ListView listView;
    ItemListAdapter<Customer> listAdapter;
    private OnFragmentInteractionListener mListener;
    private String keytoSearch;

    public SearchCustomerFragment() {

    }


    public static SearchCustomerFragment newInstance() {
        SearchCustomerFragment fragment = new SearchCustomerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected SearchCustomerMvp.Presenter onLoadPresenter() {
        return new com.giants3.hd.android.mvp.searchcustomer.PresenterImpl();
    }

    @Override
    protected void initEventAndData() {


        listAdapter = new ItemListAdapter<>(getActivity());
        listAdapter.setTableData(TableData.resolveData(getActivity(), R.array.table_customer_list));
        int wh[] = Utils.getScreenWH();

        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = (int) (wh[0] * 0.8);
        layoutParams.height = (int) (wh[1] * 0.8);
        listView.setLayoutParams(layoutParams);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Customer customer = (Customer) parent.getItemAtPosition(position);
                if (customer != null && mListener != null
                        ) {
                    mListener.onCustomerSelect(customer);
                    dismiss();
                }
            }
        });


//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                //调整act
//                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
//                intent.putExtra(ProductDetailFragment.ARG_ITEM, GsonUtils.toJson(parent.getItemAtPosition(position)));
//                startActivity(intent);
//                return true;
//            }
//        }) ;


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                getPresenter().setKeyWord(s.toString());
                doSearch();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        editText.post(new Runnable() {
            @Override
            public void run() {
                String text = "";
                editText.setText(text);
                editText.setSelection(text.length());
            }
        });

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getPresenter().search();

        }
    };


    private void doSearch() {

        editText.removeCallbacks(runnable);
        editText.postDelayed(runnable, 1000);

    }


    @Override
    protected void initViews(Bundle savedInstanceState) {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_search_customer, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void bindDatas(List<Customer> datas) {


        listAdapter.setDataArray(datas);

    }


    public interface OnFragmentInteractionListener {


        void onCustomerSelect(Customer aProduct);
    }


}
