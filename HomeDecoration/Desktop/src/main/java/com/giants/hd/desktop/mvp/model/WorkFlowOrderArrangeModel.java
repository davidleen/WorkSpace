package com.giants.hd.desktop.mvp.model;

import com.giants.hd.desktop.mvp.IModel;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2017/4/30.
 */
public class WorkFlowOrderArrangeModel implements IModel {


    private OrderItemWorkFlow orderItemWorkFlow;
    private WorkFlowProduct workFlowProduct;
    private List<OutFactory> outFactories;
    private ErpOrderItem erpOrderItem;

    public void setOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow) {
        this.orderItemWorkFlow = orderItemWorkFlow;
        bindProductWorkFowIfNew();


    }


    public void setProductWorkFlow(WorkFlowProduct workFlowProduct)
    {
        this.workFlowProduct = workFlowProduct;
        bindProductWorkFowIfNew();
    }





    public void setFactories(List<OutFactory> outFactories)
    {


        this.outFactories = outFactories;
    }

    public OrderItemWorkFlow getOrderItemWorkFlow() {
        return orderItemWorkFlow;
    }

    public WorkFlowProduct getWorkFlowProduct() {
        return workFlowProduct;
    }

    public List<OutFactory> getOutFactories() {
        return outFactories;
    }


    /**
     * 产品流程信息重新设置到订单款项

     */
    public  void  updateProductWorkFlowToOrder( )
    {

        if(workFlowProduct==null||orderItemWorkFlow==null) return ;


        orderItemWorkFlow.productFullName=workFlowProduct.productName+(StringUtils.isEmpty(workFlowProduct.productPVersion)?"":"-"+workFlowProduct.productPVersion);


        if (workFlowProduct.produceType == ProduceType.PURCHASE) {
            final List<WorkFlow> workFlows = null;
            int size = workFlows.size();
            List<WorkFlow> purchaseWorkFlowList = new ArrayList<>();
            List<WorkFlow> selfMadeWorkFlowList = new ArrayList<>();
            for (int i = 0; i < size; i++) {

                final WorkFlow workFlow = workFlows.get(i);
                if (workFlow.isSelfMade)
                    selfMadeWorkFlowList.add(workFlow);
                if (workFlow.isPurchased)
                    purchaseWorkFlowList.add(workFlow);
            }


            size = purchaseWorkFlowList.size();
            int[] workflowSteps = new int[size];
            String[] workflowNames = new String[size];
            for (int i = 0; i < size; i++) {
                workflowSteps[i] = purchaseWorkFlowList.get(i).flowStep;
                workflowNames[i] = purchaseWorkFlowList.get(i).name;
            }
            orderItemWorkFlow.workFlowSteps = StringUtils.combine(workflowSteps);
            orderItemWorkFlow.workFlowNames = StringUtils.combine(workflowNames);

        } else {


            orderItemWorkFlow.workFlowSteps = workFlowProduct.workFlowSteps;
            orderItemWorkFlow.workFlowNames = workFlowProduct.workFlowNames;

            orderItemWorkFlow.produceType = workFlowProduct.produceType;
            if (workFlowProduct.produceType == ProduceType.SELF_MADE) {

                orderItemWorkFlow.productTypes = workFlowProduct.productTypes;
                orderItemWorkFlow.productTypeNames = workFlowProduct.productTypeNames;
                orderItemWorkFlow.configs = workFlowProduct.configs;

                orderItemWorkFlow.groupStep = workFlowProduct.groupStep;


            }
        }


    }

    /**
     * 新订单流程下自动绑定产品信息
     */
    private void bindProductWorkFowIfNew() {
        if(workFlowProduct==null||orderItemWorkFlow==null) return ;
        if(orderItemWorkFlow.id<=0)

            updateProductWorkFlowToOrder();

    }

    public void setOrderItem(ErpOrderItem erpOrderItem) {

        this.erpOrderItem = erpOrderItem;
    }

    public ErpOrderItem getOrderItem() {
        return erpOrderItem;
    }
}
