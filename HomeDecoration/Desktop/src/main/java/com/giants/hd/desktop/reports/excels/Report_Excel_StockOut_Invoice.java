package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 导出出库发票单
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockOut_Invoice extends AbstractExcelReporter<ErpStockOutDetail> {
    private static final String TEMPLATE_FILE_NAME = "excel/INVOICE-YUNFEI-RM.xls";
    int styleRow = 10;

    Workbook workbook;

    public Report_Excel_StockOut_Invoice() {


    }


    @Override
    public void report(ErpStockOutDetail data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME);
        String fileName = fileOutputDirectory + File.separator + data.erpStockOut.ck_no + "-INVOICE-YUNFEI-RM.xls";
        //Create Workbook instance holding reference to .xlsx file
        workbook = new HSSFWorkbook(inputStream);
        writeOnExcel(data, workbook);
        FileOutputStream fos = new FileOutputStream(fileName);

        workbook.write(fos);
        workbook.close();


        fos.close();


    }

    /**
     * 报表
     *
     * @param data
     * @param workbook
     */
    private void writeOnExcel(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(0);

        //出库单号
        addString(writableSheet, "Invoice No:" + data.erpStockOut.ck_no, 6, 4);
        //日期
        addString(writableSheet, "Date:" + data.erpStockOut.ck_dd, 6, 7);
        // 提单号
        addString(writableSheet, "提单号:" + data.erpStockOut.tdh, 4, 8);
        //客户
        addString(writableSheet, data.erpStockOut.cus_no, 1, 4);

        //客户信息

        addString(writableSheet, data.erpStockOut.adr2, 0, 5);
        addString(writableSheet, data.erpStockOut.tel1, 0, 6);
        addString(writableSheet, data.erpStockOut.fax, 0, 7);


        //目的港
        String mdgText = "FROM FUZHOU TO %s BY SEA";
        addString(writableSheet, String.format(mdgText, data.erpStockOut.mdg), 0, 10);


        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 14;
//
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);
        int row = 0;
        for (int i = 0; i < dataSize; i++) {
            row = i + startItemRow;
            ErpStockOutItem outItem = items.get(i);
            addString(writableSheet, outItem.bat_no, 0, row);
            addString(writableSheet, outItem.describe, 1, row);

            //包装
            addString(writableSheet, outItem.khxg, 3, row);
            addString(writableSheet, outItem.unit, 4, row);

            addNumber(writableSheet, outItem.stockOutQty, 5, row);
            addNumber(writableSheet, outItem.up, 6, row);
            float amt = FloatHelper.scale(outItem.stockOutQty * outItem.up
            );
            addNumber(writableSheet, amt, 7, row);
            //PO
            addString(writableSheet, outItem.cus_os_no, 8, row);

//            //材料长宽高
//            addNumber(writableSheet, outItem.pLong, 4, row);
//            addNumber(writableSheet, outItem.pWidth, 5, row);
//            addNumber(writableSheet, outItem.pHeight, 6, row);
//            //材料长宽高
//            addNumber(writableSheet, outItem.wLong, 7, row);
//            addNumber(writableSheet, outItem.wWidth, 8, row);
//            addNumber(writableSheet, outItem.wHeight, 9, row);
//
//            //定额
//            addNumber(writableSheet, outItem.quota, 10, row);
//            //单位
//            addString(writableSheet, outItem.unitName, 11, row);
//            //利用率
//            addNumber(writableSheet, outItem.available, 12, row);
//
//            //类型
//            addNumber(writableSheet, outItem.type, 13, row);
//            //单价
//            addNumber(writableSheet, outItem.price, 14, row);
//            //金额
//            addNumber(writableSheet, outItem.amount, 15, row);
//
//            //备注
//            addString(writableSheet, outItem.memo, 16, row);
        }


        row++;
        //添加上汇总行
        int totalQty = 0;
        float totalAmt = 0;
        for (int i = 0; i < dataSize; i++) {

            totalQty += items.get(i).stockOutQty;
            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up
            );
            ;
        }
        addNumber(writableSheet, totalQty, 5, row);
        addNumber(writableSheet, totalAmt, 7, row);


    }
}
