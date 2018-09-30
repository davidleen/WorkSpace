package com.giants3.hd.server.service;

import com.giants3.hd.entity.Company;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.server.app.service.AppQuotationService;
import com.giants3.hd.server.repository.CompanyRepository;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.report.ResourceUrl;
import com.giants3.report.jasper.quotation.AppQuotationPdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

//import com.giants3.report.jasper.quotation.AppQuotationReport;

/**
 * Created by davidleen29 on 2018/2/17.
 */
@Service
public class ReportService extends AbstractService {


    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;
    @Autowired
    private AppQuotationService quotationService;
    @Autowired
    private CompanyRepository companyRepository;


    /**
     * 打印报价单pdf报表， 返回报表所在路径
     *
     * @param quotationId
     * @return
     */
    public String printAppQuotationToPdf(long quotationId) {
        QuotationDetail copy=null;
        {
            QuotationDetail quotationDetail = quotationService.getDetail(quotationId);
            copy= GsonUtils.fromJson(GsonUtils.toJson(quotationDetail),QuotationDetail.class);
        }

            String destFilePath = tempFilePath + "report/temp" + Calendar.getInstance().getTimeInMillis() + ".pdf";
            String path = null;
            try {
                path = new ClassPathResource("report/jasper/appQuotation.jrxml").getFile().getPath();
            } catch (IOException e) {
                e.printStackTrace();
            }



        com.giants3.hd.utils.FileUtils.makeDirs(destFilePath);


        for (QuotationItem item : copy.items) {
            item.thumbnail = ResourceUrl.completeUrl(item.thumbnail);
        }


        Company company = companyRepository.findAll().get(0);
        final FileInputStream jrInputStream;
        try {
            jrInputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return "";
        }

        long time=Calendar.getInstance().getTimeInMillis();
        logger.info("time user in parse pdf："+(Calendar.getInstance().getTimeInMillis()-time));
        time=Calendar.getInstance().getTimeInMillis();
        new AppQuotationPdfReport(company, copy, jrInputStream, destFilePath).report();


        logger.info("time user in parse pdf："+(Calendar.getInstance().getTimeInMillis()-time));

        return destFilePath;


    }


}
