package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.noEntity.QuotationDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 通用报价模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_NORMAL extends ExcelReportor {


    Workbook workbook;

    public Report_Excel_NORMAL(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException {


        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream = url.openStream();
        workbook = new HSSFWorkbook(inputStream);


        writeOnExcel(workbook.getSheetAt(0), quotationDetail);


        FileOutputStream fos = new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();


    }


    protected void writeOnExcel(Sheet writableSheet, QuotationDetail quotationDetail) throws IOException {


        int defaultRowCount = 7;
        int startItemRow = 9;

        int dataSize = quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);


        Quotation quotation = quotationDetail.quotation;
        //表头
        //注入报价单号
        addString(writableSheet, quotation.qNumber, 2, 1);


        //报价日期
        addString(writableSheet, quotation.qDate, 14, 1);


        //TO
        addString(writableSheet, quotation.customerName, 2, 7);


        //业务员代码
        addString(writableSheet, quotation.salesman, 11, 7);


        for (int i = 0; i < dataSize; i++) {


            int rowUpdate = startItemRow + i;
            QuotationItem item = quotationDetail.items.get(i);

            //图片

            if (isExportPicture())
                exportPicture(item.photoUrl, item.getFullProductName());


            attachPicture(workbook, writableSheet, HttpUrl.loadProductPicture(item.photoUrl), 0, rowUpdate, 0, rowUpdate);


            //读取咸康数据


            //货号
            addString(writableSheet, item.productName.trim(), 2, rowUpdate);


            //货号
            addString(writableSheet, item.pVersion, 3, rowUpdate);


            //材料比重
            addString(writableSheet, item.constitute.trim(), 5, rowUpdate);


            //单位
            int lastIndex = item.unit.lastIndexOf("/");
            addString(writableSheet, lastIndex == -1 ? "1" : item.unit.substring(lastIndex + 1), 7, rowUpdate);

            //FOb
            addNumber(writableSheet, item.price, 8, rowUpdate);

            //包装尺寸
            //内盒数
            addNumber(writableSheet, item.inBoxCount, 9, rowUpdate);


            //包装数
            addNumber(writableSheet, item.packQuantity, 10, rowUpdate);


            //解析出长宽高

            float[] result = StringUtils.decouplePackageString(item.packageSize);


            //包装长
            addNumber(writableSheet, result[0], 12, rowUpdate);

            //包装宽
            addNumber(writableSheet, result[1], 14, rowUpdate);


            //包装高
            addNumber(writableSheet, result[2], 16, rowUpdate);


            //包装体积
            addNumber(writableSheet, item.volumeSize, 17, rowUpdate);


            //是否开模
            addString(writableSheet, item.kd?"Y":"N", 18, rowUpdate);


            //产品规格
            addString(writableSheet, item.spec.trim(), 19, rowUpdate);


            //镜面尺寸
            addString(writableSheet, item.mirrorSize.trim(), 21, rowUpdate);


            //净重
            addNumber(writableSheet, item.weight, 22, rowUpdate);


            //备注
            addString(writableSheet, item.memo.trim(), 24, rowUpdate);


        }


    }
}
