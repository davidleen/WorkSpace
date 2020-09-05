package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.giants3.hd.android.R;
import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.mvp.list.ListMVP;
import com.giants3.hd.android.mvp.list.ListPresenterImpl;
import com.giants3.hd.android.widget.RefreshLayoutConfig;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.noEntity.RemoteData;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by david on 2016/5/12.
 */
public class ListFragment<T> extends BaseMvpFragment<ListMVP.Presenter> implements ListMVP.Viewer<T> {

    AbstractAdapter<T> adapter;


    @Bind(R.id.list)
    ListView listView;
    @Bind(R.id.search_text)
    EditText search_text;
    @Bind(R.id.swipeLayout)
    TwinklingRefreshLayout swipeLayout;
    @Bind(R.id.add)
    View add;
    OnFragmentInteractionListener<T> mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ViewGroup customContainer = view.findViewById(R.id.custom_container);
        configCustomContainer(customContainer);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefreshLayoutConfig.config(swipeLayout);

        if (getAdapter() != null)
            adapter = getAdapter();
        if (adapter != null)
            listView.setAdapter(adapter);

        search_text.setVisibility(supportSearch()?View.VISIBLE:View.GONE);
        if(supportSearch()) {

            search_text.addTextChangedListener(new TextWatcher() {
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
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (mListener != null) {
                    mListener.onFragmentInteraction(adapter.getItem(position));
                }else
                {
                    onListItemClick(adapter.getItem(position));
                }
            }
        });

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
        swipeLayout.setTargetView(listView);
        search_text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        search_text.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        add.setVisibility(supportAdd()? View.VISIBLE:View.GONE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                addNewOne();
            }
        });


        searchRunnable.run();
    }

    protected void configCustomContainer(ViewGroup container) {

    }


    protected void addNewOne() {


    }

    protected boolean supportSearch()
    {
        return true;
    }


    protected  void onListItemClick(T item) {

    }


    protected boolean supportAdd()
    {return false;}




    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {

            searchData();


        }
    };


    private void searchData() {


        if (swipeLayout != null)
            swipeLayout.startRefresh();

    }

    private void doSearch() {
        search_text.removeCallbacks(searchRunnable);
        search_text.postDelayed(searchRunnable, 1000);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected ListMVP.Presenter createPresenter() {
        return new ListPresenterImpl<T>() {


            @Override
            public UseCase getListUseCase(String key, int pageIndex, int pageSize) {
                return getUserCase(key, pageIndex, pageSize);
            }


        };
    }

    protected AbstractAdapter<T> getAdapter() {
        return null;
    }


    public void setFragmentListener(OnFragmentInteractionListener listener) {
        this.mListener = listener;
    }


    /**
     * 获取数据访问实例
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    protected UseCase getUserCase(String key, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    protected void onLoginRefresh() {
        getPresenter().searchData();
    }


    /**
     * 列表交互接口
     *
     * @param <T>
     */
    public interface OnFragmentInteractionListener<T> {

        void onFragmentInteraction(T data);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }


    @Override
    public void bindData(RemoteData<T> remoteData) {
        List<T> datas= filterData(remoteData.datas);
        adapter.setDataArray(datas);
        swipeLayout.finishRefreshing();
        swipeLayout.finishLoadmore();
        swipeLayout.setEnableLoadmore(remoteData.hasNext());
    }


    protected List<T> filterData(List<T> datas)
    {

        return datas;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

                getPresenter().searchData();


    }

    protected void notifyAdapterDataChanged()
    {

        getPresenter().rebindData();
    }

}
