package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.OrderListAdapter;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.OrderTableModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.ErpOrder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单面板  presenter
 * Created by david on 2016/3/29.
 */
public class Panel_Order_List extends BasePanel {
    private JPanel panel1;
    private JButton btn_search;
    private JTextField keys;
    private JHdTable orderTable;
    private Panel_Page pagePanel;
    private JComboBox cb_salesman;

    OrderTableModel orderTableModel;

    private OrderListAdapter adapter;

    public Panel_Order_List(OrderListAdapter mAdapter) {
        this.adapter = mAdapter;
        orderTableModel = new OrderTableModel();
        orderTable.setModel(orderTableModel);

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {

                adapter.search(keys.getText().trim(), getSalesId(), pageIndex, pageSize);
            }
        });

        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                adapter.search(keys.getText().trim(), getSalesId(), 0, pagePanel.getPageSize());
            }
        });
        keys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                adapter.search(keys.getText().trim(), getSalesId(), 0, pagePanel.getPageSize());
            }
        });


        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = orderTable.getSelectedRow();
                    int modelRow = orderTable.convertRowIndexToModel(row);
                    ErpOrder erpOrder = orderTableModel.getItem(modelRow);
                    adapter.loadOrderDetail(erpOrder);

                }
            }
        });
        User all = new User();
        all.id = -1;
        all.code = "--";
        all.name = "--";
        all.chineseName = "所有人";
        cb_salesman.addItem(all);


        List<User> sales = new ArrayList<>();
        if (CacheManager.getInstance().bufferData.loginUser.isAdmin()) {

            sales.addAll(CacheManager.getInstance().bufferData.salesmans);
        } else {
            String relateSales = CacheManager.getInstance().bufferData.orderAuth.relatedSales;

            String[] ids = StringUtils.isEmpty(relateSales) ? null : relateSales.split(",|，");
            if (ids != null) {
                for (User user : CacheManager.getInstance().bufferData.salesmans) {

                    boolean find = false;
                    for (String s : ids) {
                        if (String.valueOf(user.id).equals(s)) {
                            find = true;
                            break;
                        }
                    }

                    if (find)
                        sales.add(user);

                }

            }

        }
        for (User user : sales)
            cb_salesman.addItem(user);


    }

    private long getSalesId() {
        return ((User) cb_salesman.getSelectedItem()).id;
    }

    public void setData(RemoteData<ErpOrder> remoteData) {

        pagePanel.bindRemoteData(remoteData);
        orderTableModel.setDatas(remoteData.datas);
    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }
}
