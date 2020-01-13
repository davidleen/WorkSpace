package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.dialogs.ExportQuotationDialog;
import com.giants.hd.desktop.dialogs.OperationLogDialog;
import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.interf.CommonSearchAdapter;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.QuotationDetailIPresenter;
import com.giants.hd.desktop.mvp.viewer.QuotationDetailView;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.QuotationItemPopMenu;
import com.giants.hd.desktop.widget.header.ColumnGroup;
import com.giants.hd.desktop.widget.header.GroupableTableHeader;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.logic.QuotationAnalytics;
import com.giants3.hd.noEntity.Product2;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 报价单详情
 * <p/>
 * Created by davidleen29 on 2015/6/30.
 */
public class Panel_QuotationDetail extends BasePanel implements QuotationDetailView {
    private JHdTable tb;
    private JHdTable fixedTable;
    private JViewport viewport;
    private JPanel root;
    private JTextArea ta_memo;
    private JTextField jtf_number;
    private JComboBox cb_currency;
    private JComboBox<User> cb_salesman;
    private JComboBox<Customer> cb_customer;
    private JDatePickerImpl qDate;
    private JDatePickerImpl vDate;
    private JButton btn_save;
    private JButton btn_delete;
    private JButton btn_export;
    private JLabel creator;
    private JLabel createTime;
    private JPanel jp_log;
    private JLabel updator;
    private JLabel updateTime;
    private JLabel viewLog;
    private JButton btn_resume;
    private JButton btn_verify;
    private JLabel icon_verify;
    private JButton btn_unVerify;
    private JPanel jp_verify;
    private JTextField jtf_company;
    private JLabel lb_company;
    private JButton btn_reimport;
    private JLabel icon_overdue;
    private JScrollPane jtScroll;
    public QuotationDetail data;


    @Inject
    ApiManager apiManager;


    QuotationItemTableModel model;

    QuotationItemXKTableModel xkModel;

    QuotationItemFixColumnTableModel fixColumnModel;

    QuotationItemFixColumnXKTableModel fixColumnXkModel;


    //model 对应操作这两个数组
    private List<QuotationItem> quotationItems = new ArrayList<>();
    private List<QuotationXKItem> quotationXKItems = new ArrayList<>();

    QuotationDetailIPresenter presenter;


    @Inject
    public Panel_QuotationDetail(QuotationDetailIPresenter presenter) {


        init();
        this.presenter = presenter;
    }

    private void init() {
        model = new QuotationItemTableModel(new QuotationCostPriceRatioChangeListener() {
            @Override
            public void onChange(final QuotationItem quotationItem, final float cost_price_ratio, final float newValue) {


                //读取产品数据  重新计算fob值
                UseCaseFactory.getInstance().createGetProductByIdUseCase(quotationItem.productId).execute(new RemoteDataSubscriber<Product>(Panel_QuotationDetail.this) {
                    @Override
                    protected void handleRemoteData(RemoteData<Product> data) {


                        if (data.isSuccess() && data.datas.size() > 0) {
                            Product product = data.datas.get(0);

                            quotationItem.price = QuotationAnalytics.getPriceBaseOnCostRatio(product, newValue, CacheManager.getInstance().bufferData.globalData);
                            //更新表格
                            model.fireTableDataChanged();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        quotationItem.cost_price_ratio = cost_price_ratio;
                        //更新表格
                        model.fireTableDataChanged();
                    }
                });


                showLoadingDialog();


            }
        });
        xkModel = new QuotationItemXKTableModel();

        fixColumnModel = new QuotationItemFixColumnTableModel();

        fixColumnXkModel = new QuotationItemFixColumnXKTableModel(new QuotationXkCostPriceRatioChangeListener() {
            @Override
            public void onChange(final QuotationXKItem quotationItem, final float cost_price_ratio, final float newValue) {

                //读取产品数据  重新计算fob值


                UseCaseFactory.getInstance().createGetProductByIdUseCase(quotationItem.productId, quotationItem.productId2).execute(new RemoteDataSubscriber<Product>(Panel_QuotationDetail.this) {
                    @Override
                    protected void handleRemoteData(RemoteData<Product> data) {


                        if (data.isSuccess() && data.datas.size() > 0) {


                            for (Product product : data.datas) {

                                if (quotationItem.productId == product.id)
                                    quotationItem.price = QuotationAnalytics.getPriceBaseOnCostRatio(product, newValue, CacheManager.getInstance().bufferData.globalData);
                                if (quotationItem.productId2 == product.id)
                                    quotationItem.price2 = QuotationAnalytics.getPriceBaseOnCostRatio(product, newValue, CacheManager.getInstance().bufferData.globalData);


                            }

                            //更新表格
                            model.fireTableDataChanged();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        quotationItem.cost_price_ratio = cost_price_ratio;
                        //更新表格
                        model.fireTableDataChanged();
                    }
                });


                showLoadingDialog();


            }
        });


        //   tb.setModel(model);


        configTableEditor();
        configProduct2Editor();


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.save();
            }
        });


        fixedTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    int column = fixedTable.convertColumnIndexToModel(fixedTable.getSelectedColumn());
                    int selectRow = fixedTable.convertRowIndexToModel(fixedTable.getSelectedRow());
                    //单击第一列 显示原图
                    if (column == 1) {


                        if (data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {


                            QuotationItem item = quotationItems.get(selectRow);
                            if (item.productId > 0) {
                                ImageViewDialog.showProductDialog(getWindow(getRoot()), item.productName, item.pVersion, item.photoUrl);
                            }
                        } else {


                            QuotationXKItem item = quotationXKItems.get(selectRow);
                            if (item.productId > 0) {
                                ImageViewDialog.showProductDialog(getWindow(getRoot()), item.productName, item.pVersion, item.photoUrl);
                            } else if (item.productId2 > 0) {
                                ImageViewDialog.showProductDialog(getWindow(getRoot()), item.productName2, item.pVersion2, item.photo2Url);
                            }
                        }

                    }

                }

            }
        });


        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.delete();


            }
        });


        btn_export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                new ExportQuotationDialog(SwingUtilities.getWindowAncestor(getRoot()), data).setVisible(true);


            }
        });


        //配置权限  是否修改  是否可以删除

        boolean modifiable = AuthorityUtil.getInstance().editQuotation() || AuthorityUtil.getInstance().addQuotation();

        btn_save.setVisible(modifiable);


        btn_delete.setVisible(AuthorityUtil.getInstance().deleteQuotation());


        btn_export.setVisible(AuthorityUtil.getInstance().exportQuotation());


        viewLog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    Window window = getWindow(Panel_QuotationDetail.this.getRoot());
                    OperationLogDialog dialog = new OperationLogDialog(window, Quotation.class, data.quotation.id);
                    dialog.setLocationRelativeTo(window);
                    dialog.setVisible(true);


                }

            }
        });


        //取消审核功能
        btn_unVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.unVerify();


            }
        });

        //审核功能
        btn_verify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.verify();


            }
        });


        cb_customer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Customer customer = (Customer) e.getItem();
                    jtf_company.setVisible(customer.code.equals(Customer.CODE_TEMP));
                    lb_company.setVisible(customer.code.equals(Customer.CODE_TEMP));
                    getWindow(jtf_company).invalidate();

                }


            }
        });


        //恢复功能默认不显示
        btn_resume.setVisible(false);

        //审核状态默认不显示
        icon_verify.setVisible(false);
        jp_verify.setVisible(false);

        //过期状态默认不显示
        icon_overdue.setVisible(false);


        //重新导入
        btn_reimport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                final Set<Long> productIds = new HashSet<Long>();

                for (QuotationItem item : model.getValuableDatas()) {
                    productIds.add(item.productId);
                }
                for (QuotationXKItem item : xkModel.getValuableDatas()) {
                    productIds.add(item.productId);
                    productIds.add(item.productId2);
                }

                new HdSwingWorker<Product, Object>(getWindow(root)) {

                    @Override
                    protected RemoteData<Product> doInBackground() throws Exception {

                        return apiManager.readProductsByIds(productIds);

                    }

                    @Override
                    public void onResult(RemoteData<Product> data) {


                        if (data.isSuccess()) {
                            java.util.List<Product> productList = data.datas;


                            for (QuotationItem item : model.getValuableDatas()) {

                                Product product = findProduct(productList, item.productId);
                                if (product != null) {
                                    QuotationAnalytics.updateProduct(item, product, CacheManager.getInstance().bufferData.globalData);
                                }


                            }
                            //更新值
                            for (QuotationXKItem item : xkModel.getValuableDatas()) {
                                Product product = findProduct(productList, item.productId);


                                Product product2 = findProduct(productList, item.productId2);
                                if (product != null) {
                                    QuotationAnalytics.updateProduct(item, product, CacheManager.getInstance().bufferData.globalData);

                                }
                                if (product2 != null) {
                                    QuotationAnalytics.updateProduct2(item, product2, CacheManager.getInstance().bufferData.globalData);
                                }

                            }


                            //数据源改变

                            model.fireTableDataChanged();
                            fixColumnModel.fireTableDataChanged();
                            xkModel.fireTableDataChanged();
                            fixColumnXkModel.fireTableDataChanged();


                        } else {
                            JOptionPane.showMessageDialog(getWindow(), data.message);
                        }


                    }
                }.execute();


            }
        });


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

                    JPopupMenu menu = new QuotationItemPopMenu(source, new QuotationItemPopMenu.TableMenuLister() {
                        @Override
                        public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {

                            switch (index) {
                                case QuotationItemPopMenu.ITEM_INSERT:


                                    notifyModelRowInsert(rowIndex[0], rowIndex[0]);

                                    break;
                                case QuotationItemPopMenu.ITEM_DELETE:
                                    notifyModelRowDelete(rowIndex[0], rowIndex[0]);

                                    break;
                                case QuotationItemPopMenu.ITEM_APPEND:

                                    notifyModelRowInsert(rowIndex[0], rowIndex[1]);


                                    break;

                            }

                        }
                    });


                    menu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        };

        tb.addMouseListener(adapter);
        fixedTable.addMouseListener(adapter);


        fixedTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }


    private void notifyAllModel() {

        if (data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {
            model.fireTableDataChanged();
            fixColumnModel.fireTableDataChanged();
        } else {


            xkModel.fireTableDataChanged();
            fixColumnXkModel.fireTableDataChanged();

        }


    }


    private void notifyModelRowInsert(int startRow, int endRow) {


        if (data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {

            for (int i = startRow; i <= endRow; i++)
                quotationItems.add(i, new QuotationItem());
            model.fireTableRowsInserted(startRow, endRow);
            fixColumnModel.fireTableRowsInserted(startRow, endRow);
        } else {

            for (int i = startRow; i <= endRow; i++)
                quotationXKItems.add(i, new QuotationXKItem());
            xkModel.fireTableRowsInserted(startRow, endRow);
            fixColumnXkModel.fireTableRowsInserted(startRow, endRow);

        }

        viewport.setPreferredSize(fixedTable.getPreferredSize());

        fixedTable.setRowSelectionInterval(startRow, startRow);

    }

    private void notifyModelRowDelete(int startRow, int endRow) {


        if (data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {

            for (int i = endRow; i >= startRow; i--)
                quotationItems.remove(i);

            model.fireTableRowsDeleted(startRow, endRow);
            fixColumnModel.fireTableRowsDeleted(startRow, endRow);
        } else {
            for (int i = endRow; i >= startRow; i--)
                quotationXKItems.remove(i);
            xkModel.fireTableRowsDeleted(startRow, endRow);
            fixColumnXkModel.fireTableRowsDeleted(startRow, endRow);

        }
        viewport.setPreferredSize(fixedTable.getPreferredSize());

    }


    private void notifyModelRowUpdate(int startRow, int endRow) {


        if (data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {

            fixColumnModel.fireTableRowsUpdated(startRow, endRow);

            model.fireTableRowsUpdated(startRow, endRow);
        } else {

            fixColumnXkModel.fireTableRowsUpdated(startRow, endRow);

            xkModel.fireTableRowsUpdated(startRow, endRow);

        }
        //  viewport.setPreferredSize(fixedTable.getPreferredSize());

    }

    private Product findProduct(List<Product> products, long id) {
        for (Product product : products) {
            if (product.id == id) {
                return product;
            }

        }
        return null;
    }


    private void configTableEditor() {


        //定制表格的编辑功能 弹出物料选择单

        final JTextField jtf = new JTextField();
        jtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                if (!jtf.hasFocus())
                    jtf.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if (!jtf.hasFocus())
                    jtf.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });


        jtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();
                handleTableProductInput(tb, text);

            }
        });


        DefaultCellEditor editor = new DefaultCellEditor(jtf);
        fixedTable.setDefaultEditor(Product.class, editor);

        JComboBox<String> ComboBox = new JComboBox<>();
        ComboBox.addItem("Y");
        ComboBox.addItem("N");
        DefaultCellEditor booleanEditor=new DefaultCellEditor(ComboBox);
        tb.setDefaultEditor(String.class,booleanEditor);
    }


    private void configProduct2Editor() {

        //定制表格的编辑功能 弹出物料选择单

        final JTextField jtf_product2 = new JTextField();
        jtf_product2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf_product2.hasFocus())
                    jtf_product2.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf_product2.hasFocus())
                    jtf_product2.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });


        jtf_product2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();
                handleTableProduct2Input(fixedTable, text);

            }
        });


        DefaultCellEditor editor = new DefaultCellEditor(jtf_product2);
        fixedTable.setDefaultEditor(Product2.class, editor);
    }


    /**
     * 处理表格的产品搜索
     *
     * @param tb
     * @param text
     */
    private void handleTableProductInput(final JHdTable tb, final String text) {


        final int rowIndex = tb.convertRowIndexToModel(tb.getSelectedRow());
        //查询  单记录直接copy
        new HdSwingWorker<Product, Object>(SwingUtilities.getWindowAncestor(getRoot())) {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {


                return apiManager.readProductList(text, 0, 100);

            }

            @Override
            public void onResult(RemoteData<Product> data) {


                Product product = null;
                if (data == null) {
                    //表示操作取消
                    return;
                }

                if (data.isSuccess() && data.totalCount == 1) {


                    product = data.datas.get(0);


                } else {


                    SearchDialog<Product> dialog = new SearchDialog.Builder().setWindow(getWindow(root)).setTableModel(new ProductTableModel()).setCommonSearch(new CommonSearchAdapter<Product>() {

                        @Override
                        public RemoteData<Product> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.readProductList(value, pageIndex, pageCount);
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.show(tb);
                    product = dialog.getResult();


                }

                if (product != null) {

                    final GlobalData globalData = CacheManager.getInstance().bufferData.globalData;
                    if (Panel_QuotationDetail.this.data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {

                        QuotationAnalytics.updateProduct(Panel_QuotationDetail.this.quotationItems.get(rowIndex), product, globalData);

                    } else {
                        QuotationAnalytics.updateProduct(Panel_QuotationDetail.this.quotationXKItems.get(rowIndex), product, globalData);
                    }

                    notifyModelRowUpdate(rowIndex, rowIndex);

                }


            }
        }.go();

    }


    /**
     * 处理表格的产品搜索
     *
     * @param tb
     * @param text
     */
    private void handleTableProduct2Input(final JHdTable tb, final String text) {

        final int rowIndex = tb.convertRowIndexToModel(tb.getSelectedRow());

        //查询  单记录直接copy
        new HdSwingWorker<Product, Object>(SwingUtilities.getWindowAncestor(getRoot())) {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {

                //return    apiManager.readSameNameProductList(item.productName, item.productId)  ;
                return apiManager.readProductList(text, 0, 100);
            }

            @Override
            public void onResult(RemoteData<Product> data) {


                Product product;
                if (data.isSuccess() && data.totalCount == 1) {

                    product = data.datas.get(0);


                } else {


                    SearchDialog<Product> dialog = new SearchDialog.Builder().setWindow(getWindow(root)).setTableModel(new ProductTableModel()).setCommonSearch(new CommonSearchAdapter<Product>() {


                        @Override
                        public RemoteData<Product> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.readProductList(value, pageIndex, pageCount);
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.show(tb);
                    product = dialog.getResult();


                }
                if (product != null) {

                    QuotationAnalytics.updateProduct2(quotationXKItems.get(rowIndex), product, CacheManager.getInstance().bufferData.globalData);
                    notifyModelRowUpdate(rowIndex, rowIndex);
                }


            }
        }.go();

    }

    @Override
    public JComponent getRoot() {
        return root;
    }


    public void setQuotationDelete(final QuotationDelete quotationDelete) {


        if (quotationDelete != null) {

            btn_save.setVisible(false);
            btn_delete.setVisible(false);
            btn_export.setVisible(false);
            btn_verify.setVisible(false);
            btn_unVerify.setVisible(false);
            btn_resume.setVisible(true);
            btn_resume.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    new HdSwingWorker<QuotationDetail, Void>(SwingUtilities.getWindowAncestor(getRoot())) {
                        @Override
                        protected RemoteData<QuotationDetail> doInBackground() throws Exception {
                            return apiManager.resumeDeleteQuotation(quotationDelete.id);
                        }

                        @Override
                        public void onResult(RemoteData<QuotationDetail> data) {


                            if (data.isSuccess()) {
                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), "数据恢复成功");
                                if (listener != null)
                                    listener.close();
                            } else {

                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), data.message);
                            }


                        }
                    }.go();


                }
            });
        }


    }

    public void setData(QuotationDetail data) {


        this.data = data;

        quotationItems.clear();
        quotationItems.addAll(data.items);
        quotationXKItems.clear();
        quotationXKItems.addAll(data.XKItems);


        Quotation quotation = data.quotation;
        switch ((int) quotation.quotationTypeId) {

            case Quotation.QUOTATION_TYPE_NORMAL:

                tb.setModel(model);
                fixedTable.setModel(fixColumnModel);
                break;
            case Quotation.QUOTATION_TYPE_XK:

                tb.setModel(xkModel);
                fixedTable.setModel(fixColumnXkModel);
                TableColumnModel cm = tb.getColumnModel();
                ColumnGroup g_name = new ColumnGroup("折叠包装");
                int startIndex = 0;
                for (int i = 0; i < 12; i++)
                    g_name.add(cm.getColumn(startIndex + i));
                ColumnGroup g_lang = new ColumnGroup("加强包装");
                startIndex = 12;
                for (int i = 0; i < 12; i++)
                    g_lang.add(cm.getColumn(startIndex + i));

                GroupableTableHeader header = (GroupableTableHeader) tb.getTableHeader();
                header.addColumnGroup(g_name);
                header.addColumnGroup(g_lang);
                break;
        }


        jtf_number.setText(quotation.qNumber);
        qDate.getJFormattedTextField().setText(quotation.qDate);
        vDate.getJFormattedTextField().setText(quotation.vDate);
        jtf_company.setText(quotation.companyName);

        int index = -1;

        for (int i = 0, count = cb_customer.getItemCount(); i < count; i++) {

            if (cb_customer.getItemAt(i).id == quotation.customerId) {

                index = i;
                break;
            }

        }

        cb_customer.setSelectedIndex(index
        );

        index = -1;

        for (int i = 0, count = cb_salesman.getItemCount(); i < count; i++) {

            if (cb_salesman.getItemAt(i).id == quotation.salesmanId) {

                index = i;
                break;
            }

        }

        cb_salesman.setSelectedIndex(index
        );


        for (int i = 0, count = cb_currency.getItemCount(); i < count; i++) {

            if (cb_currency.getItemAt(i).equals(quotation.currency)) {

                index = i;
                break;
            }

        }
        cb_currency.setSelectedIndex(index);

        ta_memo.setText(quotation.memo);

        model.setExternalData(quotationItems);
        xkModel.setExternalData(quotationXKItems);

        fixColumnModel.setExternalData(quotationItems);
        fixColumnXkModel.setExternalData(quotationXKItems);


        boolean hasVerify = false;

        if (data != null && data.quotation != null)
            hasVerify = data.quotation.isVerified;


        model.setHasVerify(hasVerify);
        xkModel.setHasVerify(hasVerify);
        fixColumnModel.setHasVerify(hasVerify);
        fixColumnXkModel.setHasVerify(hasVerify);


        //设置固定列表的宽度
        viewport.setPreferredSize(fixedTable.getPreferredSize());


        jp_verify.setVisible(true);
        icon_verify.setVisible(data.quotation.isVerified);


        icon_overdue.setVisible(data.quotation.isOverdue());


        boolean isVerifiable = AuthorityUtil.getInstance().verifyQuotation();
        boolean isEditable = AuthorityUtil.getInstance().editQuotation();


        //非审核状态方可编辑
        // btn_save.setEnabled(!data.quotation.isVerified);

        //btn_delete.setEnabled(!data.quotation.isVerified);

        //有编辑没有审核权限才可看见，
        // btn_save.setVisible(!isVerifiable && isEditable);

        //btn_delete.setVisible(!isVerifiable && isEditable);


        btn_verify.setVisible(isVerifiable);
        btn_unVerify.setVisible(isVerifiable);
        btn_export.setVisible(AuthorityUtil.getInstance().exportQuotation());


        //未审核 撤销审核按钮不可用
        btn_unVerify.setEnabled(data.quotation.isVerified);


        //未审核才能重新导入产品信息
        btn_reimport.setEnabled(!data.quotation.isVerified);
        //未审核才能保存
        btn_save.setEnabled(!data.quotation.isVerified);
        //审核后方可导出
        //btn_export.setEnabled(data.quotation.isVerified);

        bindLogData(data.quotationLog);


    }


    public void checkData(QuotationDetail detail) throws HdUIException {


        if (StringUtils.isEmpty(jtf_number.getText().trim())) {
            throw HdUIException.create(jtf_number, "请输入报价单号");
        }

        if (cb_customer.getSelectedItem() == null) {
            throw HdUIException.create(cb_customer, "请选择客户");
        }


        if (cb_salesman.getSelectedItem() == null) {
            throw HdUIException.create(cb_salesman, "请选择业务员");
        }
        if (cb_currency.getSelectedItem() == null) {
            throw HdUIException.create(cb_currency, "请选择币种");
        }

        if (StringUtils.isEmpty(qDate.getJFormattedTextField().getText())) {
            throw HdUIException.create(qDate, "请选择报价日期");
        }
        if (StringUtils.isEmpty(vDate.getJFormattedTextField().getText())) {
            throw HdUIException.create(vDate, "请选择有效日期");
        }


    }

    /**
     * 获取数据
     *
     * @param data
     */
    public void getData(QuotationDetail data) {

        Quotation quotation = data.quotation;

        quotation.qNumber = jtf_number.getText().trim();
        quotation.currency = String.valueOf(cb_currency.getSelectedItem() == null ? "" : cb_currency.getSelectedItem());
        Customer selectedCustomer = (Customer) cb_customer.getSelectedItem();
        if (selectedCustomer != null) {
            quotation.customerId = selectedCustomer.id;
            quotation.customerCode = selectedCustomer.code;
            quotation.customerName = selectedCustomer.name;
        }
        User selectedSalesman = (User) cb_salesman.getSelectedItem();
        if (selectedSalesman != null) {
            quotation.salesmanId = selectedSalesman.id;
            quotation.salesman = selectedSalesman.chineseName;
        }
        quotation.qDate = qDate.getJFormattedTextField().getText().trim();
        quotation.vDate = vDate.getJFormattedTextField().getText().trim();
        quotation.memo = ta_memo.getText();
        quotation.companyName = jtf_company.getText();


        //空白行过滤
        if (data.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_NORMAL) {
            List<QuotationItem> valuableItem = new ArrayList<>();
            for (QuotationItem item : quotationItems) {
                if (!item.isEmpty())
                    valuableItem.add(item);
            }
            data.items.clear();
            data.items.addAll(valuableItem);


        } else {
            List<QuotationXKItem> valuableItem = new ArrayList<>();
            for (QuotationXKItem item : quotationXKItems) {
                if (!item.isEmpty())
                    valuableItem.add(item);
            }
            data.XKItems.clear();
            data.XKItems.addAll(valuableItem);

        }


    }


    /**
     * 绑定修改记录信息
     *
     * @param quotationLog
     */
    private void bindLogData(QuotationLog quotationLog) {


        jp_log.setVisible(quotationLog != null);


        if (quotationLog == null) {
            return;
        }


        creator.setText(quotationLog.creatorCName);
        createTime.setText(quotationLog.createTimeString);
        updateTime.setText(quotationLog.updateTimeString);
        updator.setText(quotationLog.updatorCName);

    }


    private void createUIComponents() {


        JDatePanelImpl picker = new JDatePanelImpl(null);
        qDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());
        picker = new JDatePanelImpl(null);
        vDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());


        cb_customer = new JComboBox<Customer>();
        for (Customer customer : CacheManager.getInstance().bufferData.customers) {
            cb_customer.addItem(customer);
        }

        cb_salesman = new JComboBox<User>();
        for (User salesman : CacheManager.getInstance().bufferData.salesmans) {
            cb_salesman.addItem(salesman);
        }

        //crate table  and addd table group header
        tb = createCustomTable(false);
        fixedTable = createCustomTable(true);

        fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jtScroll = new JScrollPane(tb);
        viewport = new JViewport();
        viewport.setView(fixedTable);
        viewport.setPreferredSize(fixedTable.getPreferredSize());
        jtScroll.setRowHeaderView(viewport);
        jtScroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable
                .getTableHeader());


    }


    /**
     * 设置表主从表关联
     *
     * @param isFixedTable
     */
    private void checkSelection(boolean isFixedTable) {
        int fixedSelectedIndex = fixedTable.getSelectedRow();
        int selectedIndex = tb.getSelectedRow();
        if (fixedSelectedIndex != selectedIndex) {
            if (isFixedTable) {
                if (fixedSelectedIndex < 0 || fixedSelectedIndex >= tb.getRowCount())
                    tb.clearSelection();
                else
                    tb.setRowSelectionInterval(fixedSelectedIndex,
                            fixedSelectedIndex);
            } else {
                if (selectedIndex < 0 || selectedIndex >= fixedTable.getRowCount())
                    fixedTable.clearSelection();
                else
                    fixedTable
                            .setRowSelectionInterval(selectedIndex, selectedIndex);
            }
        }
    }

    private JHdTable createCustomTable(final boolean isFixTable) {
        return new JHdTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                }
                return comp;
            }


            public void valueChanged(ListSelectionEvent e) {
                super.valueChanged(e);
                checkSelection(isFixTable);
            }
        };
    }


    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Window getWindow() {
        return getWindow(getRoot());
    }


    public static interface QuotationCostPriceRatioChangeListener {


        void onChange(QuotationItem quotationItem, float cost_price_ratio, float newValue);

    }

    public interface QuotationXkCostPriceRatioChangeListener {


        void onChange(QuotationXKItem quotationItem, float cost_price_ratio, float newValue);

    }
}
