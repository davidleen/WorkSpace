package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_314 extends ExcelReportor {
    Workbook workbook;

    public Report_Excel_314(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {


        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream = url.openStream();
        workbook = new HSSFWorkbook(inputStream);


        writeOnExcel(quotationDetail, workbook.getSheetAt(0));


        FileOutputStream fos = new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();


    }

    protected void writeOnExcel(QuotationDetail quotationDetail, Sheet writableSheet) throws IOException, HdException {


        int defaultRowCount = 6;
        int startItemRow = 1;

        int dataSize = quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);

        float pictureGap = 0.1f;
        for (int i = 0; i < dataSize; i++) {
            int rowUpdate = startItemRow + i;
            QuotationItem item = quotationDetail.items.get(i);


            if (isExportPicture())

                exportPicture(item.photoUrl, item.getFullProductName());
            attachPicture(workbook, writableSheet, HttpUrl.loadProductPicture(item.photoUrl), 4, rowUpdate, 4, rowUpdate);


            addNumber(writableSheet, i + 1, 0, rowUpdate);

            //设计号  版本号
            // addString(writableSheet, item.productName.trim(),2,rowUpdate);

            //货号
            addString(writableSheet, item.productName.trim(), 3, rowUpdate);


            //产品总尺寸  材质百分比
            addString(writableSheet, item.spec + "\n" + item.constitute, 5, rowUpdate);

            //单价
            addNumber(writableSheet, item.price, 6, rowUpdate);

            //产品重量
            addNumber(writableSheet, item.weight, 8, rowUpdate);

            //装箱数目
            addNumber(writableSheet, item.packQuantity, 9, rowUpdate);

            //外箱尺寸
            addString(writableSheet, item.packageSize.trim(), 10, rowUpdate);


        }


    }

}
