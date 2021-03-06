package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.entity.Company;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.report.ResourceUrl;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

/**
 * 9307客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_App_Quotation_XLXS_1 extends SimpleExcelReporter<QuotationDetail> {

    public static final String FILE_NAME = "广交会报价单_1";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";

    XSSFWorkbook workbook;

    public static final int PAGE_ITEM_COUNT = 10;
    private Company company;
    private QuotationDetail quotationDetail;
    private String destFileName;
    private boolean exportPicture;
    PictureFileExporter fileExporter;

    @Override
    protected void doSomethingOnOutputDirectory(String outputDirectory) {

        if(exportPicture)
          fileExporter=new PictureFileExporter(outputDirectory+ File.separator+destFileName.substring(0,destFileName.indexOf(".")));
    }



    public Report_App_Quotation_XLXS_1(Company company,QuotationDetail quotationDetail, String destFileName,boolean exportPicture) {
        this.company = company;


        this.quotationDetail = quotationDetail;
        this.destFileName = destFileName;

        this.exportPicture = exportPicture;
    }



    @Override
    protected void report(QuotationDetail data, Workbook workbook) {
        Sheet writableSheet = workbook.getSheetAt(0);
        int startItemRow = 15;
        int defaultRowCount = 250;
        int dataSize = data.items.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);


        addString(writableSheet, company.tel,2,3);
        addString(writableSheet, company.fax,10,3);


        final Quotation quotation = quotationDetail.quotation;
        addString(writableSheet, quotation.email,17,3);
        addString(writableSheet, quotation.booth,17,4);
        addString(writableSheet, quotation.qNumber,17,7);
        addString(writableSheet, quotation.qDate,17,9);




        addString(writableSheet, quotation.customerName,2,7);
        addString(writableSheet, quotation.customerAddress,2,9);

        int rowIndex=startItemRow;
        for (int i = 0; i < dataSize; i++) {



            QuotationItem item=quotationDetail.items.get(i);

            attachPicture(workbook,writableSheet, ResourceUrl.completeUrl(item.photoUrl),1,rowIndex,1,rowIndex);

            addString(writableSheet,item.productName,2,rowIndex);
            addString(writableSheet,item.unit,4,rowIndex);
//            addString(writableSheet,item.spec,5,rowIndex);
            String[] specValue = groupSpec(StringUtils.decoupleSpecString(item.spec));
            addString(writableSheet, specValue[0], 5, rowIndex);
            addString(writableSheet, "*", 6, rowIndex);


            addString(writableSheet, specValue[1], 7, rowIndex);
            addString(writableSheet, "*", 8, rowIndex);

            addString(writableSheet, specValue[2], 9, rowIndex);


            addString(writableSheet,"$"+item.price,10,rowIndex);

            addNumber(writableSheet, FloatHelper.scale(item.volumeSize,2) ,12,rowIndex);
            addNumber(writableSheet, FloatHelper.scale(item.weight,2) ,14,rowIndex);


            int index=16;
            addNumber(writableSheet,item.inBoxCount,index++,rowIndex);
            addString(writableSheet,"/",index++,rowIndex);
            addNumber(writableSheet,item.packQuantity,index++,rowIndex);
            addString(writableSheet,"/",index++,rowIndex);


            addNumber(writableSheet, item.boxLong,index++,rowIndex);
            addString(writableSheet,"*",index++,rowIndex);
            addNumber(writableSheet,item.boxWidth,index++,rowIndex);
            addString(writableSheet,"*",index++,rowIndex);
            addNumber(writableSheet,item.boxHeight,index++,rowIndex);

            addString(writableSheet, item.memo ,index++ ,rowIndex);


            if(fileExporter!=null)
            {
                fileExporter.exportFile(item.photoUrl,item.productName+ (StringUtils.isEmpty(item.pVersion)?"":("-"+item.pVersion)));
            }






            rowIndex+=1;

        }






    }

    @Override
    protected String getTemplateFilePath() {
        return TEMPLATE_FILE_NAME;
    }

    @Override
    protected String getDestFileName(QuotationDetail data) {
        return destFileName;
    }


}
