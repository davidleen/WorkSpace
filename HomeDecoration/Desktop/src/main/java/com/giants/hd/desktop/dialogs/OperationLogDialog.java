package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.OperationLogModel;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.entity.OperationLog;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Product;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OperationLogDialog extends BaseSimpleDialog<OperationLog> {
    private JPanel contentPane;
    private JHdTable jt;


    private Class  aClass;
    private long id;

    @Inject
    ApiManager apiManager;

    @Inject
    OperationLogModel model;

    Point popupMenuLocation = new Point();

    /**历史记录查看对话框
     * @param window
     * @param aClass
     * @param id
     */
    public OperationLogDialog(Window window,Class aClass, long id) {
        super(window);

        setTitle("历史操作记录查看");
        setContentPane(contentPane);
        this.aClass=aClass;
        this.id=id;
    }

    @Override
    protected RemoteData<OperationLog> readData() throws HdException {



      RemoteData<OperationLog> datas=  apiManager.readOperationLog(aClass.getSimpleName(),id);



        return datas;

    }

    @Override
    protected BaseTableModel<OperationLog> getTableModel() {
        return model;
    }

    @Override
    protected void init() {


        jt.setModel(model);
        jt.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            private void showMenu(MouseEvent e) {
                if (  e.isPopupTrigger()) {

                    OperationLog item = null;

                    int[] modelRows = JTableUtils.getSelectedRowSOnModel(jt);
                    if (modelRows != null && modelRows.length > 0) {

                        item = model.getItem(modelRows[0]);
                    }
                    if (item == null) return;

                    final OperationLog operationLog=item;

                    //产品支持恢复数据
                    if(Product.class.getSimpleName().equals(item.tableName))
                    {

                        popupMenuLocation.setLocation(e.getXOnScreen(), e.getYOnScreen());
                        JPopupMenu menu = new JPopupMenu();


                        JMenuItem split = new JMenuItem("恢复该修改前的数据吗？");
                        split.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {


                                restoreData(operationLog.id);

                            }
                        });
                        menu.add(split);

                        //  取得右键点击所在行
                        menu.show(e.getComponent(), e.getX(), e.getY());


                    }




                    return;
                }
            }
        });

    }



    private  void  restoreData(final long operationLogId)
    {

        new HdSwingWorker<Void,Object>(this)
        {
            @Override
            protected RemoteData<Void> doInBackground() throws Exception {



                return  apiManager.restoreProductDetailFromOperationLog(operationLogId);

            }

            @Override
            public void onResult(RemoteData<Void> data) {


                if(data.isSuccess())
                {

                    JOptionPane.showMessageDialog(OperationLogDialog.this, "数据已经恢复到指定操作纪录前， 请重新打开分析表");

                }else
                {
                    JOptionPane.showMessageDialog(OperationLogDialog.this, data.message);

                }


            }
        }.go();
    }



}
