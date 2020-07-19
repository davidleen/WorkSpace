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
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.activity.WorkFlowListActivity;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.adapter.MonitorWorkFlowReportAdapter;
import com.giants3.hd.android.mvp.MonitorWorkFlowReportMVP;
import com.giants3.hd.android.mvp.monitorworkflowreport.PresenterImpl;
import com.giants3.hd.android.widget.RefreshLayoutConfig;
import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.noEntity.RemoteData;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.Bind;

/**
 * 生产计划任务报表
 * Created by davidleen29 on 2017/6/3.
 */

public class MonitorWorkFlowReportFragment extends BaseMvpFragment<MonitorWorkFlowReportMVP.Presenter> implements MonitorWorkFlowReportMVP.Viewer {

    private static final int REQUEST_MESSAGE_OPERATE = 9999;
    ItemListAdapter<ErpWorkFlowReport> adapter;
    @Bind(R.id.swipeLayout)
    TwinklingRefreshLayout swipeLayout;

    @Bind(R.id.list)
    ListView listView;
    @Bind(R.id.search_text)
    EditText search_text;


    @Bind(R.id.all)
    TextView all;


    @Bind(R.id.completed)
    TextView completed;


    @Bind(R.id.working)
    TextView working;

    View[] views;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected MonitorWorkFlowReportMVP.Presenter createPresenter() {
        return new PresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.layout_monitor_work_flow_report, null);
    }

    @Override
    protected void initView() {

        getPresenter().init(0);
//        swipeLayout.(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                searchErpOrderItems();
//
//            }
//        });

        views=new View[]{working,completed,all};
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index=0;
                for (int i = 0; i < views.length; i++) {

                    if(views[i]==v)
                    {
                        index=i;
                        break;
                    }
                }

                getPresenter().init(index);
                getPresenter().loadData();

            }
        };
        for (int i = 0; i < views.length; i++) {


            views[i].setOnClickListener(l);
        }



        adapter = new MonitorWorkFlowReportAdapter(getActivity());
        listView.setAdapter(adapter);

        RefreshLayoutConfig.config(swipeLayout);
        swipeLayout.setTargetView(listView);
        swipeLayout.setOnRefreshListener(new RefreshListenerAdapter(){

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {


                search_text.removeCallbacks(searchRunnable);
                search_text.postDelayed(searchRunnable, 500);

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {


                getPresenter().loadMore();






            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ErpWorkFlowReport report = (ErpWorkFlowReport) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), WorkFlowListActivity.class);
                intent.putExtra(WorkFlowListActivity.KEY_ORDER_NAME,  report.osNo);
                intent.putExtra(WorkFlowListActivity.KEY_ITEM, report.itm);
                startActivityForResult(intent, REQUEST_MESSAGE_OPERATE);


            }
        });
        search_text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        search_text.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                getPresenter().onKeyChange(s.toString());
                search_text.removeCallbacks(searchRunnable);
                search_text.postDelayed(searchRunnable, 500);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {


            searchData();


        }
    };

    private void searchData() {
        getPresenter().loadData();
    }

    @Override
    public void showWaiting() {

    }

    @Override
    public void hideWaiting() {
        swipeLayout.finishLoadmore( );
        swipeLayout.finishRefreshing( );
    }

    @Override
    public void selectCategory(int categoryIndex) {

        for (int i = 0; i < views.length; i++) {

            views[i].setSelected(i==categoryIndex);
        }



    }

    @Override
    public void bindData(RemoteData<ErpWorkFlowReport> data) {

        swipeLayout.setEnableLoadmore(data.hasNext());
        adapter.setDataArray(data.datas);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_MESSAGE_OPERATE:
                searchData();

                break;
        }

    }
}
