package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.BaseListTableModel;
import com.giants.hd.desktop.model.TableField;

import com.giants.hd.desktop.mvp.presenter.WorkFlowMessageReportPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowEventConfigViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowMessageReportViewer;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.utils.StringUtils;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by davidleen29 on 2018/7/7.
 */
public class Panel_Work_Flow_Message_Report  extends BasePanel implements WorkFlowMessageReportViewer {

    private JPanel root;
    private JHdTable jt;
    private JButton exportExcel;
    private JButton search;
    private JCheckBox cb_unhandle;
    private JCheckBox cb_overdue;
    private JDatePickerImpl startDate;
    private JDatePickerImpl endDate;
    private WorkFlowMessageReportPresenter presenter;
    BaseListTableModel<WorkFlowMessage> model;

    public Panel_Work_Flow_Message_Report(final WorkFlowMessageReportPresenter presenter) {
        this.presenter = presenter;
        final List<TableField> workFlowMessageFieldList = TableStructureUtils.getWorkFlowMessage();
        model=new BaseListTableModel<WorkFlowMessage>(WorkFlowMessage.class);
        model.updateStructure(workFlowMessageFieldList);
        jt.setModel(model);




        jt.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = jt.getSelectedRow();
                    WorkFlowMessage mesage = model.getItem(row);


                    int column = jt.convertColumnIndexToModel(jt.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showProductDialog(getWindow(getRoot()), mesage.productName, mesage.pVersion,mesage.url);
                    }


                }

            }
        });

        exportExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.exportExcel(workFlowMessageFieldList);
            }
         });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean unhandle=cb_unhandle.isSelected();
                boolean overdue=cb_overdue.isSelected();

                String dateStart=startDate.getJFormattedTextField().getText();
                String dateEnd=endDate.getJFormattedTextField().getText();


                if( dateEnd.compareToIgnoreCase(dateStart)<1) {
                    showMesssage("结束日期必须大于开始日期");
                    return ;
                }
                presenter.search(dateStart,dateEnd,unhandle,overdue);





            }
        });
    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void bindData(List<WorkFlowMessage> datas) {

        model.setDatas(datas);
    }


    private void createUIComponents() {
        startDate=createDateView(-60);
        endDate =  createDateView(30);

    }

}
