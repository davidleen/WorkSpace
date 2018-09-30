package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.viewImpl.Panel_ProductProcess;
import com.giants.hd.desktop.widget.TableMouseAdapter;
import com.giants.hd.desktop.widget.TablePopMenu;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *  工序对话框
 */
public class ProductProcessDialog extends  BaseSimpleDialog<ProductProcess> {


    @Inject
    ApiManager apiManager;
    @Inject
    Panel_ProductProcess panel_productProcess;

    public ProductProcessDialog(Window window) {
        super(window);
    }





    @Override
    protected RemoteData<ProductProcess> readData() throws HdException {
          return apiManager.loadProductProcess();
    }

    @Override
    protected BaseTableModel<ProductProcess> getTableModel() {
        return panel_productProcess.productProcessModel;
    }


    @Override
    protected RemoteData<ProductProcess> saveData(List<ProductProcess> datas) throws HdException {
      return   apiManager.saveProductProcess(datas);
    }

    /**
     * 初始
     */
    @Override
    protected void init() {
        setTitle("工序列表");
        setContentPane(panel_productProcess.getRoot());
        panel_productProcess.jt_process.setModel(panel_productProcess.productProcessModel);
        panel_productProcess.btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSaveWork();

            }
        });



        TableMouseAdapter adapter=new TableMouseAdapter(new TablePopMenu.TableMenuLister() {
            @Override
            public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {

                switch (index) {


                    case TablePopMenu.ITEM_INSERT:

                        tableModel.addNewRow(rowIndex[0]);

                        break;
                    case TablePopMenu.ITEM_DELETE:




                        tableModel.deleteRows(rowIndex);
                        break;
                }



            }
        });


        panel_productProcess.jt_process.addMouseListener(adapter);


    }







}
