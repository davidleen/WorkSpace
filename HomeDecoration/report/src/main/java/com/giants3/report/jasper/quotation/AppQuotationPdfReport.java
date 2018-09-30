package com.giants3.report.jasper.quotation;

import com.giants3.hd.entity.Company;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.report.JRReporter;
import com.giants3.report.jasper.JRPdfReporter;

import java.io.InputStream;

/**
 * Created by davidleen29 on 2018/8/4.
 */
public class AppQuotationPdfReport extends  AppQuotationReport {
    public AppQuotationPdfReport(Company company, QuotationDetail quotationDetail, InputStream  jrInputStream, String destFilePath) {
        super(new JRPdfReporter(destFilePath), company, quotationDetail,jrInputStream);
    }
}
