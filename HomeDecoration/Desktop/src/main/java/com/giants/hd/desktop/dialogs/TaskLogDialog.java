package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.HdTaskLogModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.entity.HdTaskLog;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Calendar;

public class TaskLogDialog extends BaseDialog<HdTaskLog> {
    private JPanel contentPane;
    private JHdTable jtable;
    @Inject
    ApiManager apiManager;

    @Inject
    HdTaskLogModel taskLogTableModel;

    public TaskLogDialog(Window window) {
        super(window);
        init();
    }


    protected void init() {
        setContentPane(contentPane);
        setTitle("任务执行记录");
        setMinimumSize(new Dimension(750, 600));
        jtable.setModel(taskLogTableModel);


        jtable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {


                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


                        int rowIndexToModel = table.convertRowIndexToModel(row);

                        HdTaskLog data = taskLogTableModel.getItem(rowIndexToModel);

                        switch (data.state) {

                            case HdTaskLog.STATE_SUCCESS:
                                component.setForeground(Color.GREEN);
                                break;
                            case HdTaskLog.STATE_FAIL:
                                component.setForeground(Color.RED);
                                break;
                            default:
                                long time =
                                        Calendar.getInstance().getTimeInMillis();
                                component.setForeground(data.executeTime > time ? Color.DARK_GRAY : Color.LIGHT_GRAY);

                        }
                        return component;
                    }
                }
        );

    }

    public void setData(java.util.List<HdTaskLog> datas) {


        taskLogTableModel.setDatas(datas);
    }


}
