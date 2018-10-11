package com.giants3.report.jasper;

import com.giants3.report.JRReporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

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
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setWhitePageBackground(true);
        configuration.setDetectCellType(true);
        configuration.setFontSizeFixEnabled(true);
//        //去除两行之前的空白
//        XlsxReportConfiguration xlsxReportConfiguration=new SimpleXlsxReportConfiguration();
//        xlsxReportConfiguration.set
//        exporter.setConfiguration(   );
        //去除两行之前的空白
//        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
//        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
//        //设置Excel表格的背景颜色为默认的白色
//        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);

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
