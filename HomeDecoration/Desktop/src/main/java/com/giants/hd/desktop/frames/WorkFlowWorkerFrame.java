package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.WorkFlowWorkerUpdateDialog;
import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Worker;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.WorkFlowWorker;
import rx.Subscriber;

import javax.swing.*;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowWorkerFrame extends BaseMVPFrame<WorkFlowWorkerViewer> implements WorkFlowWorkerIPresenter {


    public WorkFlowWorkerFrame() {


        super(ModuleConstant.TITLE_WORK_FLOW_WORKER);


        readData();

    }

    private void readData() {


        UseCaseFactory.getInstance().createGetWorkFlowWorkerUseCase().execute(new Subscriber<RemoteData<WorkFlowWorker>>() {
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
            public void onNext(RemoteData<WorkFlowWorker> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {


                    setData(workFlowRemoteData.datas);

                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();
    }

    private void setData(List<WorkFlowWorker> datas) {


        getViewer().bindData(datas);
    }


    @Override
    protected WorkFlowWorkerViewer createViewer() {
        return new Panel_Work_Flow_Worker(this);
    }

    @Override
    public void addOne() {


        WorkFlowWorkerUpdateDialog dialog = new WorkFlowWorkerUpdateDialog(SwingUtilities.getWindowAncestor(this), null);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowWorker workFlowWorker = dialog.getResult();
        if (workFlowWorker != null) {
            readData();
        }


    }

    @Override
    public void showOne(WorkFlowWorker workFlowWorker) {
        WorkFlowWorkerUpdateDialog dialog = new WorkFlowWorkerUpdateDialog(SwingUtilities.getWindowAncestor(this), workFlowWorker);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowWorker result = dialog.getResult();
        if (result != null) {
            readData();
        }
    }
}
