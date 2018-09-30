package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.MapTableModel;
import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.presenter.AppQuotationCountReportPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationCountReportViewer;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.file.ImageUtils;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 广交会报价同步面板  presenter
 * Created by david on 2018/3/29.
 */
public class Panel_AppQuotation_Count_Report extends BasePanel implements AppQuotationCountReportViewer {
    private final AppQuotationCountReportPresenter presenter;
    private JPanel panel1;

 ;
    private JDatePickerImpl startDate;
    private JDatePickerImpl endDate;
    private JHdTable table;
    private JButton search;
    private JButton btn_excel;
    MapTableModel mapTableModel;

    public Panel_AppQuotation_Count_Report(final AppQuotationCountReportPresenter presenter) {

        this.presenter = presenter;

        final List<TableField> appQuoteCountReport = TableStructureUtils.getAppQuoteCountReport();
        mapTableModel=new MapTableModel(appQuoteCountReport);
        mapTableModel.setRowHeight(ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT);
        table.setModel(mapTableModel);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.searchReport( startDate.getJFormattedTextField().getText(), endDate.getJFormattedTextField().getText());
            }
        });
        table.addMouseListener(new MouseAdapter() {

        @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = table.getSelectedRow();
                    Map<String,String> item = mapTableModel.getItem(row);


                    int column = table.convertColumnIndexToModel(table.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showDialog(getWindow(), HttpUrl.loadPicture(item.get("url")), item.get("name"));
                    }


                }

            }
        });

        btn_excel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //选择excel 文件
                final File file = FileChooserHelper.chooseFile(JFileChooser.DIRECTORIES_ONLY, false,null);
                if(file==null) return;




                presenter.exportExcel(appQuoteCountReport,file.getAbsolutePath(),"报价次数统计报表--"+startDate.getJFormattedTextField().getText()+"-"+endDate.getJFormattedTextField().getText()+".xls");

            }
        });
    }

    @Override
    public void bindData(RemoteData<Map> data) {



        mapTableModel.setDatas(data.datas);



    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }

    private void createUIComponents() {
        startDate=createDateView(-60);
        endDate =  createDateView(0);

    }

}
