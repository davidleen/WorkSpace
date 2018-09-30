package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.model.WorkFlowOrderArrangeModel;
import com.giants.hd.desktop.mvp.presenter.OrderItemWorkFlowIPresenter;
import com.giants.hd.desktop.mvp.viewer.OrderItemWorkFlowViewer;
import com.giants.hd.desktop.viewImpl.Panel_OrderItemWorkFlow;
import com.giants3.hd.domain.BaseSubscriber;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.*;
import com.giants3.hd.entity.*;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * d订单排产界面
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowOrderArrangeDialog extends MVPDialog<OrderItemWorkFlow, OrderItemWorkFlowViewer, WorkFlowOrderArrangeModel> implements OrderItemWorkFlowIPresenter {

    OrderItemWorkFlowViewer workFlowViewer;


    private String oldData;
    private WorkFlowProduct data;


    public WorkFlowOrderArrangeDialog(Window window, ErpOrderItem erpOrderItem) {
        super(window, "订单生产流程配置");

        workFlowViewer = getViewer();

        getModel().setOrderItem(erpOrderItem);


        setMinimumSize(new Dimension(800, 600));
        readOutFactoryData();
        loadProductWorkFlow(erpOrderItem.productId);

        workFlowViewer.bindOrderData(erpOrderItem);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                readOrderItemWorkFlow(getModel().getOrderItem());
            }
        });


    }

    private void readOrderItemWorkFlow(final ErpOrderItem erpOrderItem) {

        UseCaseFactory.getInstance().createGetOrderItemWorkFlow(erpOrderItem.id).execute(new BaseSubscriber<RemoteData<OrderItemWorkFlow>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                super.onError(e);


                e.printStackTrace();

            }


            @Override
            public void onNext(RemoteData<OrderItemWorkFlow> data) {


                OrderItemWorkFlow orderItemWorkFlow;
                if (data.isSuccess() && data.datas != null && data.datas.size() > 0) {

                    orderItemWorkFlow = data.datas.get(0);


                } else {
                    orderItemWorkFlow = new OrderItemWorkFlow();
                    orderItemWorkFlow.orderName = erpOrderItem.os_no;
                    orderItemWorkFlow.productFullName = erpOrderItem.prd_name;
                    orderItemWorkFlow.orderItemId = erpOrderItem.id;
                    orderItemWorkFlow.orderItemIndex = erpOrderItem.itm;


                }
                getModel().setOrderItemWorkFlow(orderItemWorkFlow);
                bindOrderItemWorkFlow();
            }

        });


    }


    /**
     * 检查产品是否已经配置流程
     */
    private void loadProductWorkFlow(final long productId) {

        UseCaseFactory.getInstance().createGetWorkFlowOfProduct(productId).execute(new BaseSubscriber<RemoteData<WorkFlowProduct>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                super.onError(e);


            }


            @Override
            public void onNext(RemoteData<WorkFlowProduct> data) {

                WorkFlowProduct workFlowProduct = null;
                if (data.isSuccess() && data.datas.size() > 0) {

                    workFlowProduct = data.datas.get(0);

                }


                setWorkFlowProduct(workFlowProduct);


            }

        });


    }

    private void setWorkFlowProduct(WorkFlowProduct workFlowProduct) {


        getModel().setProductWorkFlow(workFlowProduct);
        bindOrderItemWorkFlow();


    }


    private void bindOrderItemWorkFlow() {


        final OrderItemWorkFlow orderItemWorkFlow = getModel().getOrderItemWorkFlow();
        final List<OutFactory> outFactories = getModel().getOutFactories();
        if (outFactories == null || orderItemWorkFlow == null) return;

        workFlowViewer.setOutFactories(outFactories);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {


                if (orderItemWorkFlow.produceType == ProduceType.SELF_MADE) {
                    final String[] productTypes = orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
                    final String[] productTypeNames = orderItemWorkFlow.productTypeNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);
                    workFlowViewer.setProductTypes(productTypes, productTypeNames, outFactories);
                }
                workFlowViewer.bindOrderItemWorkFlow(orderItemWorkFlow);
            }
        });
    }

    /**
     * 读取外厂家数据
     */
    private void readOutFactoryData() {

        UseCaseFactory.getInstance().createGetOutFactoryUseCase().execute(new Subscriber<RemoteData<OutFactory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OutFactory> data) {


                if (data.isSuccess()) {
                    getModel().setFactories(data.datas);
                    bindOrderItemWorkFlow();
                }
            }

        });

    }


    @Override
    public boolean hasModifyData() {

        return data != null && !GsonUtils.toJson(data).equals(oldData);
    }

    @Override
    public void save() {


        OrderItemWorkFlow orderItemWorkFlow = getModel().getOrderItemWorkFlow();
        WorkFlowProduct workFlowProduct = getModel().getWorkFlowProduct();
        if (orderItemWorkFlow == null) return;


        final boolean isSelfMade = orderItemWorkFlow.produceType == ProduceType.SELF_MADE;

        if (!isSelfMade) {


            List<WorkFlow> selectWorkFlows = workFlowViewer.getSelectedWorkFlows();

            int size = selectWorkFlows.size();
            StringBuilder workFlowSteps = new StringBuilder();
            StringBuilder workFlowNames = new StringBuilder();

            for (int i = 0; i < size; i++) {

                WorkFlow workFlow = selectWorkFlows.get(i);
                workFlowSteps.append(workFlow.flowStep).append(ConstantData.STRING_DIVIDER_SEMICOLON);
                workFlowNames.append(workFlow.name).append(ConstantData.STRING_DIVIDER_SEMICOLON);


            }
            orderItemWorkFlow.workFlowSteps = workFlowSteps.toString();
            orderItemWorkFlow.workFlowNames = workFlowNames.toString();

            orderItemWorkFlow.configs = "";
        } else {
            orderItemWorkFlow.workFlowSteps = workFlowProduct.workFlowSteps;
            orderItemWorkFlow.workFlowNames = workFlowProduct.workFlowNames;
            orderItemWorkFlow.configs = workFlowProduct.configs;
        }


        if (isSelfMade) {


            //获取配体厂家
            List<OutFactory> factories;
            try {
                factories = workFlowViewer.getArrangedFactories();
            } catch (Exception e) {
                e.printStackTrace();
                workFlowViewer.showMesssage(e.getMessage());
                return;
            }
            StringBuilder conceptusFactoryIds = new StringBuilder();
            StringBuilder conceptusFactoryNames = new StringBuilder();

            for (OutFactory factory : factories) {
                conceptusFactoryIds.append(factory.dep).append(StringUtils.STRING_SPLIT_SEMICOLON);
                conceptusFactoryNames.append(factory.name).append(StringUtils.STRING_SPLIT_SEMICOLON);

            }
            if (conceptusFactoryIds.length() > 0) {
                conceptusFactoryIds.setLength(conceptusFactoryIds.length() - 1);
                conceptusFactoryNames.setLength(conceptusFactoryNames.length() - 1);
            }


            orderItemWorkFlow.conceptusFactoryIds = conceptusFactoryIds.toString();
            orderItemWorkFlow.conceptusFactoryNames = conceptusFactoryNames.toString();

        } else {
            orderItemWorkFlow.conceptusFactoryIds = "";
            orderItemWorkFlow.conceptusFactoryNames = "";
        }


        orderItemWorkFlow.produceType = workFlowProduct.produceType;


        OutFactory productFactory = workFlowViewer.getProduceFactory();

        orderItemWorkFlow.produceFactoryId = isSelfMade ? "" : productFactory.dep;
        orderItemWorkFlow.produceFactoryName = isSelfMade ? "" : productFactory.name;


        UseCaseFactory.getInstance().createStartOrderItemWorkFlowUseCase(orderItemWorkFlow).execute(new Subscriber<RemoteData<OrderItemWorkFlow>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OrderItemWorkFlow> data) {

                workFlowViewer.hideLoadingDialog();

                if (data.isSuccess() && data.datas.size() > 0) {
                    setResult(data.datas.get(0));

                    workFlowViewer.showMesssage("排厂成功！");
                    setVisible(false);

                }

            }

        });


        workFlowViewer.showLoadingDialog("正在启动订单生产流程。。。");


    }

    @Override
    public void reimportProduct() {


        getModel().updateProductWorkFlowToOrder();
        workFlowViewer.bindOrderItemWorkFlow(getModel().getOrderItemWorkFlow());


    }


    @Override
    protected OrderItemWorkFlowViewer createViewer() {
        return new Panel_OrderItemWorkFlow(this);
    }

    @Override
    protected WorkFlowOrderArrangeModel createModel() {
        return new WorkFlowOrderArrangeModel();
    }


    /**
     * 全部撤销排厂
     */
    @Override
    public void cancelArrange() {


        OrderItemWorkFlow orderItemWorkFlow = getModel().getOrderItemWorkFlow();
        if (orderItemWorkFlow.id <= 0) {
            //
            viewer.showMesssage("订单未配置生产流程");
            return;
        }

        if (!viewer.showConfirmMessage("该操作会删除所有排厂信息，并重新排厂")) return;


        UseCaseFactory.getInstance().CreateCancelOrderWorkFlowUseCase(orderItemWorkFlow.id).execute(new Subscriber<RemoteData<OrderItemWorkFlow>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OrderItemWorkFlow> data) {

                workFlowViewer.hideLoadingDialog();

                if (data.isSuccess()) {

                    workFlowViewer.showMesssage("排厂撤销成功！");


                }

            }

        });


        workFlowViewer.showLoadingDialog("正在启动订单生产流程。。。");


    }
}
