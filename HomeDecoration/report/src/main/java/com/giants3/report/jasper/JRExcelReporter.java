package com.giants3.report.jasper;

import com.giants3.report.JRReporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**jasper 导出excel
 * Created by davidleen29 on 2018/10/1.
 */
public class JRExcelReporter extends JRReporter {

    private String outputFilePath;

    public JRExcelReporter(String outputFilePath) {

        this.outputFilePath = outputFilePath;
    }

    @Override
    public void doOutput(JasperPrint jp) {
        // 使用JRXlsxExporter导出器导出 其他导出器好像有很多都是JR开头可以引用看下如PDF导出器是JRPdfExproter
        JRXlsxExporter exporter = new JRXlsxExporter();        //设置输入项
        ExporterInput exporterInput = new SimpleExporterInput(jp);




        exporter.setExporterInput(exporterInput);
    //设置输出项
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputFilePath);
        exporter.setExporterOutput(exporterOutput);
        try {
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }


    }
}
