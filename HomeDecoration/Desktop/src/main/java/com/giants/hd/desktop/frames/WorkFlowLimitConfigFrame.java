package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.WorkFlowLimitConfigIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowLimitConfigViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Limit;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowTimeLimit;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import rx.Subscriber;

import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowLimitConfigFrame extends BaseMVPFrame<WorkFlowLimitConfigViewer> implements WorkFlowLimitConfigIPresenter {

    List<WorkFlowTimeLimit> workFlowLimit;

    public WorkFlowLimitConfigFrame() {


        super(ModuleConstant.TITLE_WORK_FLOW_LIMIT);


        readData();

    }

    private void readData() {


        UseCaseFactory.getInstance().createGetWorkFlowLimitUseCase().execute(new Subscriber<RemoteData<WorkFlowTimeLimit>>() {
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
            public void onNext(RemoteData<WorkFlowTimeLimit> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {


                    setData(workFlowRemoteData.datas);

                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();
    }

    private void setData(List<WorkFlowTimeLimit> datas) {

        workFlowLimit = datas;

        getViewer().bindData(workFlowLimit);
    }


    @Override
    protected WorkFlowLimitConfigViewer createViewer() {
        return new Panel_Work_Flow_Limit(this);
    }

    @Override
    public void save(boolean updateCompletedOrderItem) {

        if (workFlowLimit == null) return;


        UseCaseFactory.getInstance().createSaveWorkFlowLimitUseCase(workFlowLimit,updateCompletedOrderItem).execute(new Subscriber<RemoteData<Void>>() {
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
            public void onNext(RemoteData<Void> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {

                    getViewer().showMesssage("保存成功");


                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();


    }
}
