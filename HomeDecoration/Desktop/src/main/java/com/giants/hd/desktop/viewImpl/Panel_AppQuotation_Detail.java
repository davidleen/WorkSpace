package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.AppQuotationItemTableModel;
import com.giants.hd.desktop.mvp.presenter.AppQuotationDetailPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationDetailViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.TableMenuAdapter;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

/**
 * 广交会报价详情
 * Created by david on 2016/3/30.
 */
public class Panel_AppQuotation_Detail extends BasePanel implements AppQuotationDetailViewer {
    private final ItemListener customerItemListener;
    private final ItemListener salesmanItenListener;
    private JPanel panel1;
    private JHdTable table;

    private JTextField tf_qNumber;
    ;


    private JTextArea ta_memo;
    private JDatePickerImpl qDate;
    private JDatePickerImpl vDate;
    private JComboBox<Customer> cb_customer;
    private JComboBox<User> cb_salesman;
    private JButton save;
    private JButton add;
    private JButton movedown;
    private JButton delete;
    private JButton moveup;
    private JButton deleteQuotation;
    private JButton print;
    private JButton export_excel;
    private JButton export_pdf;
    private JButton setAllDiscount;
    private JButton cancelAllDiscount;
//    private JTextField tf_booth;


    private AppQuotationItemTableModel tableModel;

    private AppQuotationDetailPresenter presenter;
    private DocumentListener qNumberDocListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.updateQNumber(tf_qNumber.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.updateQNumber(tf_qNumber.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.updateQNumber(tf_qNumber.getText());

        }
    };
//    private DocumentListener boothDocListener = new DocumentListener() {
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            presenter.updateBooth(tf_booth.getText());
//        }
//
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            presenter.updateBooth(tf_booth.getText());
//        }
//
//        @Override
//        public void changedUpdate(DocumentEvent e) {
//
//            presenter.updateBooth(tf_booth.getText());
//
//        }
//    };
    private DocumentListener qDateDocListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.updateQDate(String.valueOf(qDate.getJFormattedTextField().getText()));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.updateQDate(String.valueOf(qDate.getJFormattedTextField().getText()));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.updateQDate(String.valueOf(qDate.getJFormattedTextField().getText()));
        }
    };

    private DocumentListener vDateDocListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.updateVDate(String.valueOf(vDate.getJFormattedTextField().getText()));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.updateVDate(String.valueOf(vDate.getJFormattedTextField().getText()));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.updateVDate(String.valueOf(vDate.getJFormattedTextField().getText()));
        }
    };
    private DocumentListener memoDocListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.updateMemo(ta_memo.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.updateMemo(ta_memo.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.updateMemo(ta_memo.getText());
        }
    };

    public Panel_AppQuotation_Detail(final AppQuotationDetailPresenter presenter) {

        this.presenter = presenter;
        tableModel = new AppQuotationItemTableModel(new AppQuotationItemTableModel.OnValueChangeListener() {
            @Override
            public void onPriceChange(int itemIndex, float newValue) {


                presenter.updateItemPrice(itemIndex, newValue);
            }

            @Override
            public void onQtyChange(int itemIndex, int newQty) {

                presenter.updateItemQty(itemIndex, newQty);

            }

            @Override
            public void onMemoChange(int itemIndex, String newValue) {
                presenter.updateItemMemo(itemIndex, newValue);
            }
        });

        table.setModel(tableModel);


        table.addMouseListener(new TableMenuAdapter(table, new String[]{"   添加   ", "   删除   ", "   打折   ", "   取消折扣   "}, new TableMenuAdapter.TableMenuListener() {
            @Override
            public void onMenuClick(JTable table, int index) {
                int[] selectRow = JTableUtils.getSelectedRowSOnModel(table);
                switch (index) {
                    case 0:
                        presenter.addItem(selectRow[0]);
                        break;
                    case 1:
                        presenter.deleteItem(selectRow);
                        break;
                    case 2:

                        String result=  JOptionPane.showInputDialog(getWindow(),"输入折扣值（0-1）之间","1");
                        if(result==null) return;
                        float value= 0;
                        try {
                            value = Float.valueOf(result);
                        } catch (NumberFormatException e1) {
                            e1.printStackTrace();
                        }
                        if(value>1||value<=0) {
                            showMesssage("折扣值设置不正确，必须在0-1之间");
                            return;
                        }

                        presenter.discountItem(selectRow,value);
                        break; case 3:
                        presenter.discountItem(selectRow, 1f);
                        break;
                }


            }
        }) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = table.getSelectedRow();
                    QuotationItem item = tableModel.getItem(row);


                    int column = table.convertColumnIndexToModel(table.getSelectedColumn());
                    //单击第一列 显示原图

                    switch (column) {
                        case 2:
                            ImageViewDialog.showDialog(getWindow(), HttpUrl.loadPicture(item.photoUrl), item.productName);
                            break;
                        case 1:

                            presenter.viewProduct(item.productId);


                            break;
                    }


                }

            }
        });
        customerItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Customer customer = (Customer) e.getItem();

                    presenter.setCustomer(customer);


                }


            }
        };
        cb_customer.addItemListener(customerItemListener);

        salesmanItenListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    User user = (User) e.getItem();

                    presenter.setSaleman(user);


                }


            }
        };
        cb_salesman.addItemListener(

                salesmanItenListener

        );

        cb_salesman.setEnabled(CacheManager.getInstance().bufferData.loginUser.isAdmin());
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.addItem(-1);
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.deleteItem(JTableUtils.getSelectedRowSOnModel(table));
            }
        });

        deleteQuotation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmMessage("确定删除当前报价单?"))
                    presenter.deleteQuotation();
            }
        });
        moveup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.moveUpItem(JTableUtils.getFirstSelectedRowSOnModel(table));
            }
        });
        movedown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.moveDownItem(JTableUtils.getFirstSelectedRowSOnModel(table));
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();
            }
        });
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.print();
            }
        });
        export_excel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.exportExcel();

            }
        });
        export_pdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.exportPdf();
            }
        });


        setAllDiscount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

              String result=  JOptionPane.showInputDialog(getWindow(),"输入折扣值（0-1）之间","1");
                if(result==null) return;
                float value= 0;
                try {
                    value = Float.valueOf(result);
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                }
                if(value>1||value<=0) {
                    showMesssage("折扣值设置不正确，必须在0-1之间");
                    return;
                }




                presenter.setAllDiscount(value);

            }
        });

        cancelAllDiscount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.cancelAllDiscount();
            }
        });

    }


    public void setEditable(boolean editable) {
        tf_qNumber.setEditable(editable);
       // tf_booth.setEditable(editable);
        vDate.setEnabled(editable);
        qDate.setEnabled(editable);


    }

    @Override
    public void bindDetail(QuotationDetail detailData) {


        bindData(detailData.quotation);

        tableModel.setDatas(detailData.items);


    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    @Override
    public void bindData(Quotation item) {


        tf_qNumber.getDocument().removeDocumentListener(qNumberDocListener);
        tf_qNumber.setText(item.qNumber);
        tf_qNumber.getDocument().addDocumentListener(qNumberDocListener);
//        tf_booth.getDocument().removeDocumentListener(boothDocListener);
//        tf_booth.setText(item.booth);
//        tf_booth.getDocument().addDocumentListener(boothDocListener);
        qDate.getJFormattedTextField().getDocument().removeDocumentListener(qDateDocListener);
        qDate.getJFormattedTextField().setText(item.qDate);
        qDate.getJFormattedTextField().getDocument().addDocumentListener(qDateDocListener);

        vDate.getJFormattedTextField().getDocument().removeDocumentListener(vDateDocListener);
        vDate.getJFormattedTextField().setText(item.vDate);
        vDate.getJFormattedTextField().getDocument().addDocumentListener(vDateDocListener);
        ta_memo.getDocument().removeDocumentListener(memoDocListener);
        ta_memo.setText(item.memo);
        ta_memo.getDocument().addDocumentListener(memoDocListener);
        cb_customer.removeItemListener(customerItemListener);

        int index = -1;

        for (int i = 0, count = cb_customer.getItemCount(); i < count; i++) {

            if (cb_customer.getItemAt(i).id == item.customerId) {

                index = i;
                break;
            }

        }

        cb_customer.setSelectedIndex(index
        );
        cb_customer.addItemListener(customerItemListener);


        index = -1;

        for (int i = 0, count = cb_salesman.getItemCount(); i < count; i++) {

            if (cb_salesman.getItemAt(i).id == item.saleId) {

                index = i;
                break;
            }

        }
        cb_salesman.removeItemListener(salesmanItenListener);
        cb_salesman.setSelectedIndex(index
        );
        cb_salesman.addItemListener(salesmanItenListener);
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
    }

}
