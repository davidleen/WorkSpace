package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.AppQuoteAuthModel;
import com.giants.hd.desktop.model.OrderAuthModel;
import com.giants.hd.desktop.model.QuoteAuthModel;
import com.giants.hd.desktop.model.StockOutAuthModel;
import com.giants.hd.desktop.mvp.presenter.AuthRelateDetailIPresenter;
import com.giants.hd.desktop.mvp.viewer.AuthRelateDetailViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.OrderAuth;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.StockOutAuth;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.AppQuoteAuth;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * n
 */
public class Panel_Auth_Relates extends BasePanel implements AuthRelateDetailViewer {
    private JPanel contentPane;
    private JButton save;
    private JHdTable tb_quote;
    private JPanel panel_relateSales;
    private JTabbedPane tabs;
    private JHdTable tb_stock;
    private JHdTable tb_order;
    private JHdTable tb_app_quote;


    AppQuoteAuthModel appQuoteAuthModel;

    QuoteAuthModel quoteAuthModel;
    OrderAuthModel orderAuthModel;
    StockOutAuthModel stockOutAuthModel;


    AuthRelateDetailIPresenter presenter;

    public Panel_Auth_Relates(Window window, final AuthRelateDetailIPresenter presenter) {
        super(window);
        this.presenter = presenter;


//        panel_relateSales.setVisible(false);
        tb_quote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int modelRow = tb_quote.convertRowIndexToModel(tb_quote.getSelectedRow());


                    showRow(tb_quote,modelRow);
                }
            }
        });



        tb_stock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int modelRow = tb_stock.convertRowIndexToModel(tb_stock.getSelectedRow());


                    showRow(tb_stock,modelRow);
                }
            }
        });


        tb_order
                .addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int modelRow = tb_order.convertRowIndexToModel(tb_order.getSelectedRow());


                    showRow(tb_order,modelRow);
                }
            }
        });
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                presenter.setSelectedPane(tabs.getSelectedIndex());

            }
        });

        quoteAuthModel = new QuoteAuthModel();
        quoteAuthModel.setRowAdjustable(false);
        tb_quote.setModel(quoteAuthModel);


        orderAuthModel = new OrderAuthModel();
        orderAuthModel.setRowAdjustable(false);
        tb_order.setModel(orderAuthModel);

        stockOutAuthModel = new StockOutAuthModel();
        stockOutAuthModel.setRowAdjustable(false);
        tb_stock.setModel(stockOutAuthModel);



        appQuoteAuthModel = new AppQuoteAuthModel();
        appQuoteAuthModel.setRowAdjustable(false);
        tb_app_quote.setModel(appQuoteAuthModel);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();
            }
        });

    }

    @Override
    public JComponent getRoot() {
        return contentPane;
    }


    /**
     * 显示指定行数
     *
     * @param row
     */
    private void showRow(JTable table,int row) {


        panel_relateSales.setVisible(row >= 0);
        table.setRowSelectionInterval(row, row);
        presenter.onQuoteAuthRowSelected(row);

    }

    @Override
    public void showQuoteAuthList(List<QuoteAuth> quoteAuth) {

        quoteAuthModel.setDatas(quoteAuth);

    }

    @Override
    public void bindRelateSalesData(List<Integer> indexs) {


        int size = panel_relateSales.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component component = panel_relateSales.getComponent(i);
            if (component instanceof JRadioButton) {

                JRadioButton jRadioButton = (JRadioButton) component;

                jRadioButton.removeItemListener(itemListener);


                jRadioButton.setSelected(indexs.indexOf(i) > -1);

                jRadioButton.addItemListener(itemListener);
            }

        }

        panel_relateSales.setVisible(true);


    }

    @Override
    public void showStockOutAuthList(List<StockOutAuth> datas) {
        stockOutAuthModel.setDatas(datas);

    }

    @Override
    public void showOrderAuthList(List<OrderAuth> orderAuths) {
        orderAuthModel.setDatas(orderAuths);

    }

    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {


            StringBuilder newRelates = new StringBuilder();
            //读取所有item  保存
            int size = panel_relateSales.getComponentCount();
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                JRadioButton jRadioButton = (JRadioButton) panel_relateSales.getComponent(i);
                if (jRadioButton.isSelected()) {
                    indexes.add(i);

                }


            }
            presenter.onRelateUsesSelected(indexes);


        }
    };


    @Override
    public void showAllSales(List<User> salesList) {

        panel_relateSales.setLayout(new GridLayout(0, 1, 10, 10));
        for (User user : salesList) {

            JRadioButton radioButton = new JRadioButton(user.code + "-" + user.name + "-" + user.chineseName);
            panel_relateSales.add(radioButton);
        }

//        panel_relateSales.revalidate();
//        panel_relateSales.setVisible(false);


    }

    @Override
    public void showPaneAndRow(int selectedPane, int showRow) {


        tabs.setSelectedIndex(selectedPane);

        switch (showRow) {
            case 0:
                tb_quote.setRowSelectionInterval(showRow, showRow);
                break;
            case 1:

                tb_order.setRowSelectionInterval(showRow, showRow);
                break;
            case 2
                    :

                tb_stock.setRowSelectionInterval(showRow, showRow);
                break; case 3
                    :

                tb_app_quote.setRowSelectionInterval(showRow, showRow);
                break;
        }


    }

    @Override
    public void showQuoteRow(int showRow) {

        tb_quote.setRowSelectionInterval(showRow,showRow);

    }

    @Override
    public void showOrderRow(int showRow) {
        if(showRow>=0&&tb_order.getRowCount()>0&&showRow<tb_order.getRowCount())
        tb_order.setRowSelectionInterval(showRow,showRow);
    }

    @Override
    public void showStockOutRow(int showRow) {
        tb_stock.setRowSelectionInterval(showRow,showRow);
    }

    @Override
    public void showAppQuoteAuthList(List<AppQuoteAuth> quoteAuth) {
        appQuoteAuthModel.setDatas(quoteAuth);
    }
}
