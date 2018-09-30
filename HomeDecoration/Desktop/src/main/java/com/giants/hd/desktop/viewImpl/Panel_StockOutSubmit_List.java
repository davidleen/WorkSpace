package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.StockOutSubmitTableModel;
import com.giants.hd.desktop.mvp.presenter.StockOutSubmitIPresenter;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
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
 * 界面   销库库列表
 * Created by davidleen29 on 2016/7/12.
 */
public class Panel_StockOutSubmit_List extends BasePanel {
    private JPanel root;
    private JPanel panel1;
    private JHdTable tb;

    private JTextField jtf_key;
    private JButton btn_search;

    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;
    private JButton export;

    @Inject
    StockOutSubmitTableModel tableModel;

    @Override
    public JComponent getRoot() {
        return root;
    }

    StockOutSubmitIPresenter presenter;


    public Panel_StockOutSubmit_List(final StockOutSubmitIPresenter presenter) {
        this.presenter = presenter;

        tb.setModel(tableModel);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);



        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


             if (presenter != null)
                  presenter.search(jtf_key.getText().trim(),date_start.getJFormattedTextField().getText(),date_end.getJFormattedTextField().getText());

            }
        });



        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {



//                if(presenter!=null&&e.getClickCount()==2)
//                {
//                    int row = tb.getSelectedRow();
//                    int modelRow = tb.convertRowIndexToModel(row);
//                    ErpStockOut erpOrder = tableModel.getItem(modelRow);
//                    presenter.onListItemClick(erpOrder);
//                }



            }
        });



        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.exportExcel();
            }
        });





    }



    public void setData(RemoteData<StockSubmit> remoteData) {


        if (remoteData != null && remoteData.isSuccess()) {
            tableModel.setDatas(remoteData.datas);


        }


    }


    private void createUIComponents() {
        JDatePanelImpl picker = new JDatePanelImpl(null);
        final HdDateComponentFormatter formatter = new HdDateComponentFormatter();
        date_start = new JDatePickerImpl(picker, formatter);

        try {
            long aMonthAgo=Calendar.getInstance().getTimeInMillis()-30l*24*60*60*1000;
            final Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(aMonthAgo );
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
