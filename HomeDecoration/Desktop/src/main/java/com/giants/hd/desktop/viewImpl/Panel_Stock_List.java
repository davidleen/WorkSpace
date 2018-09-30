package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.StockOutTableModel;
import com.giants.hd.desktop.mvp.presenter.StockListIPresenter;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 界面   出库列表
 * Created by davidleen29 on 2016/7/12.
 */
public class Panel_Stock_List extends BasePanel {
    private JPanel root;
    private JPanel panel1;
    private JHdTable tb;
    private Panel_Page pageController;
    private JTextField jtf_key;
    private JButton btn_search;
    private JComboBox cb_salesman;
    @Inject
    StockOutTableModel tableModel;

    @Override
    public JComponent getRoot() {
        return root;
    }

    StockListIPresenter presenter;


    public Panel_Stock_List(final StockListIPresenter presenter) {
        this.presenter = presenter;

        tb.setModel(tableModel);

        pageController.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(),getSalesId(), pageIndex, pageSize);
            }
        });


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(),getSalesId(), 0, pageController.getPageSize());

            }
        });



        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {



                if(presenter!=null&&e.getClickCount()==2)
                {
                    int row = tb.getSelectedRow();
                    int modelRow = tb.convertRowIndexToModel(row);
                    ErpStockOut erpOrder = tableModel.getItem(modelRow);
                    presenter.onListItemClick(erpOrder);
                }



            }
        });


        String relateSales = CacheManager.getInstance().bufferData.stockOutAuth.relatedSales;
        User all = new User();
        all.id = -1;
        all.code = "--";
        all.name = "--";
        all.chineseName = "所有人";
        cb_salesman.addItem(all);
        String[] ids = StringUtils.isEmpty(relateSales) ? null : relateSales.split(",|，");
        if (ids != null) {
            for (User user : CacheManager.getInstance().bufferData.salesmans) {

                boolean find = false;
                for (String s : ids) {
                    if (String.valueOf(user.id).equals(s)) {
                        find = true;
                        break;
                    }
                }

                if (find)
                    cb_salesman.addItem(user);
            }

        }




    }

    private long getSalesId()
    {
        return ((User)cb_salesman.getSelectedItem()).id;
    }

    public void setData(RemoteData<ErpStockOut> remoteData) {


        if (remoteData != null && remoteData.isSuccess()) {
            tableModel.setDatas(remoteData.datas);
            pageController.bindRemoteData(remoteData);

        }


    }
}
