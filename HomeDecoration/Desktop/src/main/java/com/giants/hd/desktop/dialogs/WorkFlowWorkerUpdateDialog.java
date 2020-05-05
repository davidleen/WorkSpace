package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.OutFactoryModel;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerUpdateViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlowWorkerUpdate;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowWorker;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 产品关联的生产流程数据
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowWorkerUpdateDialog extends BaseMVPDialog<WorkFlowWorker, WorkFlowWorkerUpdateViewer> implements WorkFlowWorkerUpdateIPresenter {


    public static final String DIVIDER = ",";
    private String oldData;
    private WorkFlowWorker data;
    private WorkFlowWorker workFlowWorker;


    public WorkFlowWorkerUpdateDialog(Window window, WorkFlowWorker workFlowWorker) {
        super(window, "流程工作人员配置");
        this.workFlowWorker = workFlowWorker;
        setMinimumSize(new Dimension(600, 400));


//        getViewer().bindWorkFlows(ErpWorkFlow.WorkFlows);


        loadUsers();


    }

    private void loadUsers() {
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

                } else {
                    getViewer().showMesssage(remoteData.message);
                }


            }


        });
        getViewer().showLoadingDialog();
    }

    @Override
    public void pickjgh() {


//        OutFactoryDialog dialog = new OutFactoryDialog(this);
//        dialog.setLocationRelativeTo(getRootPane());
//        dialog.setVisible(true);


        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.getOutFactories(), OutFactory.class).execute(new RemoteDataSubscriber<OutFactory>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<OutFactory> data) {

                java.util.List<OutFactory> preItems = null;
                if (workFlowWorker.jghncodes != null) {
                    String[] split = workFlowWorker.jghncodes.split(DIVIDER);
                    if (split.length > 0) {
                        preItems = new ArrayList<>();
                        for (OutFactory outFactory : data.datas) {
                            for (String s : split) {
                                if (outFactory.dep.equalsIgnoreCase(s)) {
                                    preItems.add(outFactory);
                                    break;
                                }
                            }

                        }
                    }
                }


                ItemPickDialog<OutFactory> itemPickDialog = new ItemPickDialog(WorkFlowWorkerUpdateDialog.this, "挑选加工户", data.datas, preItems, new OutFactoryModel());
                itemPickDialog.setMinimumSize(new Dimension(600, 600));
                itemPickDialog.setVisible(true);
                java.util.List<OutFactory> result = itemPickDialog.getResult();
                if (result != null) {


                    StringBuilder names = new StringBuilder();
                    StringBuilder codes = new StringBuilder();
                    int size = result.size();
                    for (OutFactory outFactory : result) {
                        codes.append(outFactory.dep)
                                .append(DIVIDER);
                        names.append(outFactory.name).append(DIVIDER);
                    }
                    if (size > 0) {
                        names.setLength(names.length() - DIVIDER.length());
                        codes.setLength(codes.length() - DIVIDER.length());

                    }

                    workFlowWorker.jghnames = names.toString();
                    workFlowWorker.jghncodes = codes.toString();
                    getViewer().bindData(workFlowWorker);


                }
            }


        });
        getViewer().showLoadingDialog();


    }

    @Override
    public void delete() {


        int res = JOptionPane.showConfirmDialog(this, "是否删除当前流程人员？", "删除数据", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.YES_OPTION) {
            return;
        }

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
