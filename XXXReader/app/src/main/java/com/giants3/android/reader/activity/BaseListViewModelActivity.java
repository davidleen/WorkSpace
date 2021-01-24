package com.giants3.android.reader.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.giants3.android.adapter.AbsRecycleViewHolder;
import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.databinding.ActivityListBinding;
import com.giants3.android.reader.vm.BaseListViewModel;
import com.giants3.android.reader.vm.BookListViewModel;
import com.giants3.android.refresh.PullRefreshListener;
import com.giants3.android.refresh.RefreshGroup;
import com.giants3.reader.entity.Book;
import com.giants3.reader.noEntity.RemoteData;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public  abstract  class BaseListViewModelActivity<T extends BaseListViewModel<D>,D > extends BaseViewModelActivity<ActivityListBinding, T  >  {

    RecyclerView recyclerView;
    AbstractRecyclerAdapter  listAdapter;

    @Override
    protected ActivityListBinding createViewBinding() {
        return ActivityListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<T> getViewModelClass() {


        //获取运行时参数
        Type genericSuperclass = getClass().getGenericSuperclass();
        try {
            if (genericSuperclass instanceof ParameterizedType) {
                Type actualTypeArgument = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];

                if (actualTypeArgument instanceof Class) {
                    Class<T> clas = (Class<T>) actualTypeArgument;
                    return clas;

                }


            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RefreshGroup refreshGroup = getViewBinding().refreshGroup;
        recyclerView = refreshGroup.getContentView();
        refreshGroup.setEnableRefresh(true);
        listAdapter = createAdapter();

        listAdapter.setOnItemClickListener(new AbstractRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item, int position) {

                BaseListViewModelActivity.this.onItemClick(listAdapter,(D)item,position);

            }
        });
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(getDefaultLayoutManager());
        getViewModel().getListResult().observe(this, new Observer<RemoteData<D>>() {
            @Override
            public void onChanged(RemoteData<D> bookRemoteData) {


                boolean hasMore = bookRemoteData.isSuccess() ? bookRemoteData.pageIndex < bookRemoteData.pageCount - 1 : false;
                if (bookRemoteData.pageIndex == 0) {
                    refreshGroup.finishRefresh();
                } else {
                    refreshGroup.finishLoadMore(bookRemoteData.isSuccess(), hasMore);
                }
                refreshGroup.setEnableLoadMore(hasMore);
                listAdapter.setDataArray(bookRemoteData.datas);

            }
        });
        refreshGroup.setPullRefreshListener(new PullRefreshListener() {
            @Override
            public void onPullToRefresh() {
                getViewModel().loadData();
            }
        });
        getViewModel().loadData();
    }


    protected abstract AbstractRecyclerAdapter createAdapter();

    protected RecyclerView.LayoutManager getDefaultLayoutManager()
    {

        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        {

            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                RecyclerView.LayoutParams layoutParams = super.generateDefaultLayoutParams();
                layoutParams.width= RecyclerView.LayoutParams.MATCH_PARENT;
                return layoutParams;
            }
        };
    }

    public void onItemClick(AbstractRecyclerAdapter adapter,D item,int position)
    {





    }

}
