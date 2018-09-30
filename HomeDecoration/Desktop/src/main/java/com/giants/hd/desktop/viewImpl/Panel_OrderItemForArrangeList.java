package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.OrderItemArrangeTableModel;
import com.giants.hd.desktop.mvp.presenter.OrderItemForArrangeListIPresenter;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.mvp.viewer.OrderItemForArrangeListViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by davidleen29 on 2016/9/25.
 */
public class Panel_OrderItemForArrangeList extends BasePanel implements OrderItemForArrangeListViewer {
    private JPanel root;
    private JTabbedPane tabbedPane1;

    private JHdTable jt;
    private JTextField tf_key;
    private JButton search;
    private Panel_Page pagePanel;
    private OrderItemForArrangeListIPresenter presenter;

    OrderItemArrangeTableModel unCompleteModel;

    public Panel_OrderItemForArrangeList(final OrderItemForArrangeListIPresenter presenter) {
        this.presenter = presenter;

        unCompleteModel = new OrderItemArrangeTableModel();




        jt.setModel(unCompleteModel);
        unCompleteModel.setRowAdjustable(false);


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
        jt.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2)
                {
                    int row= JTableUtils.getFirstSelectedRowSOnModel(jt);
                    if(row>-1)
                    {

                        ErpOrderItem erpOrderItem=unCompleteModel.getItem(row);
                        presenter.onListItemClick(erpOrderItem) ;


                    }
                }
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
            unCompleteModel.setDatas(remoteData.datas);
            pagePanel.bindRemoteData(remoteData);
        }else
        {
            showMesssage(remoteData.message);
        }
    }



}
