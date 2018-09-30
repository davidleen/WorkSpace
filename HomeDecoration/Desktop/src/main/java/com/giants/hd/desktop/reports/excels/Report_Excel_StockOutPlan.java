package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.OrderReportItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**导出出货计划报表
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockOutPlan extends  AbstractExcelReporter<List<OrderReportItem>>{
    private static final String TEMPLATE_FILE_NAME = "excel/出货单格式.xls";


    Workbook workbook;
    public Report_Excel_StockOutPlan()
    {




    }


    @Override
    public void report(List<OrderReportItem> data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream=	getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME) ;
        String fileName=fileOutputDirectory+ File.separator+"出货单"+ DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS_LOG.format( Calendar.getInstance().getTime())+".xls";
        //Create Workbook instance holding reference to .xlsx file
        workbook = new HSSFWorkbook(inputStream);
        writeOnExcel(data ,workbook  );
        FileOutputStream fos=    new FileOutputStream(fileName);

        workbook.write(fos);
        workbook.close();


        fos.close();





    }

    /**
     *报表
     * @param items
     * @param workbook
     */
    private void writeOnExcel(List<OrderReportItem> items, Workbook workbook) {




        Sheet writableSheet= workbook.getSheetAt(0);

        int dataSize=items.size();
        int defaultRowCount=30;
        int startItemRow=2;
//
        //实际数据超出范围 插入空行
        duplicateRow(workbook,writableSheet,startItemRow,defaultRowCount,dataSize);
        int row=0;
        for (int i=0;i<dataSize;i++) {
              row=i+startItemRow;
              OrderReportItem outItem=items.get(i);
            addString(writableSheet, outItem.cus_no, 0, row);
            addString(writableSheet, outItem.os_no, 1, row);


            addString(writableSheet, outItem.prd_no, 2, row);
            //图片

            attachPicture(workbook,writableSheet, HttpUrl.loadPicture(outItem.url),3 , row ,3 , row );
            addString(writableSheet, outItem.cus_prd_no, 4, row);
            addString(writableSheet, outItem.unit, 5, row);
            addNumber(writableSheet, outItem.qty, 6, row);
            addString(writableSheet, outItem.verifyDate, 7, row);
            addString(writableSheet, outItem.sendDate, 8, row);
        }




    }
}
