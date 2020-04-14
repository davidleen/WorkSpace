package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.OrderReportItemTableModel;
import com.giants.hd.desktop.mvp.presenter.OrderReportIPresenter;
import com.giants.hd.desktop.mvp.viewer.OrderReportViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.OrderReportItem;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 订单报表界面实现层
 * Created by davidleen29 on 2016/8/8.
 */
public class Panel_Order_Report extends BasePanel implements OrderReportViewer {

    private final OrderReportIPresenter orderReportPresenter;

    OrderReportItemTableModel orderReportItemTableModel;
    private JPanel root;
    private Panel_Page pagePanel;
    private JHdTable orderTable;
    private JButton btn_search;

    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;
    private JComboBox cb_sales;
    private JButton export;

    public Panel_Order_Report(final OrderReportIPresenter orderReportPresenter) {

        this.orderReportPresenter = orderReportPresenter;

        orderReportItemTableModel = new OrderReportItemTableModel();
        orderTable.setModel(orderReportItemTableModel);
        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String startDate = date_start.getJFormattedTextField().getText().toString();
                String endDate = date_end.getJFormattedTextField().getText().toString();

                  User user= (User) cb_sales.getSelectedItem();

                orderReportPresenter.search(user.id, startDate, endDate, 0, pagePanel.getPageSize());


            }
        });

        date_start.getJFormattedTextField().setText("2017-01-01");
        try {
            date_end.getJFormattedTextField().setText(new HdDateComponentFormatter().valueToString(Calendar.getInstance()));
        } catch (ParseException e) {
            date_end.getJFormattedTextField().setText("2020-01-01");
        }


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                if (orderReportPresenter != null) {
                    String startDate = date_start.getJFormattedTextField().getText().toString();
                    String endDate = date_end.getJFormattedTextField().getText().toString();

                    User user= (User) cb_sales.getSelectedItem();
                    orderReportPresenter.search(user.id, startDate, endDate, pageIndex, pageSize);
                }
            }
        });

        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

//                  int[] row=  JTableUtils.getSelectedRowSOnModel(orderTable);
//                    if(row!=null&&row.length>=1)
//                    {
//
//                        ErpOrderItem order= orderItemTableModel.getItem(row[0]);
//                        orderReportPresenter.loadDetail(order);
//                    }


                }
            }
        });


        User all = new User();
        all.id = -1;
        all.code = "--";
        all.name = "--";
        all.chineseName = "所有人";
        cb_sales.addItem(all);


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
            cb_sales.addItem(user);
//        for (User user : CacheManager.getInstance().bufferData.salesmans) {
//            cb_sales.addItem(user);
//        }

        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int column = orderTable.convertColumnIndexToModel(orderTable.getSelectedColumn());
                    int selectRow = orderTable.convertRowIndexToModel(orderTable.getSelectedRow());
                    OrderReportItem item = orderReportItemTableModel.getItem(selectRow);
                    if (item == null) return;
                    if (StringUtils.isEmpty(item.url)) return;

                    //单击第一列 显示原图
                    if (column == 3) {

                            ImageViewDialog.showDialog(getWindow(getRoot()), HttpUrl.loadPicture(item.url), item.prd_no);

                    }
                }
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderReportPresenter.export();
            }
        });
    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void setData(RemoteData<OrderReportItem> erpOrderRemoteData) {
        if (erpOrderRemoteData.isSuccess()) {
      orderReportItemTableModel.setDatas(erpOrderRemoteData.datas);
        } else
            showMesssage(erpOrderRemoteData.message);
        pagePanel.bindRemoteData(erpOrderRemoteData);
    }

    private void createUIComponents() {
        JDatePanelImpl picker = new JDatePanelImpl(null);
        date_start = new JDatePickerImpl(picker, new HdDateComponentFormatter());

        picker = new JDatePanelImpl(null);
        date_end = new JDatePickerImpl(picker, new HdDateComponentFormatter());
    }
}
