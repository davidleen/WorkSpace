package com.giants3.hd.android.activity;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.WorkFlowMessageAdapter;
import com.giants3.hd.android.mvp.workflowmessage.OrderItemWorkFlowMessageMVP;
import com.giants3.hd.android.mvp.workflowmessage.PresenterImpl;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.WorkFlowMessage;

import java.util.List;

import butterknife.BindView;


/**
 * Created by davidleen29 on 2017/6/25.
 */

public class OrderItemWorkFlowMessageActivity extends BaseViewerActivity<OrderItemWorkFlowMessageMVP.Presenter> implements OrderItemWorkFlowMessageMVP.Viewer  {
    private static final int REQUEST_MESSAGE_OPERATE = 1000;
     @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    public static final String KEY_ORDER_ITEM = "KEY_ORDER_ITEM";

     @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;


     @BindView(R.id.workFlowMessages)
    ListView workFlowMessages;

    WorkFlowMessageAdapter adapter;

    @Override
    protected OrderItemWorkFlowMessageMVP.Presenter onLoadPresenter() {
        return new PresenterImpl();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_item_work_flow_message);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("订单流程消息记录");

        }




    }

    @Override
    public void bindData(List<WorkFlowMessage> datas) {
        adapter.setDataArray(datas);
    }

    @Override
    protected void initEventAndData() {



        ErpOrderItem orderItem = null;
        try {
            orderItem = GsonUtils.fromJson(getIntent().getStringExtra(KEY_ORDER_ITEM), ErpOrderItem.class);
        } catch (Throwable e) {
            e.printStackTrace();

            showMessage("未发现传递的订单数据");
            finish();
            return;
        }

        getPresenter().setOrderItem(orderItem);
        adapter=new WorkFlowMessageAdapter(this);
        workFlowMessages.setAdapter(adapter);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                getPresenter().loadWorkFlowMessageByOrderItem();

            }
        });

        workFlowMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                WorkFlowMessage workFlowMessage= (WorkFlowMessage) parent.getItemAtPosition(position);
                WorkFlowMessageReceiveActivity.start(OrderItemWorkFlowMessageActivity.this,workFlowMessage,REQUEST_MESSAGE_OPERATE);

            }
        });


    }

    @Override
    public void showWaiting() {



        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hideWaiting() {
        swipeLayout.setRefreshing(false);
    }
}
