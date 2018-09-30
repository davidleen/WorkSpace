package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.*;

import java.util.List;

/**
 * Created by davidleen29 on 2017/1/11.
 */
public interface OrderItemWorkFlowViewer  extends IViewer {


    List<OutFactory> getArrangedFactories() throws Exception;

      void     setProductTypes(String[] productTypes, String[] productTypeNames, List<OutFactory> outFactories);

    void bindOrderData(ErpOrderItem erpOrderItem);

    void setOutFactories(List<OutFactory> outFactories);

    OutFactory getProduceFactory();

    List<WorkFlow> getSelectedWorkFlows();





    void bindOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow);
}
