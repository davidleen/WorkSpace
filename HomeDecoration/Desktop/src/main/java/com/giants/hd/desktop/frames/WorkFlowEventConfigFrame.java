package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.WorkFlowAreaUpdateDialog;
import com.giants.hd.desktop.dialogs.WorkFlowArrangerUpdateDialog;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.WorkFlowEventConfigIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowEventConfigViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Event_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.WorkFlowArea;
import com.giants3.hd.entity.WorkFlowArranger;
import com.giants3.hd.entity.WorkFlowEvent;

import javax.swing.*;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowEventConfigFrame extends BaseMVPFrame<WorkFlowEventConfigViewer> implements WorkFlowEventConfigIPresenter {


    public WorkFlowEventConfigFrame() {

        super(ModuleConstant.TITLE_WORK_FLOW_ITEM_EVENT_CONFIG);


        readData();

    }

    private void readData() {


        UseCaseFactory.getInstance().createGetWorkFlowEventListUseCase().execute(new RemoteDataSubscriber<WorkFlowEvent>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<WorkFlowEvent> data) {
                setData(data.datas);
            }
        });
        UseCaseFactory.getInstance().createGetWorkFlowAreaListUseCase().execute(new RemoteDataSubscriber<WorkFlowArea>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<WorkFlowArea> data) {
                setArea(data.datas);
            }
        });


        getViewer().showLoadingDialog();
    }

    private void setData(List<WorkFlowEvent> datas) {


        getViewer().bindData(datas);
    }
    private void setArea(List<WorkFlowArea> datas) {


        getViewer().bindArea(datas);
    }


    @Override
    protected WorkFlowEventConfigViewer createViewer() {
        return new Panel_Work_Flow_Event_List(this);
    }

    @Override
    public void addOne() {


        WorkFlowArrangerUpdateDialog dialog = new WorkFlowArrangerUpdateDialog(SwingUtilities.getWindowAncestor(this), null);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowArranger workFlowWorker = dialog.getResult();
        if (workFlowWorker != null) {
            readData();
        }


    }

    @Override
    public void showOne(WorkFlowEvent workFLowEvent) {
//        WorkFlowAreaUpdateDialog dialog = new WorkFlowAreaUpdateDialog(SwingUtilities.getWindowAncestor(this), workFLowEvent);
//        dialog.setLocationByPlatform(true);
//        dialog.setVisible(true);
//        WorkFlowArranger result = dialog.getResult();
//        if (result != null) {
//            readData();
//        }
    }


    @Override
    public void showOne(WorkFlowArea workFlowArea) {


        WorkFlowAreaUpdateDialog dialog = new WorkFlowAreaUpdateDialog(SwingUtilities.getWindowAncestor(this), workFlowArea);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowArea result = dialog.getResult();
        if (result != null) {
            readData();
        }

    }


    @Override
    public void addOneArea() {


        WorkFlowAreaUpdateDialog dialog = new WorkFlowAreaUpdateDialog(SwingUtilities.getWindowAncestor(this),null);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowArea result = dialog.getResult();
        if (result != null) {
            readData();
        }


    }
}
