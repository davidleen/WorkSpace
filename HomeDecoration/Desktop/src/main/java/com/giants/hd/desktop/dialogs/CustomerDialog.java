package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.CustomerModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.TableMenuAdapter;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerDialog extends BaseSimpleDialog<Customer>
{
    private JPanel contentPane;
    private JButton btn_save;
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
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                doSaveWork();
            }
        });
        String[] menu=new String[]{"删除"};
        tb.addMouseListener(new TableMenuAdapter(tb, menu, new TableMenuAdapter.TableMenuListener() {
            @Override
            public void onMenuClick(JTable table, int index) {

                if(index==0)
                {int row=table.getSelectedRow();
                    int modelRow=  table.convertRowIndexToModel(row);

                    model.deleteRow(modelRow);

                }

            }
        }));
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
