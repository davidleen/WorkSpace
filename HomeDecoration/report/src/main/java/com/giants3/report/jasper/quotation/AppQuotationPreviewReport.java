package com.giants3.report.jasper.quotation;

import com.giants3.hd.entity.Company;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.report.JRReporter;
import com.giants3.report.jasper.JRPreviewReporter;

import java.io.InputStream;

/**
 * Created by davidleen29 on 2018/8/4.
 */
public class AppQuotationPreviewReport extends  AppQuotationReport {
    public AppQuotationPreviewReport(  Company company, QuotationDetail quotationDetail,InputStream jrReportFileStream) {
        super(new JRPreviewReporter(), company, quotationDetail,jrReportFileStream);
    }
}
