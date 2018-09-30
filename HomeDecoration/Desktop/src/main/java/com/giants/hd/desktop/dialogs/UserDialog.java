package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.UserModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.TableMenuAdapter;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.CompanyPosition;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDialog extends BaseSimpleDialog<User> {
    private JPanel contentPane;
    private JButton btn_save;
    private JHdTable jt;



    @Inject
    ApiManager apiManager;

    @Inject
    UserModel model;
    public UserDialog(Window window) {
        super(window);


    }

    @Override
    protected RemoteData<User> saveData(java.util.List<User> datas) throws HdException {
        return apiManager.saveUsers(datas);
    }

    @Override
    protected RemoteData<User> readData() throws HdException {
        return apiManager.readUsers();
    }

    @Override
    protected BaseTableModel<User> getTableModel() {
        return model;
    }

    @Override
    protected void init() {
        setTitle("用户列表");
        setContentPane(contentPane);
        jt.setModel(model);
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSaveWork();
            }
        });
        String[] menu=new String[]{"删除"};
        jt.addMouseListener(new TableMenuAdapter(jt, menu, new TableMenuAdapter.TableMenuListener() {
            @Override
            public void onMenuClick(JTable table, int index) {

                if(index==0)
                {int row=table.getSelectedRow();
                  int modelRow=  table.convertRowIndexToModel(row);

                    model.deleteRow(modelRow);

                }

            }
        }));


        JComboBox<CompanyPosition> packMaterialClassComboBox = new JComboBox<>();
        for (CompanyPosition packMaterialClass : CompanyPosition.POSITIONS)
            packMaterialClassComboBox.addItem(packMaterialClass);
        jt.setDefaultEditor(CompanyPosition.class, new DefaultCellEditor(packMaterialClassComboBox));
    }
    @Override
    public void doSomethingOnError(RemoteData<User> data) {
        doLoadWork();
    }
}
