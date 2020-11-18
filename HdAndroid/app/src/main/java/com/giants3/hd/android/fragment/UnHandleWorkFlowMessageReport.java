package com.giants3.hd.android.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.android.widget.RefreshLayoutConfig;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.noEntity.RemoteData;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2018/7/22.
 */

public class UnHandleWorkFlowMessageReport extends BaseMvpFragment {

    ItemListAdapter<WorkFlowMessage> adapter;
     @BindView(R.id.swipeLayout)
    TwinklingRefreshLayout swipeLayout;


     @BindView(R.id.list)
    ListView listView;

    @Override
    protected void initView() {

        RefreshLayoutConfig.config(swipeLayout);
        swipeLayout.setTargetView(listView);
        swipeLayout.setOnRefreshListener(new RefreshListenerAdapter() {


            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {


                UseCaseFactory.getInstance().createGetUnHandleWorkFlowMessageReportCase(24).execute(new Subscriber<RemoteData<WorkFlowMessage>>() {
                    @Override
                    public void onCompleted() {
                        swipeLayout.finishRefreshing();
                        swipeLayout.finishLoadmore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeLayout.finishRefreshing();
                        swipeLayout.finishLoadmore();
                        ToastHelper.show(e.getMessage());
                    }

                    @Override
                    public void onNext(RemoteData<WorkFlowMessage> remoteData) {


                        if (remoteData.isSuccess()) {
                            adapter.setDataArray(remoteData.datas);

                            if (remoteData.datas.size() == 0) {
                                ToastHelper.show("当前无记录");
                            }

                        }

                        if (swipeLayout != null) {

                            swipeLayout.finishRefreshing();
                            swipeLayout.finishLoadmore();
                        }

                    }
                });

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {


                swipeLayout.finishLoadmore();


            }
        });


        adapter = new ItemListAdapter<WorkFlowMessage>(getContext());
        TableData tableData = TableData.resolveData(getActivity(), R.array.table_work_flow_message);
        adapter.setTableData(tableData);
        listView.setAdapter(adapter);
        swipeLayout.startRefresh();
    }

    @Override
    protected NewPresenter createPresenter() {
        return new NewPresenter() {
            @Override
            public void attachView(NewViewer view) {

            }

            @Override
            public void start() {

            }

            @Override
            public void detachView() {

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.unhandleworkflowmessage, null);
    }
}
