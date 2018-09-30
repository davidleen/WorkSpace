package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.OrderListAdapter;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.QuotationHelper;
import com.giants.hd.desktop.model.AppQuotationTableModel;
import com.giants.hd.desktop.mvp.presenter.AppQuotationListPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationListViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.ErpOrder;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 广交会报价面板  presenter
 * Created by david on 2018/3/29.
 */
public class Panel_AppQuotation_List extends BasePanel  implements AppQuotationListViewer {
    private final AppQuotationListPresenter presenter;
    private JPanel panel1;
    private JButton btn_search;
    private JTextField keys;
    private JHdTable orderTable;
    private Panel_Page pagePanel;
    private JButton add;
    private JDatePickerImpl startDate;
    private JDatePickerImpl endDate;
    private JComboBox cb_salesman;

    AppQuotationTableModel tableModel;



    public Panel_AppQuotation_List(final AppQuotationListPresenter presenter) {

        this.presenter = presenter;
        tableModel = new AppQuotationTableModel();
        orderTable.setModel(tableModel);

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {

                startSearch(pageIndex,pageSize);
            }
        });

        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                startSearch(0,pagePanel.getPageSize());
            }
        });
        keys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                startSearch(0,pagePanel.getPageSize());

            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.addOne();
            }
        });


        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = orderTable.getSelectedRow();
                    int modelRow = orderTable.convertRowIndexToModel(row);
                    Quotation item = tableModel.getItem(modelRow);
                    presenter.showOne(item);

                }
            }
        });

        cb_salesman.setVisible(! CacheManager.getInstance().bufferData.quoteAuth.limitSelf);
        List<User> users= QuotationHelper.getQuotationUsersOnLoginUser();
        for (User user:users)
            cb_salesman.addItem(user);


    }

    private void startSearch(int pageIndex, int pageSize)
    {
        final User selectedItem = (User) cb_salesman.getSelectedItem();
        final long salemansId;
        if ( CacheManager.getInstance().bufferData.quoteAuth.limitSelf) {
            salemansId = CacheManager.getInstance().bufferData.loginUser.id;
        } else {
            salemansId = selectedItem.id;
        }
        presenter.search(keys.getText().trim(),startDate.getJFormattedTextField().getText(),endDate.getJFormattedTextField().getText(), salemansId,pageIndex,pageSize);


    }

    @Override
    public void bindData(RemoteData<Quotation> remoteData) {

        pagePanel.bindRemoteData(remoteData);
        tableModel.setDatas(remoteData.datas);
    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    @Override
    public void performSearch() {



        btn_search.doClick();


    }


    private void createUIComponents() {
        startDate=createDateView(-60);
        endDate =  createDateView(30);

    }
}
