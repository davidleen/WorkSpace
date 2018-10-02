package com.giants3.report.jasper;

import com.giants3.report.JRReporter;
import com.giants3.report.Reporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/8/4.
 */
public class JRPdfReporter  extends JRReporter {


    private String destFilePath;

    public JRPdfReporter( String destFilePath)
    {


        this.destFilePath = destFilePath;

    }

    @Override
    public void doOutput(JasperPrint jp) {
        try {
            JasperExportManager.exportReportToPdfFile(jp,destFilePath);

        } catch (JRException e) {
            e.printStackTrace();
        }

    }


}
