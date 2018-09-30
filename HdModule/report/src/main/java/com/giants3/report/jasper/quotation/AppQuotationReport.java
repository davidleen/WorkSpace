package com.giants3.report.jasper.quotation;

import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.report.jasper.CustomBeanDataSource;
import com.giants3.report.jasper.JRPdfReport;
import com.giants3.report.jasper.ReportData;
import net.sf.jasperreports.engine.JRDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/2/17.
 */
public class AppQuotationReport extends JRPdfReport {




    private QuotationDetail quotationDetail;
    private String jrFilePath;

    public AppQuotationReport(QuotationDetail quotationDetail,String jrFilePath,String destFilePath) {

        super(destFilePath);
        this.quotationDetail = quotationDetail;
        this.jrFilePath = jrFilePath;
    }

    @Override
    public JRDataSource getDataSource() {

        List<QuotationItem> quotationItems = quotationDetail.items;
        return new CustomBeanDataSource(quotationItems);

    }

    @Override
    public InputStream getReportFile() {


        try {
            return new FileInputStream(jrFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public Map<String, Object> getParameters() {
        return new ReportData(quotationDetail.quotation, QuotationItem.class);
    }
}
