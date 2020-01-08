package com.giants.hd.desktop.dialogs;


import com.giants.hd.desktop.mvp.presenter.WorkFlowArrangerUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowArrangerUpdateViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlowArrangerUpdate;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowArranger;
import rx.Subscriber;

import java.awt.*;

/**
 * 产品关联的生产流程数据
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowArrangerUpdateDialog extends BaseMVPDialog<WorkFlowArranger, WorkFlowArrangerUpdateViewer> implements WorkFlowArrangerUpdateIPresenter {


    private String oldData;
    private WorkFlowArranger data;
    private WorkFlowArranger workFlowWorker;


    public WorkFlowArrangerUpdateDialog(Window window, WorkFlowArranger workFlowWorker) {
        super(window, "流程工作人员配置");
        this.workFlowWorker = workFlowWorker;
        setMinimumSize(new Dimension(600, 400));

        loadUsers();

    }
    private void loadUsers()
    {
        UseCaseFactory.getInstance().createGetUserListUseCase().execute(new Subscriber<java.util.List<User>>() {
            @Override
            public void onCompleted() {
                getViewer().hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<User> remoteData) {

                getViewer().hideLoadingDialog();
                getViewer().bindUsers(remoteData);
                setData(workFlowWorker);


            }
        });

    }
    private void setData(WorkFlowArranger aWorkFlowArranger) {

        if (aWorkFlowArranger == null) {
            aWorkFlowArranger = new WorkFlowArranger();
        }
        oldData = GsonUtils.toJson(aWorkFlowArranger);

        data = aWorkFlowArranger;

        getViewer().bindData(data);


    }


    @Override
    public void save() {


        getViewer().getData(data);


        if (!hasModifyData()) {
            getViewer().showMesssage("数据无改动");
            return;
        }

        UseCaseFactory.getInstance().createSaveWorkFlowArrangerUseCase(data).execute(new Subscriber<RemoteData<WorkFlowArranger>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlowArranger> remoteData) {
                getViewer().hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    final WorkFlowArranger arranger = remoteData.datas.get(0);
                    setData(arranger);
                    setResult(arranger);
                    getViewer().showMesssage("保存成功");
                    dispose();

                } else {
                    getViewer().showMesssage(remoteData.message);
                }


            }


        });
        getViewer().showLoadingDialog();
    }

    @Override
    public void delete() {

        UseCaseFactory.getInstance().createDeleteWorkFlowArrangerUseCase(data.id).execute(new Subscriber<RemoteData<Void>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<Void> remoteData) {
                getViewer().hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    setResult(data);
                    getViewer().showMesssage("删除成功");
                    dispose();

                } else {
                    getViewer().showMesssage(remoteData.message);
                }


            }


        });
        getViewer().showLoadingDialog();


    }

    @Override
    public boolean hasModifyData() {

        return data != null && !GsonUtils.toJson(data).equals(oldData);
    }

    @Override
    protected WorkFlowArrangerUpdateViewer createViewer() {
        return new Panel_WorkFlowArrangerUpdate(this);
    }
}
