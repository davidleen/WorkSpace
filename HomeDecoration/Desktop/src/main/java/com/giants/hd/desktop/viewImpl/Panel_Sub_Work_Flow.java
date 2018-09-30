package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.local.ClipBordHelper;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.SubWorkFlowListTableModel;
import com.giants.hd.desktop.mvp.presenter.SubWorkFlowListIPresenter;
import com.giants.hd.desktop.mvp.viewer.SubWorkFlowLIstViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * 小工序报表
 * Created by davidleen29 on 2016/7/12.
 */
public class Panel_Sub_Work_Flow extends BasePanel implements SubWorkFlowLIstViewer {
    private JPanel root;
    private JPanel panel1;
    private JHdTable tb;

    private JTextField jtf_key;
    private JButton btn_search;

    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;
    private JButton export;

    @Inject
    SubWorkFlowListTableModel tableModel;

    @Override
    public JComponent getRoot() {
        return root;
    }

    SubWorkFlowListIPresenter presenter;


    public Panel_Sub_Work_Flow(final SubWorkFlowListIPresenter presenter) {
        this.presenter = presenter;

        tb.setModel(tableModel);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(), date_start.getJFormattedTextField().getText(), date_end.getJFormattedTextField().getText());

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



        tb.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             *
             * @param e
             */
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                //黏贴
                if (e.isControlDown()) {

                    if (e.getKeyCode() == KeyEvent.VK_C) {

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                final int[] selectedRowSOnModel = JTableUtils.getSelectedRowSOnModel(tb);
                                if (selectedRowSOnModel != null && selectedRowSOnModel.length == 1) {
                                    final Sub_workflow_state item = tableModel.getItem(selectedRowSOnModel[0]);
//                                    System.out.println("aaaaaaaaaaaaaaaa" + item.tz_no);
                                    ClipBordHelper.setSysClipboardText(item.tz_no);

                                }
                            }
                        });



                    }
                }


            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //  presenter.exportExcel();
            }
        });

        export.setVisible(false);


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

    @Override
    public void bindData(List<Sub_workflow_state> states) {

        tableModel.setDatas(states);

    }
}
