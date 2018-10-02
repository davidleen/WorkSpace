package com.giants3.report.jasper.quotation;

import com.giants3.hd.entity.Company;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.report.JRReporter;
import com.giants3.report.jasper.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/2/17.
 */
public   class AppQuotationReport extends JRReport {


    private final Company company;
    private QuotationDetail quotationDetail;
    private InputStream jrReportFileStream;

    /**
     *
     * @param reporter
     * @param company
     * @param quotationDetail
     * @param jrReportFileStream 報表模板文件输入流
     */
    public AppQuotationReport(JRReporter reporter,Company company, QuotationDetail quotationDetail,InputStream jrReportFileStream) {


        super(reporter);
        this.company = company;
        this.quotationDetail = quotationDetail;
        this.jrReportFileStream = jrReportFileStream;
    }



    @Override
    public JRDataSource getDataSource() {

        List<QuotationItem> quotationItems = quotationDetail.items;
        return new CustomBeanDataSource(quotationItems);

    }

    @Override
    public InputStream getReportFile() {



          return
                  jrReportFileStream;

    }

    @Override
    public Map<String, Object> getParameters() {
        return new CompanyReportData(company,quotationDetail.quotation, Quotation.class);
    }


}
