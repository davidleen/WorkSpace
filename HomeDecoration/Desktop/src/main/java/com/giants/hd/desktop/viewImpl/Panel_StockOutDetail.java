package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.dialogs.SplitStockOutItemDialog;
import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants.hd.desktop.model.StockOutGuihaoModel;
import com.giants.hd.desktop.model.StockOutItemTableModel;
import com.giants.hd.desktop.mvp.presenter.StockOutDetailIPresenter;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.mvp.viewer.StockOutDetailViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.noEntity.ErpStockOutDetail;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 出库界面详情界面
 * Created by davidleen29 on 2016/7/14.
 */
public class Panel_StockOutDetail extends BasePanel implements StockOutDetailViewer {

    private JPanel root;
    private JHdTable tb;
    private JTextField tf_ck_no;
    private JTextField tf_cus;
    private JTextField tf_dd;
    private JTextField tf_mdg;
    private JTextField tf_tdh;
    private JTextField tf_gsgx;
    private JScrollPane scroll;


    private JButton save;
    private JHdTable tb_guihao;
    private JTextField tf_guihao;
    private JTextField tf_fengqian;
    private JButton btn_addgui;
    private JButton btn_showall;
    private JTextField tf_sal;
    private JButton export_invoice;
    private JButton export_pack;
    private JTextField tf_guixing;
    private JButton export_qingguan;
    private JButton export_qingguan_xk;
    private JPanel panel_input;
    private JTextArea jta_maitou;
    private StockOutDetailIPresenter presenter;

    private StockOutItemTableModel tableModel;

    private StockOutGuihaoModel guihaoModel;

    Point popupMenuLocation=new Point();

    public Panel_StockOutDetail(StockOutDetailIPresenter presenter) {
        this.presenter = presenter;
        init();
    }

    private void init() {

        tableModel = new StockOutItemTableModel();
        panel_input.setVisible(false);
        tb.setModel(tableModel);

        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb_guihao.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.addMouseListener(new MouseAdapter() {


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
                if (  presenter.isEditable()&&e.isPopupTrigger()) {

                      ErpStockOutItem item=null;

                    int[] modelRows = JTableUtils.getSelectedRowSOnModel(tb);
                    if (modelRows != null && modelRows.length > 0) {

                        item=tableModel.getItem(modelRows[0]);
                    }
                    if(item==null)return;
                   final  ErpStockOutItem finalItem=item;
                    popupMenuLocation.setLocation(e.getXOnScreen(),e.getYOnScreen());
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem split = new JMenuItem(" 拆 分 ");
                    split.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            showSplitDialog(finalItem);

                        }
                    });
                    menu.add(split);
                    if(item.subRecord) {
                        JMenuItem delete = new JMenuItem(" 删 除 ");
                        menu.add(delete);
                        delete.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {


                                presenter.deleteErpStockOutItem( finalItem);

                            }
                        });
                    }
                    //  取得右键点击所在行
                    menu.show(e.getComponent(), e.getX(), e.getY());

                    return;
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {


                if (e.getClickCount() == 2) {

                    int column = tb.convertColumnIndexToModel(tb.getSelectedColumn());
                    int selectRow = tb.convertRowIndexToModel(tb.getSelectedRow());
                    ErpStockOutItem item = tableModel.getItem(selectRow);

                    if (item == null) return;

                    //单击第一列 显示原图
                    if (column == 1) {


                        if (!StringUtils.isEmpty(item.prd_no)) {
                            ImageViewDialog.showProductDialog(getWindow(getRoot()), item.prd_no, item.pVersion, item.url);
                        }
                    }
                    if (column == 6) {
                        if (AuthorityUtil.getInstance().viewOrderMenu()) {
                            String os_no = item.os_no;
                            presenter.showOrderDetail(os_no);
                        }


                    }


                }
            }

            ;

        });


        guihaoModel = new StockOutGuihaoModel();
        tb_guihao.setModel(guihaoModel);
        //柜号删除处理
        MouseAdapter adapter = new MouseAdapter() {

            @Override
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
                if (e.isPopupTrigger()) {
                    JTable source = (JTable) e.getSource();

                    JPopupMenu menu = new JPopupMenu();

                    final JMenuItem delete = new JMenuItem("删除");
                    menu.add(delete);
                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int tableRow[] = JTableUtils.getSelectedRowSOnModel(tb_guihao);
                            if (tableRow.length == 0) {
                                JOptionPane.showMessageDialog(tb_guihao, "请选择行进行删除。");
                                return;
                            }
                            StockOutDetailFrame.GuiInfo guiInfo = guihaoModel.getItem(tableRow[0]);

                            presenter.removeGuiInfo(guiInfo);

                        }
                    });


                    menu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        };

        tb_guihao.addMouseListener(adapter);


        //保存
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.save();


            }
        });


        //添加柜号数据
        btn_addgui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String guihao = tf_guihao.getText();
                String fengqian = tf_fengqian.getText().trim();
                String guixing=tf_guixing.getText().trim();

                if (StringUtils.isEmpty(guihao) || StringUtils.isEmpty(fengqian)) {
                    JOptionPane.showMessageDialog(getRoot(), "请输入柜号封签号");
                    return;
                }


                presenter.addGuiInfo(guihao, fengqian,guixing);

            }
        });


        tb_guihao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) ;
                int[] row = JTableUtils.getSelectedRowSOnModel(tb_guihao);
                if (row != null && row.length > 0) {
                    StockOutDetailFrame.GuiInfo guiInfo = guihaoModel.getItem(row[0]);
                    if (guiInfo != null) {
                        presenter.filterGuihao(guiInfo);

                    }
                }
            }
        });


        btn_showall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.filterGuihao(null);
            }
        });


        export_invoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.exportInvoice();

            }
        });


        export_pack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.exportPack();
            }
        });


        export_qingguan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.exportQingguan();
            }
        });

        export_qingguan_xk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.exportQingguan_xk();
            }
        });
    }


    @Override
    public JComponent getRoot() {
        return root;
    }


    @Override
    public void setStockOutDetail(ErpStockOutDetail erpStockOutDetail) {


        tf_ck_no.setText(erpStockOutDetail.erpStockOut.ck_no);

        tf_dd.setText(erpStockOutDetail.erpStockOut.ck_dd);
        tf_cus.setText(erpStockOutDetail.erpStockOut.cus_no);
        tf_gsgx.setText(erpStockOutDetail.erpStockOut.gsgx);
        tf_mdg.setText(erpStockOutDetail.erpStockOut.mdg);
        tf_tdh.setText(erpStockOutDetail.erpStockOut.tdh);
        tf_sal.setText(erpStockOutDetail.erpStockOut.sal_name + "-" + erpStockOutDetail.erpStockOut.sal_cname);




        jta_maitou.getDocument().removeDocumentListener(maitouDocumentListener);
//        ta_cemai.getDocument().removeDocumentListener(cemaiDocumentListener);
//        ta_neihemai.getDocument().removeDocumentListener(neihemaiDocumentListener);
//        ta_zhengmai.getDocument().removeDocumentListener(zhengmaiDocumentListener);
//        ta_memo.getDocument().removeDocumentListener(memoDocumentListener);
//
        jta_maitou.setText(erpStockOutDetail.erpStockOut.maitou);
//        ta_cemai.setText(erpStockOutDetail.erpStockOut.cemai);
//        ta_zhengmai.setText(erpStockOutDetail.erpStockOut.zhengmai);
//        ta_neihemai.setText(erpStockOutDetail.erpStockOut.neheimai);
//        ta_memo.setText(erpStockOutDetail.erpStockOut.memo);
//
//
        jta_maitou.getDocument().addDocumentListener(maitouDocumentListener);
//        ta_cemai.getDocument().addDocumentListener(cemaiDocumentListener);
//        ta_neihemai.getDocument().addDocumentListener(neihemaiDocumentListener);
//        ta_zhengmai.getDocument().addDocumentListener(zhengmaiDocumentListener);
//        ta_memo.getDocument().addDocumentListener(memoDocumentListener);

        tableModel.setDatas(erpStockOutDetail.items);

    }


    private DocumentListener maitouDocumentListener=new DocumentAdapter() {


        @Override
        public void onTextChange(DocumentEvent documentEvent) {
            presenter.onMaitouChanged(jta_maitou.getText().trim());
        }
    };

    @Override
    public void showGuihaoData(Set<StockOutDetailFrame.GuiInfo> guiInfos) {

        tf_guihao.setText("");
        tf_fengqian.setText("");
        tf_guixing.setText("");
        List<StockOutDetailFrame.GuiInfo> guiInfoList = new ArrayList<>();
        guiInfoList.addAll(guiInfos);
        guihaoModel.setDatas(guiInfoList);


        if(presenter.isEditable()) {
            //设置出库表格柜号处理数据
            JComboBox<StockOutDetailFrame.GuiInfo> guiInfoJComboBox = new JComboBox<>();
            guiInfoJComboBox.addItem(new StockOutDetailFrame.GuiInfo("", "",""));
            for (StockOutDetailFrame.GuiInfo guiInfo : guiInfoList)
                guiInfoJComboBox.addItem(guiInfo);
            DefaultCellEditor comboboxEditor = new DefaultCellEditor(guiInfoJComboBox);


            tb.setDefaultEditor(StockOutDetailFrame.GuiInfo.class, comboboxEditor);
        }

    }

    @Override
    public void showItems(List<ErpStockOutItem> itemList) {
        tableModel.setDatas(itemList);

    }


    @Override
    public void setEditable(boolean b) {


        save.setVisible(b);
        btn_addgui.setVisible(b);
        tableModel.setEditable(b);
        tableModel.setRowAdjustable(false);
        guihaoModel.setEditable(false);

    }

    @Override
    public void setExportable(boolean b) {

        export_pack.setVisible(b);
        export_invoice.setVisible(b);

    }

    @Override
    public void setStockOutPriceVisible(boolean visible) {

        tableModel.setPriceVisible(visible);
    }

    /**
     * 显示拆分对话框
     *
     * @param stockOutItem
     */

    @Override
    public void showSplitDialog(final ErpStockOutItem stockOutItem) {


        SplitStockOutItemDialog dialog = new SplitStockOutItemDialog();
        dialog.pack();
        dialog.setLocation( popupMenuLocation);
        dialog.setStockOutItem(stockOutItem, new SplitStockOutItemDialog.OnNewQtyGetListener() {
            @Override
            public void onNewQty(int newQty) {

                presenter.splitItem(stockOutItem,newQty);
            }
        });
        dialog.setVisible(true);
    }
}
