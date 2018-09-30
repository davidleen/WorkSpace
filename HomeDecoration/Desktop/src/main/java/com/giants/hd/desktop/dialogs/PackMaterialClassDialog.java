package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.PackMaterialClassTableModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.PackMaterialClass;
import com.giants3.hd.entity.PackMaterialPosition;
import com.giants3.hd.entity.PackMaterialType;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * 包装材料列表
 */
public class PackMaterialClassDialog extends BaseSimpleDialog<PackMaterialClass> {
    private JPanel contentPane;
    private JButton save;
    private JHdTable jt;

    @Inject
    ApiManager apiManager;
    PackMaterialClassTableModel model;
    public PackMaterialClassDialog(Window window) {
        super(window);

        setTitle("包装录入模板");
        setContentPane(contentPane);
        model=new PackMaterialClassTableModel();
        jt.setModel(model);

    }

    @Override
    protected RemoteData<PackMaterialClass> readData() throws HdException {
        return apiManager.readPackMaterialClass();
    }

    @Override
    protected BaseTableModel<PackMaterialClass> getTableModel() {
        return model;
    }

    @Override
    protected void init() {


        JComboBox<PackMaterialType> packMaterialTypeComboBox = new JComboBox<>();
        for (PackMaterialType type : CacheManager.getInstance().bufferData.packMaterialTypes)
            packMaterialTypeComboBox.addItem(type);
        DefaultCellEditor comboboxEditor = new DefaultCellEditor(packMaterialTypeComboBox);

        jt.setDefaultEditor(PackMaterialType.class, comboboxEditor);

        JComboBox<PackMaterialPosition> packMaterialPositionComboBox = new JComboBox<>();
        for (PackMaterialPosition position : CacheManager.getInstance().bufferData.packMaterialPositions)
            packMaterialPositionComboBox.addItem(position);
        jt.setDefaultEditor(PackMaterialPosition.class, new DefaultCellEditor(packMaterialPositionComboBox));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                 doSaveWork();




            }
        });

    }

    @Override
    protected RemoteData<PackMaterialClass> saveData(List<PackMaterialClass> datas) throws HdException {




      return  apiManager.savePackMaterialClass(datas);
    }
}
