package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Quotation;
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
public class Report_Excel_302 extends ExcelReportor {


    Workbook workbook;

    public Report_Excel_302(QuotationFile modelName) throws HdException {
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


        int defaultRowCount = 1;
        int startItemRow = 30;

        int dataSize = quotationDetail.items.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);


        Quotation quotation = quotationDetail.quotation;
        //报价日期
        addString(writableSheet, quotation.qDate, 3, 8);

        float pictureGap = 0.1f;
        for (int i = 0; i < dataSize; i++) {
            int rowUpdate = startItemRow + i;
            QuotationItem item = quotationDetail.items.get(i);


            //图片

            if (isExportPicture())

                exportPicture(item.photoUrl, item.getFullProductName());
            attachPicture(workbook, writableSheet, HttpUrl.loadProductPicture(item.photoUrl), 6, rowUpdate, 6, rowUpdate);


            //序号
            addNumber(writableSheet, i + 1, 0, rowUpdate);


            //货号
            addString(writableSheet, item.productName.trim(), 3, rowUpdate);


            //   单位
            addString(writableSheet, item.unit, 5, rowUpdate);


            //   产品描述
            addString(writableSheet, item.memo, 9, rowUpdate);


            //产品尺寸

            String[] specValue = groupSpec(StringUtils.decoupleSpecString(item.spec));
            addString(writableSheet, specValue[0], 10, rowUpdate);


            addString(writableSheet, specValue[1], 11, rowUpdate);

            addString(writableSheet, specValue[2], 12, rowUpdate);


            //   重量
            addNumber(writableSheet, item.weight, 13, rowUpdate);


            //   材质百分比
            addString(writableSheet, item.constitute, 17, rowUpdate);


            //外箱尺寸
            float[] packValue = StringUtils.decouplePackageString(item.packageSize);


            addNumber(writableSheet, packValue[0], 40, rowUpdate);

            addNumber(writableSheet, packValue[1], 41, rowUpdate);

            addNumber(writableSheet, packValue[2], 42, rowUpdate);


            //单价 fob
            addNumber(writableSheet, item.price, 48, rowUpdate);


        }


    }

}
