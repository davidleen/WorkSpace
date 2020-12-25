package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.noEntity.RemoteData;

import java.util.List;

/**
 * 产品关联的生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowQueryViewer extends IViewer {


    public void bindData(RemoteData<ErpOrderItem> states);

    void bindAlertData(RemoteData<ErpOrderItem> data);
}
