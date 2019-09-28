package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.frames.ProductDetailFrame;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.OperationLogModel;
import com.giants.hd.desktop.mvp.DialogViewer;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.OperationLog;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.noEntity.ProductDetail;
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

                        JMenuItem  split = new JMenuItem("查看历史数据");
                        split.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {


                                UseCaseFactory.getInstance().createGetUseCase(HttpUrl.readProductHistoryData(operationLog.id), ProductDetail.class).execute(new RemoteDataSubscriber<ProductDetail>(new DialogViewer(OperationLogDialog.this)) {


                                    @Override
                                    protected void handleRemoteData(RemoteData<ProductDetail> data) {
                                        ProductDetail productDetail=data.datas.get(0);

                                        OperationLogDialog.this.dispose();
                                        ProductDetailFrame productDetailFrame = new ProductDetailFrame(productDetail, null,ProductDetailFrame.VIEW_TYPE_HISTORY);

                                        JFrame frame = productDetailFrame;
                                        frame.setLocationRelativeTo(OperationLogDialog.this);
                                        frame.setVisible(true);


                                    }


                                });


                            }
                        });
                        menu.add(split);
                         split = new JMenuItem("恢复修改前数据");
                        split.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                int option= JOptionPane.showConfirmDialog(OperationLogDialog.this, "确定恢复当前分析表数据到当前修改记录前吗？"," 提示", JOptionPane.OK_CANCEL_OPTION);

                                if (JOptionPane.OK_OPTION == option) {
                                    restoreData(operationLog.id);
                                }


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
