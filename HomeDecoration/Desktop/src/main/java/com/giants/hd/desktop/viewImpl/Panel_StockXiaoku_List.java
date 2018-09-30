package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.StockXiaokuTableModel;
import com.giants.hd.desktop.mvp.presenter.StockXiaokuIPresenter;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockXiaoku;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Calendar;

/**
 * 界面   销库列表 出库到货柜
 * Created by davidleen29 on 2016/7/12.
 */
public class Panel_StockXiaoku_List extends BasePanel {
    private JPanel root;
    private JPanel panel1;
    private JHdTable tb;

    private JButton btn_search;

    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;
    private Panel_Page pageController;
    private JTextField key;
    @Inject
    StockXiaokuTableModel tableModel;

    @Override
    public JComponent getRoot() {
        return root;
    }

    StockXiaokuIPresenter presenter;


    public Panel_StockXiaoku_List(final StockXiaokuIPresenter presenter) {
        this.presenter = presenter;

        tb.setModel(tableModel);


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (presenter != null)
                    presenter.search(key.getText().trim(),0, pageController.getPageSize());

            }
        });



        pageController.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {

                if (presenter != null)
                    presenter.search(key.getText(),pageIndex, pageSize);
            }
        });

        tb.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if(presenter!=null&&e.getClickCount()==2)
                {
                    int row = tb.getSelectedRow();
                    int modelRow = tb.convertRowIndexToModel(row);
                    StockXiaoku xiaoku = tableModel.getItem(modelRow);
                    presenter.onListItemClick(xiaoku);
                }

            }
        });


    }


    public void setData(RemoteData<StockXiaoku> remoteData) {


        if (remoteData != null && remoteData.isSuccess()) {
            tableModel.setDatas(remoteData.datas);


        }
        pageController.bindRemoteData(remoteData);

    }


    private void createUIComponents() {
        JDatePanelImpl picker = new JDatePanelImpl(null);
        final HdDateComponentFormatter formatter = new HdDateComponentFormatter();
        date_start = new JDatePickerImpl(picker, formatter);

        try {
            long aMonthAgo = Calendar.getInstance().getTimeInMillis() - 30l * 24 * 60 * 60 * 1000;
            final Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(aMonthAgo);
            date_start.getJFormattedTextField().setText(formatter.valueToString(instance));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        picker = new JDatePanelImpl(null);
        date_end = new JDatePickerImpl(picker, formatter);
        try {
            date_end.getJFormattedTextField().setText(formatter.valueToString(Calendar.getInstance()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
