package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerUpdateViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlowWorkerUpdate;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowWorker;
import rx.Subscriber;

import java.awt.*;

/**
 * 产品关联的生产流程数据
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowWorkerUpdateDialog extends BaseMVPDialog<WorkFlowWorker, WorkFlowWorkerUpdateViewer> implements WorkFlowWorkerUpdateIPresenter {


    private String oldData;
    private WorkFlowWorker data;
    private WorkFlowWorker workFlowWorker;


    public WorkFlowWorkerUpdateDialog(Window window, WorkFlowWorker workFlowWorker) {
        super(window, "流程工作人员配置");
        this.workFlowWorker = workFlowWorker;
        setMinimumSize(new Dimension(400, 400));


//        getViewer().bindWorkFlows(ErpWorkFlow.WorkFlows);


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

    private void setData(WorkFlowWorker aWorkFlowWorker) {

        if (aWorkFlowWorker == null) {
            aWorkFlowWorker = new WorkFlowWorker();
        }
        oldData = GsonUtils.toJson(aWorkFlowWorker);

        data = aWorkFlowWorker;

        getViewer().bindData(data);


    }


    public void readData() {

//        UseCaseFactory.getInstance().createGetWorkFlowOfProduct(productId).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
//            @Override
//            public void onCompleted() {
//                workFlowViewer.hideLoadingDialog();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                workFlowViewer.hideLoadingDialog();
//                workFlowViewer.showMesssage(e.getMessage());
//            }
//
//
//            @Override
//            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {
//                workFlowViewer.hideLoadingDialog();
//                if (workFlowRemoteData.isSuccess()) {
//
//                    if (workFlowRemoteData.totalCount > 0)
//                        setData(workFlowRemoteData.datas.get(0));
//                    else {
//
//                        final WorkFlowProduct data = new WorkFlowProduct();
//                        data.productId = productId;
//                        setData(data);
//                        workFlowViewer.showMesssage("该产品未建立生产进度信息");
//                        return;
//                    }
//                }
//
//
//            }
//
//
//        });
        getViewer().showLoadingDialog();
    }




    @Override
    public void save() {


        getViewer().getData(data);


        if (!hasModifyData()) {
            getViewer().showMesssage("数据无改动");
            return;
        }

        UseCaseFactory.getInstance().createSaveWorkFlowWorkerUseCase(data).execute(new Subscriber<RemoteData<WorkFlowWorker>>() {
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
            public void onNext(RemoteData<WorkFlowWorker> remoteData) {
                getViewer().hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    final WorkFlowWorker workFlowProduct = remoteData.datas.get(0);
                    setData(workFlowProduct);
                    setResult(workFlowProduct);
                    getViewer().showMesssage("保存成功");
                    dispose();

                }else
                {
                    getViewer().showMesssage(remoteData.message);
                }


            }


        });
        getViewer().showLoadingDialog();
    }

    @Override
    public void delete() {

        UseCaseFactory.getInstance().createDeleteWorkFlowWorkerUseCase(data.id).execute(new Subscriber<RemoteData<Void>>() {
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
    protected WorkFlowWorkerUpdateViewer createViewer() {
        return new Panel_WorkFlowWorkerUpdate(this);
    }
}
