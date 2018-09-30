package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.SubWorkFlowListIPresenter;
import com.giants.hd.desktop.mvp.presenter.WorkFlowLimitConfigIPresenter;
import com.giants.hd.desktop.mvp.viewer.SubWorkFlowLIstViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowLimitConfigViewer;
import com.giants.hd.desktop.viewImpl.Panel_Sub_Work_Flow;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Limit;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowTimeLimit;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import rx.Subscriber;

import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class SubWorkFlowListFrame extends BaseMVPFrame<SubWorkFlowLIstViewer> implements SubWorkFlowListIPresenter {

    List<Sub_workflow_state> workFlowLimit;

    public SubWorkFlowListFrame() {


        super(ModuleConstant.TITLE_SUB_WORK_FLOW);




    }

    private void readData(String key, String dateStart, String dateEnd) {


        UseCaseFactory.getInstance().createGetSubWorkFlowListUseCase(  key,   dateStart,   dateEnd).execute(new Subscriber<RemoteData<Sub_workflow_state>>() {
            //            @Override
            public void onCompleted() {
                getViewer().hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<Sub_workflow_state> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {


                    setData(workFlowRemoteData.datas);

                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();
    }

    private void setData(List<Sub_workflow_state> datas) {

        workFlowLimit = datas;

        getViewer().bindData(workFlowLimit);
    }


    @Override
    protected SubWorkFlowLIstViewer createViewer() {
        return new Panel_Sub_Work_Flow(this);
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
    public void search(String key, String dateStart, String dateEnd) {

        readData(  key,   dateStart,   dateEnd);
    }
}
