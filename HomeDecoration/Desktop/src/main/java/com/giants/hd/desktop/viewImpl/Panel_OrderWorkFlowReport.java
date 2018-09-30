package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.OrderItemWorkFlowStateModel;
import com.giants.hd.desktop.model.OrderItemWorkFlowTableModel;
import com.giants.hd.desktop.mvp.presenter.OrderWorkFlowReportIPresenter;
import com.giants.hd.desktop.mvp.viewer.OrderWorkFlowReportViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpOrderItemProcess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by davidleen29 on 2016/9/25.
 */
public class Panel_OrderWorkFlowReport extends BasePanel implements OrderWorkFlowReportViewer {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private JHdTable jt_undone;
    private JHdTable jt_work;
    private JTextField tf_key;
    private JButton search;
    private Panel_Page pagePanel;
    private OrderWorkFlowReportIPresenter presenter;

    OrderItemWorkFlowStateModel unCompleteModel;
    OrderItemWorkFlowTableModel workFlowTableModel;
    public Panel_OrderWorkFlowReport(final OrderWorkFlowReportIPresenter presenter) {
        this.presenter = presenter;

        unCompleteModel = new OrderItemWorkFlowStateModel();
        unCompleteModel.setRowAdjustable(false);

        jt_undone.setModel(unCompleteModel);

        workFlowTableModel=new OrderItemWorkFlowTableModel();
        jt_work.setModel(workFlowTableModel);
        workFlowTableModel.setRowAdjustable(false);


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                if (presenter != null) {
                    String key = tf_key.getText().trim();
                    presenter.search(key, pageIndex, pageSize);
                }
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String key = tf_key.getText().trim();
                presenter.search(key, 0, pagePanel.getPageSize());
            }
        });
        tf_key.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String key = tf_key.getText().trim();
                presenter.search(key, 0, pagePanel.getPageSize());
            }
        });
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
    public void setData(RemoteData<ErpOrderItem> remoteData) {

        if (remoteData.isSuccess()) {
            workFlowTableModel.setDatas(remoteData.datas);
            pagePanel.bindRemoteData(remoteData);
        }else
        {
            showMesssage(remoteData.message);
        }
    }


    @Override
    public void setUnCompleteData(RemoteData<ErpOrderItemProcess> remoteData) {



        if (remoteData.isSuccess()) {
            unCompleteModel.setDatas(remoteData.datas);
        }else
        {
            showMesssage(remoteData.message);
        }
    }
}
