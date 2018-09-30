package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.filters.ExcelFileFilter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.reports.ReportFactory;
import com.giants.hd.desktop.reports.excels.ExcelReportor;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.noEntity.QuotationDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * 导出报价单面板
 */
public class ExportQuotationDialog extends BaseDialog<QuotationDetail> {
    private JPanel contentPane;
    private JComboBox cb_model;
    private JTextField jtf_path;
    private JButton btn_file;
    private JButton btn_confirm;
    private JCheckBox withPicture;

    private static String lastSelectedPath="";

    @Inject
    ReportFactory reportFactory;


    private QuotationDetail detail;
    public ExportQuotationDialog(Window window,QuotationDetail detail) {
        super(window,"报价模板选择");
        this.detail=detail;
        setContentPane(contentPane);


        for(QuotationFile s: QuotationFile.defaultFiles)
        {
            cb_model.addItem(s);
        }

        jtf_path.setText(lastSelectedPath);


        btn_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //选择excel 文件
                final File file = FileChooserHelper.chooseFile(JFileChooser.DIRECTORIES_ONLY, false,null);




                if (file != null) {

                /*
                    获得你选择的文件绝对路径
                */
                    String path = file.getAbsolutePath();
                    jtf_path.setText(path);
                    lastSelectedPath=path;
                }
            }
        });


        btn_confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(cb_model.getSelectedItem()==null)
                {
                    JOptionPane.showMessageDialog(ExportQuotationDialog.this,"请选择导出模板");
                        cb_model.requestFocus();
                    return;
                }
                if(StringUtils.isEmpty(jtf_path.getText().trim()) )
                {
                    JOptionPane.showMessageDialog(ExportQuotationDialog.this,"请选择导出路径");
                    jtf_path.requestFocus();
                    return;
                }



                boolean isExportPicture=withPicture.isSelected();


                doExportWork((QuotationFile) cb_model.getSelectedItem(),jtf_path.getText().trim(),isExportPicture);
            }
        });



        //默认关联对应客户的模板


        int itemCount=cb_model.getItemCount();

        int selectItem=0;
        for (int i = 0; i < itemCount; i++) {
            QuotationFile file= (QuotationFile) cb_model.getItemAt(i);
            if(file.name.contains(detail.quotation.customerName) ||file.name.contains(detail.quotation.customerCode))
            {
                selectItem=i;
                break;
            }
        }

        cb_model.setSelectedIndex(selectItem);



    }

    /**
     * 导出数据模型
     * @param file
     * @param exportPath
     */
    private void doExportWork(final QuotationFile file, final String exportPath, final boolean  isExportPicture) {


        new HdSwingWorker<Void,Void>(ExportQuotationDialog.this)
        {
            @Override
            protected RemoteData<Void> doInBackground() throws Exception {

            ExcelReportor reporter= reportFactory.createExcelReportor(file);
                reporter.setExportPicture(isExportPicture);
                reporter.report(detail, exportPath);

            //    ExportHelper.exportQuotation(detail, modelName, exportPath);

                return new RemoteData<Void>();
            }

            @Override
            public void onResult(RemoteData<Void> data) {


                JOptionPane.showMessageDialog(ExportQuotationDialog.this,"导出成功");

                dispose();


            }
        }.go();

    }


}
