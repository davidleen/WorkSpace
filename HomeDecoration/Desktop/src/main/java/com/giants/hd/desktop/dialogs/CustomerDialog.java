package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.CustomerModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.TableMenuAdapter;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.exception.HdException;
import com.giants3.report.ResourceUrl;
import com.google.inject.Inject;
import net.sf.jasperreports.engine.export.Cut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerDialog extends BaseSimpleDialog<Customer>
{
    private JPanel contentPane;
    private JButton btn_add;
    private JHdTable tb;



    @Inject
    CustomerModel model;
    @Inject
    ApiManager apiManager;

    public CustomerDialog(Window window) {
        super(window);

    }


    @Override
    public void init() {
        setContentPane(contentPane);
        setTitle("客户列表");
        tb.setModel(model);
        setMinimumSize(new Dimension(600, 400));
        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    int row = tb.getSelectedRow();
                    Customer customer = model.getItem(row);


                    int column = tb.convertColumnIndexToModel(tb.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showDialog(CustomerDialog.this, ResourceUrl.completeUrl(customer.nameCardFileUrl));
                    }

                }
            }
        });

    }



    @Override
    protected RemoteData<Customer> readData() throws HdException {
        return apiManager.readCustomers();
    }

    @Override
    protected BaseTableModel<Customer> getTableModel() {
        return model;
    }


    @Override
    protected RemoteData<Customer> saveData(List<Customer> datas) throws HdException {
        return apiManager.saveCustomers(datas);
    }

    @Override
    public void doSomethingOnError(RemoteData<Customer> data) {
        doLoadWork();
    }
}
