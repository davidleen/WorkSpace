package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.ErpOrderItemProcessTableModel;
import com.giants.hd.desktop.model.ErpWorkFlowReportTableModel;
import com.giants.hd.desktop.mvp.ErpOrderItemStateMVP;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlowReport;

import javax.swing.*;

/**
 * Created by davidleen29 on 20170513
 */
public class Panel_ErpOrderItemStatesList extends BasePanel implements ErpOrderItemStateMVP.Viewer {
    private JPanel root;


    private JHdTable jt;
    private JHdTable reportTable;


    private ErpOrderItemStateMVP.Presenter presenter;

    ErpOrderItemProcessTableModel tableModel;
    ErpWorkFlowReportTableModel reportTableModel;

    public Panel_ErpOrderItemStatesList(final ErpOrderItemStateMVP.Presenter presenter) {
        this.presenter = presenter;

        tableModel = new ErpOrderItemProcessTableModel();

        reportTableModel=new ErpWorkFlowReportTableModel();


        jt.setModel(tableModel);
        tableModel.setRowAdjustable(false);

        reportTable.setModel(reportTableModel);
        reportTableModel.setRowAdjustable(false);


    }

    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return root;
    }


    @Override
    public void setData(RemoteData<ErpOrderItemProcess> remoteData) {

        if (remoteData.isSuccess()) {
            tableModel.setDatas(remoteData.datas);
        } else {
            showMesssage(remoteData.message);
        }
    }


    @Override
    public void setReportData(RemoteData<ErpWorkFlowReport> workFlowReportRemoteData) {


            reportTableModel.setDatas(workFlowReportRemoteData.datas);

    }
}
