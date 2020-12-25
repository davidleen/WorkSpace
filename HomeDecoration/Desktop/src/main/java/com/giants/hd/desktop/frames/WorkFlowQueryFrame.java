package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.SubWorkFlowListIPresenter;
import com.giants.hd.desktop.mvp.presenter.WorkFlowQueryPresenter;
import com.giants.hd.desktop.mvp.viewer.SubWorkFlowLIstViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowQueryViewer;
import com.giants.hd.desktop.viewImpl.Panel_Sub_Work_Flow;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Query;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import rx.Subscriber;

import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowQueryFrame extends BaseMVPFrame<WorkFlowQueryViewer> implements WorkFlowQueryPresenter {


    public WorkFlowQueryFrame() {


        super(ModuleConstant.TITLE_WORK_FLOW_QUERY);




    }

    private void readData(String key, int pageIndex, int  pageSize) {

        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.getStockInButNotCompletedOrderItems( key,   pageIndex,   pageSize), ErpOrderItem.class).execute(new RemoteDataSubscriber<ErpOrderItem>(getViewer()) {






            @Override
            protected void handleRemoteData(RemoteData<ErpOrderItem> data) {

                    setData(data);


            }




        });
        getViewer().showLoadingDialog();
    }

    private void setData(RemoteData<ErpOrderItem> datas) {


        getViewer().bindData(datas);
    }


    @Override
    protected WorkFlowQueryViewer createViewer() {
        return new Panel_Work_Flow_Query(this);
    }

    @Override
    public void save(boolean updateCompletedOrderItem) {

//        if (workFlowLimit == null) return;
//
//
//        UseCaseFactory.getInstance().createSaveWorkFlowLimitUseCase(workFlowLimit,updateCompletedOrderItem).execute(new Subscriber<RemoteData<Void>>() {
//            //            @Override
//            public void onCompleted() {
//                getViewer().hideLoadingDialog();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                getViewer().hideLoadingDialog();
//                getViewer().showMesssage(e.getMessage());
//            }
//
//
//            @Override
//            public void onNext(RemoteData<Void> workFlowRemoteData) {
//                getViewer().hideLoadingDialog();
//                if (workFlowRemoteData.isSuccess()) {
//
//                    getViewer().showMesssage("保存成功");
//
//
//                } else
//
//                    getViewer().showMesssage(workFlowRemoteData.message);
//
//
//            }
//
//
//        });
//        getViewer().showLoadingDialog();


    }

    @Override
    public void search(String key, int pageIndex, int pageSize  ) {

        readData(  key,   pageIndex,   pageSize);
    }

    @Override
    public void queryOrderItems(String key, int workFlowState, int flowStep, int alertType, int pageIndex, int pageSize) {
        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.queryErpOrderItems( key, workFlowState,flowStep, alertType, pageIndex,   pageSize), ErpOrderItem.class).execute(new RemoteDataSubscriber<ErpOrderItem>(getViewer()) {






            @Override
            protected void handleRemoteData(RemoteData<ErpOrderItem> data) {

                getViewer().bindAlertData(data);



            }




        });
        getViewer().showLoadingDialog();
    }


}
