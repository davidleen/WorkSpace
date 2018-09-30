package com.giants.hd.desktop.reports;

import com.giants.hd.desktop.reports.excels.ExcelReportor;
import com.giants3.hd.exception.HdException;
import com.google.inject.Singleton;

/**
 * Created by davidleen29 on 2015/8/6.
 */

@Singleton
public class ReportFactory {


    public ExcelReportor createExcelReportor(QuotationFile file) throws HdException {



        ExcelReportor reportor=null;

            try {
                reportor = (ExcelReportor) file.aClass.getConstructor(QuotationFile.class).newInstance(file);
            }catch (Throwable t)
            {
                t.printStackTrace();
            }

        if(reportor==null)
        {

            throw   HdException.create("未定义相应导出处理与模板" + file.name + "相关联");
        }
        return reportor;
    }
}
