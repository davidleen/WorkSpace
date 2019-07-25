package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.utils.StringUtils;
import com.google.inject.Guice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 314_KKL 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_314_KKL_XLS extends ExcelReportor {
    Workbook workbook;

    public Report_Excel_314_KKL_XLS(QuotationFile modelName) {
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


        int defaultRowCount = 92;
        int startItemRow = 7;

        int dataSize = quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);
        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap = 0.1f;
        int columnIndex;
        for (int i = 0; i < dataSize; i++) {
            int rowUpdate = startItemRow + i;
            QuotationItem item = quotationDetail.items.get(i);

             ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);
            if (isExportPicture())

                exportPicture(item.photoUrl, item.getFullProductName());


            columnIndex=4;
            //货号
            addString(writableSheet, item.productName.trim(),columnIndex, rowUpdate); //货号
            columnIndex++;
            addString(writableSheet, item.memo.trim(),columnIndex, rowUpdate);//备注
            columnIndex++;
            addString(writableSheet, productDetail.product.constitute,columnIndex, rowUpdate);

            columnIndex+=4;
            attachPicture(workbook, writableSheet, HttpUrl.loadProductPicture(item.photoUrl), columnIndex, rowUpdate, columnIndex, rowUpdate);

           columnIndex+=6;
            addNumber(writableSheet, item.price, columnIndex, rowUpdate);

            columnIndex+=5;
            addNumber(writableSheet, item.inBoxCount, columnIndex, rowUpdate);
            columnIndex+=1;
            addNumber(writableSheet, item.packQuantity, columnIndex, rowUpdate);




            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //包装尺寸 cm
            columnIndex+=2;
            addNumber(writableSheet, packValue[0], columnIndex, rowUpdate);
            columnIndex+=2;
            addNumber(writableSheet, packValue[1], columnIndex, rowUpdate);
            columnIndex+=2;
            addNumber(writableSheet, packValue[2], columnIndex, rowUpdate);


            columnIndex+=1;
            addNumber(writableSheet, item.volumeSize, columnIndex, rowUpdate);

            columnIndex+=2;
            addNumber(writableSheet, item.weight, columnIndex, rowUpdate);
            columnIndex+=2;
            addNumber(writableSheet, item.weight, columnIndex, rowUpdate);
//
//
//            //设计号  版本号
//            // addString(writableSheet, item.productName.trim(),2,rowUpdate);
//
//            //货号
//            addString(writableSheet, item.productName.trim(), 3, rowUpdate);
//
//
//            //产品总尺寸  材质百分比
//            addString(writableSheet, item.spec + "\n" + item.constitute, 5, rowUpdate);
//
//            //单价
//            addNumber(writableSheet, item.price, 6, rowUpdate);
//
//            //产品重量
//            addNumber(writableSheet, item.weight, 8, rowUpdate);
//
//            //装箱数目
//            addNumber(writableSheet, item.packQuantity, 9, rowUpdate);
//
//            //外箱尺寸
//            addString(writableSheet, item.packageSize.trim(), 10, rowUpdate);


        }


    }

}
