package com.giants3.report.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Created by davidleen29 on 2018/2/17.
 */
public abstract class JRPdfReport extends  JRReport {


    private String destFilePath;

    public JRPdfReport(String destFilePath)
    {

        this.destFilePath = destFilePath;
    }
    @Deprecated
    protected void reportJRType(JasperPrint jp)
    {


        try {
            JasperExportManager.exportReportToPdfFile(jp,destFilePath);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

}
