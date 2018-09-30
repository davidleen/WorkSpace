package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
import com.giants3.hd.exception.HdException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 入仓记录报表
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockXiaoku extends AbstractExcelReporter<RemoteData<StockSubmit>> {

    public static final String FILE_NAME = "出货报表";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";
    int styleRow = 10;

    Workbook workbook;
    private StockXiaoku xiaoku;

    List<StockSubmit> xiaokuItems;


    public Report_Excel_StockXiaoku(StockXiaoku xiaoku, List<StockSubmit> xiaokuItems) {


        this.xiaoku = xiaoku;
        this.xiaokuItems = xiaokuItems;
    }


    public void report(String fileOutputDirectory) throws IOException, HdException {


        String cus_no = xiaokuItems.get(0).cus_no;

        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME);
        String fileName = fileOutputDirectory + File.separator + xiaoku.xhdg + "柜" + cus_no + FILE_NAME + ".xls";
        //Create Workbook instance holding reference to .xlsx file
        workbook = new HSSFWorkbook(inputStream);
        writeOnExcel(workbook);
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        workbook.close();


        fos.close();


    }


    /**
     * 报表
     *
     * @param workbook
     */
    private void writeOnExcel(Workbook workbook) {


        String cus_no = xiaokuItems.get(0).cus_no;
        String dd = xiaokuItems.get(0).dd;

        Sheet writableSheet = workbook.getSheetAt(0);

        addNumber(writableSheet, xiaoku.xhdg, 1, 1);
        addString(writableSheet, xiaoku.ps_no, 5, 1);
        addString(writableSheet, xiaoku.xhgh, 1, 2);
        addString(writableSheet, xiaoku.xhfq, 4, 2);
        addString(writableSheet, cus_no, 8, 2);
        addString(writableSheet, dd, 12, 2);
        addString(writableSheet, xiaoku.xhph, 1, 3);
        addString(writableSheet, xiaoku.xhgx, 4, 3);
        addString(writableSheet, xiaoku.tcgs, 8, 3);

        List<StockSubmit> items = xiaokuItems;
        int dataSize = items.size();
        int defaultRowCount = 3;
        int startItemRow = 6;
//
//实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);
        int row = 0;
        int allXs = 0;
        float allTj = 0;
        float allCost = 0;
        for (int i = 0; i < dataSize; i++) {
            row = i + startItemRow;
            StockSubmit outItem = items.get(i);


            addString(writableSheet, outItem.so_no, 0, row);
            addString(writableSheet, outItem.bat_no, 1, row);
            addString(writableSheet, outItem.prd_name, 2, row);
            addString(writableSheet, outItem.cus_os_no, 3, row);
            addNumber(writableSheet, outItem.xs, 4, row);

            allXs+=outItem.xs;


            addNumber(writableSheet, outItem.so_zxs, 5, row);
            addNumber(writableSheet, outItem.qty, 6, row);


            float[] khxg = StringUtils.decouplePackageString(outItem.khxg);
            if (khxg.length >= 0)
                addNumber(writableSheet, khxg[0], 7, row);
            if (khxg.length >= 1)
                addNumber(writableSheet, khxg[1], 8, row);
            if (khxg.length >= 2)
                addNumber(writableSheet, khxg[2], 9, row);

            addNumber(writableSheet, outItem.zxgtj, 10, row);
            allTj += outItem.zxgtj;

            addString(writableSheet, outItem.area, 11, row);
            addNumber(writableSheet, outItem.price, 12, row);


            addNumber(writableSheet, outItem.cost, 13, row);
            allCost += outItem.cost;

            addString(writableSheet, outItem.dept, 14, row);










        }

        row++;
        row++;

        addString(writableSheet, "总计", 0, row);
        addNumber(writableSheet, allXs, 4, row);
        addNumber(writableSheet, allTj, 10, row);
        addNumber(writableSheet, allCost, 13, row);


    }


}
