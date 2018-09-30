package com.giants3.report.jasper;


import com.giants3.report.Reportable;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.Map;

/**
 * jasper 报表基类
 *
 * Created by davidleen29 on 2017/3/16.
 */
public  abstract  class JRReport implements Reportable {




    @Override
    public   void report()
    {
          InputStream inputStream=null;
        try {
            inputStream = getReportFile();
            JasperReport jr= JasperCompileManager.compileReport(inputStream);


            JasperPrint jp= JasperFillManager.fillReport(jr,getParameters(),getDataSource());

            reportJRType(  jp);

            //inputStream.close();
        } catch (JRException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            }catch (Throwable t)
            {}

        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    protected abstract void reportJRType(JasperPrint jp);


    public  abstract  JRDataSource getDataSource();
    public  abstract InputStream getReportFile();
    public  abstract Map<String,Object> getParameters();
}
