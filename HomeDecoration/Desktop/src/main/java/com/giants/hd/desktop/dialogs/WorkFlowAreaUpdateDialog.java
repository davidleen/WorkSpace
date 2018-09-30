package com.giants.hd.desktop.dialogs;


import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.WorkFlowAreaUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowAreaUpdateViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlowAreaUpdate;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.WorkFlowArea;

import java.awt.*;

/**
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowAreaUpdateDialog extends BaseMVPDialog<WorkFlowArea, WorkFlowAreaUpdateViewer> implements WorkFlowAreaUpdateIPresenter {


    private String oldData;
    private WorkFlowArea data;
    private WorkFlowArea workFlowArea;


    public WorkFlowAreaUpdateDialog(Window window, WorkFlowArea workFlowArea) {
        super(window, "交接区域");
        this.workFlowArea = workFlowArea;
        setMinimumSize(new Dimension(400, 400));

        setData(workFlowArea);

    }

    private void setData(WorkFlowArea aWorkFlowArea) {

        if (aWorkFlowArea == null) {
            aWorkFlowArea = new WorkFlowArea();
        }
        oldData = GsonUtils.toJson(aWorkFlowArea);

        data = aWorkFlowArea;
        getViewer().bindData(aWorkFlowArea);


    }


    @Override
    public void save() {


        getViewer().getData(data);
        if (!hasModifyData()) {
            getViewer().showMesssage("数据无改动");
            return;
        }

        UseCaseFactory.getInstance().createSaveWorkFlowAreaUseCase(data).execute(new RemoteDataSubscriber<WorkFlowArea>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<WorkFlowArea> data) {
                final WorkFlowArea workFlowArea = data.datas.get(0);
                setData(workFlowArea);
                setResult(workFlowArea);
                getViewer().showMesssage("保存成功");
            }


        });
        getViewer().showLoadingDialog();
    }

    @Override
    public void delete() {
        UseCaseFactory.getInstance().createDeleteWorkFlowAreaUseCase(data.id).execute(new RemoteDataSubscriber<Void>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<Void> remoteData) {
                setResult(data);
                getViewer().showMesssage("删除成功");
                dispose();
            }


        });
        getViewer().showLoadingDialog();


    }

    @Override
    public boolean hasModifyData() {

        return data != null && !GsonUtils.toJson(data).equals(oldData);
    }

    @Override
    protected WorkFlowAreaUpdateViewer createViewer() {
        return new Panel_WorkFlowAreaUpdate(this);
    }
}
