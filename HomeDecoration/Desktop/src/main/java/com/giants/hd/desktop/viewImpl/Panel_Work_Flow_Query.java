package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.ClipBordHelper;
import com.giants.hd.desktop.local.ComboItem;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.mvp.presenter.SubWorkFlowListIPresenter;
import com.giants.hd.desktop.mvp.presenter.WorkFlowQueryPresenter;
import com.giants.hd.desktop.mvp.viewer.SubWorkFlowLIstViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowQueryViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.noEntity.RemoteData;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * 小工序报表
 * Created by davidleen29 on 2016/7/12.
 */
public class Panel_Work_Flow_Query extends BasePanel implements WorkFlowQueryViewer {
    private JPanel root;
    private JHdTable tb;

    private JTextField jtf_key;
    private JButton btn_search;

    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;
    private Panel_Page pagePanel;
    private JTabbedPane tabbedPane1;
    private JHdTable tb_alert;
    private JComboBox cb_flow;
    private JButton search_alert;
    private JComboBox cb_alert;
    private Panel_Page page_alert;
    private JTextField key_alert;
    private JComboBox cb_state;

    ErpOrderItemTableModel tableModel=new ErpOrderItemTableModel();
    ErpOrderItemTableModel model_alert=new ErpOrderItemTableModel();

    @Override
    public JComponent getRoot() {
        return root;
    }

    WorkFlowQueryPresenter presenter;


    public Panel_Work_Flow_Query(final WorkFlowQueryPresenter presenter) {
        this.presenter = presenter;

        tb.setModel(tableModel);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tb_alert.setModel(model_alert);
        tb_alert.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(), 0, pagePanel.getPageSize());

            }
        });

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {
                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(), pageIndex, pageSize);
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

        tb.setDefaultRenderer(String.class, getRenderer());
         tb_alert.setDefaultRenderer(String.class, getRenderer());



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

//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                final int[] selectedRowSOnModel = JTableUtils.getSelectedRowSOnModel(tb);
//                                if (selectedRowSOnModel != null && selectedRowSOnModel.length == 1) {
//                                    final Sub_workflow_state item = tableModel.getItem(selectedRowSOnModel[0]);
////                                    System.out.println("aaaaaaaaaaaaaaaa" + item.tz_no);
//                                    ClipBordHelper.setSysClipboardText(item.tz_no);
//
//                                }
//                            }
//                        });



                    }
                }


            }
        });


        cb_flow.addItem(new ComboItem(-1,""));
        int length = ErpWorkFlow.STEPS.length;
        for (int i = 0; i < length; i++) {
            cb_flow.addItem(new ComboItem(ErpWorkFlow.STEPS[i],ErpWorkFlow.NAMES[i]));
        }

        cb_state.addItem(new ComboItem(-1,"所有"));
        cb_state.addItem(new ComboItem(ErpWorkFlow.STATE_WORKING,"在产"));
        cb_state.addItem(new ComboItem(ErpWorkFlow.STATE_COMPLETE,"完工"));
        page_alert.setListener(new PageListener(){
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {
                startQuery(pageIndex,pageSize);
            }
        });

        search_alert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                startQuery(0,page_alert.getPageSize());





            }
        });



        cb_alert.addItem(new ComboItem(-1,""));
        cb_alert.addItem(new ComboItem(0,"未预警"));
        cb_alert.addItem(new ComboItem(1,"黄色预警"));
        cb_alert.addItem(new ComboItem(2,"橙色预警"));
        cb_alert.addItem(new ComboItem(3,"红色预警"));




    }

    private DefaultTableCellRenderer getRenderer() {
        return new DefaultTableCellRenderer() {


            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int rowIndexToModel = table.convertRowIndexToModel(row);

                TableModel model = table.getModel();
                if (model instanceof AbsTableModel) {
                    AbsTableModel<ErpOrderItem> itemBaseTableModel = (AbsTableModel<ErpOrderItem>) model;

                    ErpOrderItem item = itemBaseTableModel.getItem(rowIndexToModel);


                    if (item.currentOverDueDay > 5) {
                        component.setBackground(Color.RED);
                        component.setForeground(Color.WHITE);


                    } else if (item.currentOverDueDay > 0) {

                        component.setBackground(Color.ORANGE);
                        component.setForeground(Color.WHITE);

                    } else if (item.currentOverDueDay != 0 && Math.abs(item.currentOverDueDay) <= item.currentAlertDay) {
                        component.setBackground(Color.YELLOW);
                        component.setForeground(Color.BLACK);

                    } else {
                        component.setBackground(isSelected?Color.BLUE:Color.WHITE);
                        component.setForeground(isSelected?Color.WHITE:Color.BLACK);

                    }



                }
                return component;
            }
        };
    }


    private void startQuery(int pageIndex, int pageSize)
    {
        ComboItem selectedItem = (ComboItem) cb_flow.getSelectedItem();
        int flowStep=selectedItem.value;

        ComboItem selectState
                = (ComboItem) cb_state.getSelectedItem();
        int state= selectState.value;

        ComboItem selectAlertType
                = (ComboItem) cb_alert.getSelectedItem();
        int selectAlert= selectAlertType.value;

        if (presenter != null) {

            presenter.queryOrderItems(key_alert.getText().trim(),state, flowStep, selectAlert,pageIndex, pageSize);

        }
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
    public void bindData(RemoteData<ErpOrderItem> data) {

        tableModel.setDatas(data.datas);
        pagePanel.bindRemoteData(data);

    }

    @Override
    public void bindAlertData(RemoteData<ErpOrderItem> data) {



        model_alert.setDatas(data.datas);
        page_alert.bindRemoteData(data);
    }
}
