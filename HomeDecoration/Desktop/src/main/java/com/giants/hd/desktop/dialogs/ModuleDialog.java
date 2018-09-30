package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.ModuleModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Module;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModuleDialog extends BaseSimpleDialog<Module> {
    private JPanel contentPane;
    private JButton btn_save;
    private JHdTable jt;


    @Inject
    ApiManager apiManager;

    @Inject
    ModuleModel model;
    public ModuleDialog(Window window) {
        super(window);
    }

    @Override
    protected RemoteData<Module> saveData(java.util.List<Module> datas) throws HdException {
        return apiManager.saveModules(datas);
    }

    @Override
    protected RemoteData<Module> readData() throws HdException {
        return apiManager.readModules();
    }

    @Override
    protected BaseTableModel<Module> getTableModel() {
        return model;
    }

    @Override
    protected void init() {
        setTitle("模块列表");
        setContentPane(contentPane);
        jt.setModel(model);
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSaveWork();
            }
        });
    }
}
