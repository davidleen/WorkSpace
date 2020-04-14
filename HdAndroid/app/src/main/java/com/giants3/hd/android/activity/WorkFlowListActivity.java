package com.giants3.hd.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.BuildConfig;
import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.WorkFlowReportItemAdapter;
import com.giants3.hd.android.fragment.SendWorkFlowFragment;
import com.giants3.hd.android.helper.AuthorityUtil;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.mvp.workFlow.WorkFlowListMvp;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.entity.OrderItemWorkMemo;
import com.giants3.hd.entity.ProductWorkMemo;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.entity_erp.SampleState;
import com.giants3.hd.noEntity.CompanyPosition;
import com.giants3.hd.utils.StringUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 生产流程管理界面
 * 挑选订单， 显示流程序列
 */
public class WorkFlowListActivity extends BaseHeadViewerActivity<WorkFlowListMvp.Presenter> implements WorkFlowListMvp.Viewer, SendWorkFlowFragment.OnFragmentInteractionListener {


    public static final int REQUEST_CODE = 33;
    public static final String KEY_ORDER_ITEM = "KEY_ORDER_ITEM";



    @Bind(R.id.orderItemInfo)
    TextView orderItemInfo;

    @Bind(R.id.beibang)
    TextView beibang;


    @Bind(R.id.sampleState)
    TextView sampleState;

    @Bind(R.id.clear)
    View clear;
    @Bind(R.id.adjust)
    View adjust;


    @Bind(R.id.workFlowReport)
    GridView workFlowReport;

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;


    // 进度显示adapter
    WorkFlowReportItemAdapter adapter;


    //Order adapter;


    @Override
    protected WorkFlowListMvp.Presenter onLoadPresenter() {
        return new com.giants3.hd.android.mvp.workFlow.WorkFlowListPresenter();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTitle("生产流程管理");





        clear.setVisibility(SharedPreferencesHelper.getLoginUser().name.equals(User.ADMIN)?View.VISIBLE:View.GONE);
        clear.setOnClickListener(this);
        adjust.setVisibility(SharedPreferencesHelper.getLoginUser().name.equals(User.ADMIN)?View.VISIBLE:View.GONE);
        adjust.setOnClickListener(this);
    }


    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_work_flow_list, null);
    }

    @Override
    protected void initEventAndData(Bundle savedInstance) {
        ErpOrderItem orderItem = null;
        try {
            orderItem = GsonUtils.fromJson(getIntent().getStringExtra(KEY_ORDER_ITEM), ErpOrderItem.class);
        } catch (Throwable e) {
            e.printStackTrace();

            showMessage("未发现传递的订单数据");
            finish();
            return;
        }


        getPresenter().setSelectOrderItem(orderItem);



        adapter = new WorkFlowReportItemAdapter(this);
        adapter.setAdapterListener(new WorkFlowReportItemAdapter.WorkFlowAdapterListener() {
            @Override
            public void onSendWorkFlow(ErpWorkFlowReport workFlowReport) {
                getPresenter().sendWorkFlow(workFlowReport.osNo, workFlowReport.itm, workFlowReport.workFlowStep);
            }

            @Override
            public void onReceiveWorkFlow(ErpWorkFlowReport report) {
                getPresenter().confirmCompletedWork(report);
            }

            @Override
            public void removeReportFromMonitor(ErpWorkFlowReport data) {


                getPresenter().removeReportFromMonitor(data);
            }

            @Override
            public void addReportToMonitor(ErpWorkFlowReport data) {
                getPresenter().addReportToMonitor(data);

            }
        });
        workFlowReport.setAdapter(adapter);
        workFlowReport.setNumColumns(3);
        workFlowReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ErpWorkFlowReport workFlowReport = (ErpWorkFlowReport) parent.getItemAtPosition(position);
                getPresenter().chooseWorkFlowReport(workFlowReport);


            }
        });


        workFlowReport.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ErpWorkFlowReport workFlowReport = (ErpWorkFlowReport) parent.getItemAtPosition(position);


                getPresenter().chooseWorkFlowReport(workFlowReport);


                return true;
            }
        });

      //  workFlowReport.setExpanded(true);

        orderItemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!StringUtils.isEmpty(orderItemInfo.getText().toString().trim())) {
                    getPresenter().searchData();
                }


            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSearchWorkFlowReport();
            }
        });


    }


    private void doSearchWorkFlowReport() {
        swipeLayout.setRefreshing(true);
        getPresenter().searchData();


    }


    @Override
    public void showSampleState(SampleState data) {


        beibang.setText("背板："+(SampleState.BB_TRUE.equalsIgnoreCase(  data.bb )
              ? "有" : "无"));



        String  message ="";
        if(data.BL_ID!=null) {
            switch (data.BL_ID) {

                case SampleState.STATE_LB:
                case SampleState.STATE_PC:
                    message = "在库：" + data.wareHouse +",时间：" + data.ltime;

                    break;
                case SampleState.STATE_LN:

                    message = "借出：" + data.factory +",时间："+ data.ltime;
                    break;

            }
        }
        sampleState.setText("样品状态："+(message));





    }

    @Override
    public void showSendWorkFlowDialog(final ErpWorkFlowReport workFlowReport,final ErpWorkFlowReport nextWorkFlowReport, ProductWorkMemo productWorkMemo, OrderItemWorkMemo orderItemWorkMemo) {

        //是否生产设置备注的流程


        boolean unMemoStep = workFlowReport.workFlowStep == ErpWorkFlow.STEPS[0] || workFlowReport.workFlowStep == ErpWorkFlow.STEPS[ErpWorkFlow.STEPS.length - 1];

        boolean isWorkingStep=workFlowReport.workFlowStep<=ErpWorkFlow.LAST_STEP;
        boolean canSend =workFlowReport.summary==null? isWorkingStep&&getPresenter().canSendWorkFlow(workFlowReport):false;
        boolean canReceive =workFlowReport.summary==null?isWorkingStep&&nextWorkFlowReport!=null&& getPresenter().canReceiveWorkFlow(nextWorkFlowReport):false;


//        if (  !canSend && !canReceive) return;

        View inflate = getLayoutInflater().inflate(R.layout.layout_work_flow_info, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(inflate).create();
        TextView productMemoView = (TextView) inflate.findViewById(R.id.productMemo);
        TextView title_productMemoView = (TextView) inflate.findViewById(R.id.title_product_memo);
        productMemoView.setText(productWorkMemo == null ? "" : productWorkMemo.memo);
        TextView orderItemMemoView = (TextView) inflate.findViewById(R.id.orderItemMemo);
        TextView viewMaterial = (TextView) inflate.findViewById(R.id.viewMaterial);
        TextView items = (TextView) inflate.findViewById(R.id.items);
        TextView title_orderItemMemoView = (TextView) inflate.findViewById(R.id.title_order_item_memo);
        orderItemMemoView.setText(orderItemWorkMemo == null ? "" : orderItemWorkMemo.memo);

        productMemoView.setVisibility(unMemoStep ? View.GONE : View.VISIBLE);
        title_productMemoView.setVisibility(unMemoStep ? View.GONE : View.VISIBLE);
        orderItemMemoView.setVisibility(unMemoStep ? View.GONE : View.VISIBLE);
        title_orderItemMemoView.setVisibility(unMemoStep ? View.GONE : View.VISIBLE);
        viewMaterial.setVisibility(unMemoStep ? View.GONE : View.VISIBLE);


        TextView send = (TextView) inflate.findViewById(R.id.send);
        send.setVisibility(canSend ? View.VISIBLE : View.GONE);
        send.setVisibility(canSend ? View.VISIBLE : View.GONE);

        send.setText(workFlowReport.workFlowStep == ErpWorkFlow.STEP_CHENGPIN ? "出 货" : "生产结束");


        View syncErpStockData = inflate.findViewById(R.id.syncErpStockData);
        syncErpStockData.setVisibility(workFlowReport.workFlowStep==ErpWorkFlow.LAST_STEP&&(AuthorityUtil.getInstance().isAdmin() || BuildConfig.DEBUG) ? View.VISIBLE : View.GONE);

        syncErpStockData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPresenter().syncErpStockData(workFlowReport);
            }
        });

        View adjust_limit = inflate.findViewById(R.id.adjust_limit);
        adjust_limit.setVisibility( (AuthorityUtil.getInstance().isAdmin()  || BuildConfig.DEBUG||SharedPreferencesHelper.getLoginUser().position != CompanyPosition.FACTORY_DIRECTOR_CODE) ? View.VISIBLE : View.GONE);
        adjust_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                showUpdateLimitDialog(workFlowReport);
                alertDialog.dismiss();
            }


        });


        TextView receive = (TextView) inflate.findViewById(R.id.receive);
        receive.setVisibility(canReceive ? View.VISIBLE : View.GONE);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().sendWorkFlow(workFlowReport.osNo, workFlowReport.itm, workFlowReport.workFlowStep);

            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().receiveWorkFlow(nextWorkFlowReport.osNo, nextWorkFlowReport.itm, nextWorkFlowReport.workFlowStep);

            }
        });

        viewMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 包装流程，并且名称带有组装时候， 弹出选择查看组装的还是包装的
                if (ErpWorkFlow.CODE_BAOZHUANG.equals(workFlowReport.workFlowCode) && workFlowReport.workFlowName.startsWith(ErpWorkFlow.NAME_ZUZHUANG)) {


                    final AlertDialog zhbzDialog = new AlertDialog.Builder(WorkFlowListActivity.this)
                            .setItems(new String[]{ErpWorkFlow.NAME_ZUZHUANG + "材料", ErpWorkFlow.NAME_BAOZHUANG + "材料"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which) {
                                        case 0:
                                            OrderItemWorkFlowMaterialActivity.start(WorkFlowListActivity.this, workFlowReport.osNo, workFlowReport.itm, ErpWorkFlow.CODE_ZUZHUANG, 0);

                                            break;
                                        case 1:
                                            OrderItemWorkFlowMaterialActivity.start(WorkFlowListActivity.this, workFlowReport.osNo, workFlowReport.itm, ErpWorkFlow.CODE_BAOZHUANG, 0);
                                            break;
                                    }

                                }
                            }).create();
                    zhbzDialog.setCanceledOnTouchOutside(true);
                    zhbzDialog.show();


                } else


                    OrderItemWorkFlowMaterialActivity.start(WorkFlowListActivity.this, workFlowReport.osNo, workFlowReport.itm, workFlowReport.workFlowCode, 0);


            }
        });


        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ErpWorkFlowItemActivity.start(WorkFlowListActivity.this,workFlowReport.osNo,workFlowReport.itm,workFlowReport.workFlowCode);



            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    @Override
    public void showSelectOrderItem(ErpOrderItem orderItem) {


        orderItemInfo.setText("订单号:" + orderItem.os_no + ",货号:" + orderItem.prd_name+",订单数量:"+orderItem.qty);


    }


    @Override
    public void showSendReceiveDialog(List<WorkFlowMessage> messageList) {



        if(messageList.size()>1) {
            Intent intent = new Intent(this, WorkFlowMessageActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        }else
        {
            Intent intent = new Intent(this, WorkFlowMessageReceiveActivity.class);
            intent.putExtra(WorkFlowMessageReceiveActivity.KEY_MESSAGE, GsonUtils.toJson(messageList.get(0)));
            startActivityForResult(intent, REQUEST_CODE);
        }

    }


    @Override
    public void showUnHandleWorkMessageDialog(List<WorkFlowMessage> messageList) {
        if(messageList.size()>1) {
            Intent intent = new Intent(this, WorkFlowMessageListActivity.class);
            intent.putExtra(WorkFlowMessageListActivity.KEY_MESSAGE_LIST,GsonUtils.toJson(messageList));
            startActivityForResult(intent, REQUEST_CODE);

        }else if(messageList.size()==1)
        {
            Intent intent = new Intent(this, WorkFlowMessageReceiveActivity.class);
            intent.putExtra(WorkFlowMessageReceiveActivity.KEY_MESSAGE, GsonUtils.toJson(messageList.get(0)));
            startActivityForResult(intent, REQUEST_CODE);
        }else
        {
            ToastHelper.show("没有发现可以确认的流程");
        }
    }

    @Override
    public void bindOrderIteWorkFlowReport(List<ErpWorkFlowReport> datas) {


        adapter.setDataArray(datas);

        swipeLayout.setRefreshing(false);


    }


    @Override
    public void sendWorkFlowMessage(List<ErpOrderItemProcess> datas) {


        SendWorkFlowFragment fragment = SendWorkFlowFragment.newInstance(datas);
        fragment.show(getSupportFragmentManager(), "dialog9999");


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWorkFlowSend() {


        getPresenter().searchData();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            doSearchWorkFlowReport();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onViewClick(int id, View v) {


        switch (id)
        {
            case R.id.clear:

          new      AlertDialog.Builder(this).setTitle("是否确定重置流程数据!!!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

                  dialog.dismiss();
                   getPresenter().resetWorkFlow();
              }
          }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
              }
          }).create().show();


                break;  case R.id.adjust:

                  new      AlertDialog.Builder(this).setTitle("是否校正相关流程数据itm值？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                          dialog.dismiss();
                           getPresenter().adjustWorkFlow();
                      }
                  }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                      }
                  }).create().show();


                break;



        }
    }


    private void showUpdateLimitDialog(final ErpWorkFlowReport workFlowReport) {

        View inflate = getLayoutInflater().inflate(R.layout.layout_work_flow_limit_update, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(inflate).create();
        final EditText limit=inflate.findViewById(R.id.limit);
        final EditText alert=inflate.findViewById(R.id.alert);

        limit.setText(String.valueOf(workFlowReport.limitDay));
        alert.setText(String.valueOf(workFlowReport.alertDay));



        View update=inflate.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int limitDay=-1;
                int alertDay=-1;
                try {
                    limitDay = Integer.valueOf(limit.getText().toString());
                    alertDay = Integer.valueOf(alert.getText().toString());
                } catch (Throwable t){

                }
                if(limitDay<0||limitDay<0)
                {
                    showMessage("输入异常，必须是整数>0");
                    return;
                }
                getPresenter().updateWorkFlowTimeLimit(workFlowReport,limitDay,alertDay);




            }
        });
        View close=inflate.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


        alertDialog.show();

    }
}
