package com.giants3.hd.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.android.mvp.workFlow.ErpWorkFlowItemMvp;
import com.giants3.hd.entity_erp.ErpWorkFlowItem;

import java.util.List;

import butterknife.BindView;

/**
 * 生产流程管理界面
 * 挑选订单， 显示流程序列
 */
public class ErpWorkFlowItemActivity extends BaseHeadViewerActivity<ErpWorkFlowItemMvp.Presenter> implements ErpWorkFlowItemMvp.Viewer {


    public static final String PARA_OS_NO = "PARA_OS_NO";
    public static final String PARA_ITM = "PARA_ITM";
    public static final String PARA_WORKFLOWCODE = "PARA_WORKFLOWCODE";
     @BindView(R.id.list)
    ListView list;
    ItemListAdapter<ErpWorkFlowItem> adapter;

    public static void start(Activity activity, String osNo, int itm, String workFlowCode) {
        Intent intent = new Intent(activity, ErpWorkFlowItemActivity.class);
        intent.putExtra(PARA_OS_NO, osNo);
        intent.putExtra(PARA_ITM, itm);
        intent.putExtra(PARA_WORKFLOWCODE, workFlowCode);
        activity.startActivity(intent);
    }

    @Override
    protected ErpWorkFlowItemMvp.Presenter onLoadPresenter() {
        return new com.giants3.hd.android.mvp.workFlow.ErpWorkFlowItemPresenter();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("明细流程");
        adapter = new ItemListAdapter(this);
        TableData tableData = TableData.resolveData(this, R.array.erp_work_flow_item);
        adapter.setTableData(tableData);
        list.setAdapter(adapter);


    }


    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_erp_work_flow_items, null);
    }

    @Override
    protected void initEventAndData(Bundle savedInstance) {


        String osNo = getIntent().getStringExtra(PARA_OS_NO);
        int itm = getIntent().getIntExtra(PARA_ITM, 0);
        String workflowcode = getIntent().getStringExtra(PARA_WORKFLOWCODE);
        getPresenter().loadWorkFlowItems(osNo, itm, workflowcode);


    }


    @Override
    public void bindData(List<ErpWorkFlowItem> datas) {

        adapter.setDataArray(datas);


    }
}
