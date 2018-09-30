package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.model.OrderItemTableModel;
import com.giants.hd.desktop.mvp.presenter.OrderDetailIPresenter;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.mvp.viewer.OrderDetailViewer;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.widget.AttachPanel;
import com.giants.hd.desktop.widget.DateCellEditor;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.TextAreaCellEditor;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.noEntity.ErpOrderDetail;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

/**
 * 订单详情
 * Created by david on 2016/3/30.
 */
public class Panel_Order_Detail extends BasePanel implements OrderDetailViewer {
    private JPanel panel1;
    private JHdTable orderItemList;
    private JButton printPaint;
    private JButton showDetail;
    private JTextField tf_dd;
    private JTextField tf_os_no;
    private JTextField tf_sales;
    private JTextField tf_customer;
    private JTextField tf_cos;
    private JTextField tf_so_data;
    private JTextArea ta_memo;
    private JTextArea ta_cemai;
    private JTextArea ta_zhengmai;
    private JTextArea ta_neihemai;
    private AttachPanel panel_attach;
    private JButton btn_addPicture;
    private JButton save;
    private JTextArea ta_rem;
    private JTextArea ta_youmai;
    private JTextArea ta_zuomai;
    private JButton btn_track;
    private JButton upload;
    private JLabel viewMaitou;




    private OrderItemTableModel orderItemTableModel;

    private OrderDetailIPresenter orderDetailPresenter;

    public Panel_Order_Detail(OrderDetailIPresenter frame) {

        this.orderDetailPresenter = frame;
        orderItemTableModel = new OrderItemTableModel();
        orderItemTableModel.setRowAdjustable(false);
        orderItemList.setModel(orderItemTableModel);


        printPaint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (orderItemList.getSelectedRow() <= -1) {
                    JOptionPane.showMessageDialog(Panel_Order_Detail.this.getWindow(), "请选一项订单产品");
                    return;
                }


                int modelRow = orderItemList.convertRowIndexToModel(orderItemList.getSelectedRow());
                ErpOrderItem orderItem = orderItemTableModel.getItem(modelRow);

                orderDetailPresenter.printPaint(orderItem);

            }
        });


        showDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (orderItemList.getSelectedRow() <= -1) {
                    JOptionPane.showMessageDialog(Panel_Order_Detail.this.getWindow(), "请选一项订单产品");
                    return;
                }


                int modelRow = orderItemList.convertRowIndexToModel(orderItemList.getSelectedRow());
                ErpOrderItem orderItem = orderItemTableModel.getItem(modelRow);
                orderDetailPresenter.showProductDetail(orderItem);


            }
        });


        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                File file= SwingFileUtils.getSelectedFile();

                if(file!=null&&file.exists())
                orderDetailPresenter.updateMaitouFile(file);



            }
        });

        orderItemList.addMouseListener(new MouseInputAdapter() {


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
                if (orderDetailPresenter.isEditable() && e.isPopupTrigger()) {

                    ErpOrderItem item = null;

                    int[] modelRows = JTableUtils.getSelectedRowSOnModel(orderItemList);
                    if (modelRows != null && modelRows.length > 0) {

                        item = orderItemTableModel.getItem(modelRows[0]);
                    }
                    if (item == null) return;
                    final ErpOrderItem finalItem = item;




                    return;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = orderItemList.getSelectedRow();
                    ErpOrderItem orderItem = orderItemTableModel.getItem(row);


                    int column = orderItemList.convertColumnIndexToModel(orderItemList.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 1) {
                        ImageViewDialog.showProductDialog(getWindow(), orderItem.prd_name, "", orderItem.url);
                    }

                    //第八列 显示附件
                    if (column == 8) {

                        String packAttaches = orderItem.packAttaches;
                        if (StringUtils.isEmpty(packAttaches)) return;
                        String[] temp = StringUtils.split(packAttaches);
                        final int length = temp.length;
                        String[] newUrl = new String[length];
                        for (int i = 0; i < length; i++) {
                            newUrl[i] = HttpUrl.loadPicture(temp[i]);
                        }


                        ImageViewDialog.showDialog(getWindow(), StringUtils.combine(newUrl), orderItem.prd_name + "包装附件");

                    }


                }

            }
        });


        btn_addPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final File[] files = FileChooserHelper.chooseFiles(JFileChooser.FILES_ONLY, false, new PictureFileFilter());


                if (files != null) {
                    orderDetailPresenter.addPictureClick(files);
                }


            }
        });


        //保存
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderDetailPresenter.save();


            }
        });

        btn_track.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                orderDetailPresenter.startOrderTrack();
            }
        });

        //图片的摆放布局
        panel_attach.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel_attach.setMaxSize(100);

        panel_attach.setListener(new AttachPanel.OnAttachFileDeleteListener() {
            @Override
            public void onDelete(int index, String url) {

                orderDetailPresenter.onDeleteAttach(url);
            }
        });
        panel_attach.setTitle("订单附件");

        //  orderItemList.setCe(Date.class,new DateCellEditor());
        orderItemList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        orderItemList.getColumn(OrderItemTableModel.TITLE_VERIFY_DATE).setCellEditor(new DateCellEditor());
        orderItemList.getColumn(OrderItemTableModel.TITLE_SEND_DATE).setCellEditor(new DateCellEditor());
        TextAreaCellEditor textAreaCellEditorAndRenderer = new TextAreaCellEditor();
        orderItemList.getColumn(OrderItemTableModel.TITLE_PACKAGE_INFO).setCellEditor(textAreaCellEditorAndRenderer);
        orderItemList.getColumn(OrderItemTableModel.TITLE_MAITOU).setCellEditor(textAreaCellEditorAndRenderer);
        orderItemList.getColumn(OrderItemTableModel.TITLE_GUAGOU).setCellEditor(textAreaCellEditorAndRenderer);



        viewMaitou.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getClickCount()==2)
                {

                    orderDetailPresenter.viewMaitou();

                }
            }
        });

    }

    @Override
    public void setOrderDetail(ErpOrderDetail orderDetail) {

        ErpOrder order = orderDetail.erpOrder;
        tf_dd.setText(order.os_dd);
        tf_os_no.setText(order.os_no);
        tf_customer.setText(order.cus_no);
        tf_cos.setText(order.cus_os_no);
        tf_sales.setText(order.sal_no + "-" + order.sal_name + "-" + order.sal_cname);
        tf_so_data.setText(order.so_data);
        ta_rem.setText(order.rem);

        ta_memo.setText(order.memo);


        ta_cemai.getDocument().removeDocumentListener(cemaiDocumentListener);
        ta_neihemai.getDocument().removeDocumentListener(neihemaiDocumentListener);
        ta_zhengmai.getDocument().removeDocumentListener(zhengmaiDocumentListener);
        ta_memo.getDocument().removeDocumentListener(memoDocumentListener);
        ta_zuomai.getDocument().removeDocumentListener(zuomaiDocumentListener);
        ta_youmai.getDocument().removeDocumentListener(youmaiDocumentListener);


        ta_cemai.setText(orderDetail.erpOrder.cemai);
        ta_zhengmai.setText(orderDetail.erpOrder.zhengmai);
        ta_neihemai.setText(orderDetail.erpOrder.neheimai);
        ta_memo.setText(orderDetail.erpOrder.memo);
        ta_zuomai.setText(orderDetail.erpOrder.zuomai);
        ta_youmai.setText(orderDetail.erpOrder.youmai);


        ta_cemai.getDocument().addDocumentListener(cemaiDocumentListener);
        ta_neihemai.getDocument().addDocumentListener(neihemaiDocumentListener);
        ta_zhengmai.getDocument().addDocumentListener(zhengmaiDocumentListener);
        ta_memo.getDocument().addDocumentListener(memoDocumentListener);
        ta_zuomai.getDocument().addDocumentListener(zuomaiDocumentListener);
        ta_youmai.getDocument().addDocumentListener(youmaiDocumentListener);

        orderItemTableModel.setDatas(orderDetail.items);

        viewMaitou.setVisible(!StringUtils.isEmpty(orderDetail.erpOrder.maitouUrl));

//        //检查是否已经启动生产流程追踪
//        boolean hasStartTrack = false;
//        for (ErpOrderItem orderItem : orderDetail.items) {
//            if (!StringUtils.isEmpty(orderItem.currentWorkFlow)) {
//                hasStartTrack = true;
//                break;
//            }
//        }
//
//        btn_track.setEnabled(!hasStartTrack);
//        btn_track.setText(!hasStartTrack?"启动生产流程":"生产流程已经启动");
        btn_track.setVisible(false);

    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    @Override
    public void setEditable(boolean b) {


        btn_addPicture.setVisible(b);
        save.setVisible(b);
        upload.setVisible(b);

    }

    @Override
    public void showAttachFiles(List<String> attachStrings) {
        String[] urls = new String[attachStrings.size()];
        attachStrings.toArray(urls);
        panel_attach.setAttachFiles(urls);


    }


    /**
     * 侧唛数据修改监听
     */
    private DocumentListener cemaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            orderDetailPresenter.onCemaiChange(ta_cemai.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            orderDetailPresenter.onCemaiChange(ta_cemai.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            orderDetailPresenter.onCemaiChange(ta_cemai.getText().trim());

        }
    };

    /**
     * 内盒唛数据修改监听
     */
    private DocumentListener neihemaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            orderDetailPresenter.onNeihemaiChange(ta_neihemai.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            orderDetailPresenter.onNeihemaiChange(ta_neihemai.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            orderDetailPresenter.onNeihemaiChange(ta_neihemai.getText().trim());

        }
    };


    /**
     * 正唛数据修改监听
     */
    private DocumentListener zhengmaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            orderDetailPresenter.onZhengmaiChange(ta_zhengmai.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            orderDetailPresenter.onZhengmaiChange(ta_zhengmai.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            orderDetailPresenter.onZhengmaiChange(ta_zhengmai.getText().trim());

        }
    };


    /**
     * 备注数据修改监听
     */
    private DocumentListener memoDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            orderDetailPresenter.onMemoChange(ta_memo.getText().trim());

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            orderDetailPresenter.onMemoChange(ta_memo.getText().trim());

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            orderDetailPresenter.onMemoChange(ta_memo.getText().trim());

        }
    };

    /**
     * 左唛数据修改监听
     */
    private DocumentListener zuomaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            orderDetailPresenter.onZuomaiChange(ta_zuomai.getText().trim());

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            orderDetailPresenter.onZuomaiChange(ta_zuomai.getText().trim());

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            orderDetailPresenter.onZuomaiChange(ta_zuomai.getText().trim());

        }
    };
    /**
     * 右唛数据修改监听
     */
    private DocumentListener youmaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            orderDetailPresenter.onYoumaiChange(ta_youmai.getText().trim());

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            orderDetailPresenter.onYoumaiChange(ta_youmai.getText().trim());

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            orderDetailPresenter.onYoumaiChange(ta_youmai.getText().trim());

        }
    };


    @Override
    public void setPriceVisible(boolean priceVisible) {
        orderItemTableModel.setFobPriceVisible(priceVisible);
    }


    @Override
    public void setCanViewProduct(boolean b) {

        showDetail.setVisible(b);

    }


}
