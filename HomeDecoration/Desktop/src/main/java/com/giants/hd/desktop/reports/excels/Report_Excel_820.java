package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.utils.AccumulateMap;
import com.giants3.hd.domain.api.HttpUrl;
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
public class Report_Excel_820 extends ExcelReportor {


    public Report_Excel_820(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {


        InputStream inputStream = url.openStream();
        Workbook workbook = new HSSFWorkbook(inputStream);


        writeOnExcel(quotationDetail, workbook);


        FileOutputStream fos = new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();


    }


    protected void writeOnExcel(QuotationDetail quotationDetail, Workbook writableWorkbook) throws IOException, HdException {


        int size = quotationDetail.items.size();

        AccumulateMap names = new AccumulateMap();


        Quotation quotation = quotationDetail.quotation;


        for (int i = 0; i < size; i++) {
            QuotationItem item = quotationDetail.items.get(i);

            names.accumulate(item.productName);


            int duplicateCount = names.get(item.productName).intValue();


            Sheet fromSheet = writableWorkbook.getSheetAt(0);

            Sheet writableSheet = writableWorkbook.createSheet(item.productName + (duplicateCount > 1 ? ("_" + (duplicateCount - 1)) : ""));
            POIUtils.copySheet(writableWorkbook, fromSheet, writableSheet, true);


//            //移除照片
//
//            for(int imageIndex=0,count=writableSheet.getNumberOfImages();imageIndex<count;imageIndex++)
//            {
//                WritableImage writableImage=null;
//                try {
//                      writableImage = writableSheet.getImage(imageIndex);
//                }catch (Throwable t)
//                {
//
//                }
//                if(writableImage!=null)
//                   writableSheet.removeImage(writableImage);
//            }


//            Label       label;
//
//            jxl.jxl.biff.biff.Number num ;
//            WritableImage image;
            // 插入日期

            //报价日期
            addString(writableSheet, quotation.qDate, 10, 0);

            //货号
            addString(writableSheet, item.productName, 6, 11);


            //主要成分
            addString(writableSheet, item.constitute, 6, 12);


            //尺寸   英寸
            addString(writableSheet, item.spec, 6, 15);


            //包装尺寸   英寸
            addString(writableSheet, item.packageSize, 6, 16);

            //每箱数目
            int unitSize = 1;
            try {
                unitSize = Integer.valueOf(item.unit.substring(item.unit.lastIndexOf("/")));
            } catch (Throwable t) {

            }

            addNumber(writableSheet, unitSize, 6, 19);

            //cbm 体积

            addNumber(writableSheet, item.volumeSize, 6, 20);


            //fob
            float fob = item.price;
            addNumber(writableSheet, fob, 9, 18);
            addNumber(writableSheet, fob, 10, 18);


            if (isExportPicture())


                exportPicture(item.photoUrl, item.getFullProductName());


            attachPicture(writableWorkbook, writableSheet, HttpUrl.loadProductPicture(item.photoUrl), 0, 10, 4, 23);


            //覆盖样品数据
            addString(writableSheet, "", 5, 25);
            addString(writableSheet, "", 13, 18);
            addString(writableSheet, "", 13, 19);


        }


    }

}
