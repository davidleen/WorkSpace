package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.StockSubmit;
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
public class Report_Excel_StockInAndSubmitList extends AbstractExcelReporter<RemoteData<StockSubmit>> {

    public static final String FILE_NAME = "入仓记录";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";
    int styleRow = 10;

    Workbook workbook;

    public String startDate;
    public String endDate;

    public Report_Excel_StockInAndSubmitList(String startDate, String endDate) {


        this.startDate = startDate;
        this.endDate = endDate;

    }


    @Override
    public void report(RemoteData<StockSubmit> data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME);

        String fileName = fileOutputDirectory + File.separator + (startDate.equalsIgnoreCase(endDate) ? startDate : (startDate + "到" + endDate)) + FILE_NAME + ".xls";
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
    private void writeOnExcel(RemoteData<StockSubmit> data, Workbook workbook) {

        if (!data.isSuccess()) return;


        Sheet writableSheet = workbook.getSheetAt(0);
        addString(writableSheet, startDate.substring(0, 4), 0, 1);

        List<StockSubmit> items = data.datas;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 4;
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
            addString(writableSheet, outItem.dd.substring(5), 0, row);
            addString(writableSheet, outItem.cus_no, 1, row);
            addString(writableSheet, outItem.so_no, 2, row);
            //客号
            addString(writableSheet, outItem.prd_name + "/" + outItem.bat_no, 3, row);
            addString(writableSheet, outItem.no, 4, row);
            //数量
            addNumber(writableSheet, outItem.xs, 5, row);
            allXs += outItem.xs;
            addNumber(writableSheet, outItem.so_zxs, 6, row);
            addNumber(writableSheet, outItem.qty, 7, row);


            float[] khxg = StringUtils.decouplePackageString(outItem.khxg);
            if (khxg.length >= 0)
                addNumber(writableSheet, khxg[0], 8, row);
            if (khxg.length >= 1)
                addNumber(writableSheet, khxg[1], 9, row);
            if (khxg.length >= 2)
                addNumber(writableSheet, khxg[2], 10, row);


            addNumber(writableSheet, outItem.zxgtj, 11, row);
            allTj += outItem.zxgtj;
            addString(writableSheet, outItem.area, 12, row);
            addNumber(writableSheet, outItem.price, 13, row);
            addNumber(writableSheet, outItem.cost, 14, row);
            allCost += outItem.cost;
            addString(writableSheet, outItem.dept, 15, row);


        }

        row++;

        addString(writableSheet, "总计", 0, row);
        addNumber(writableSheet, allXs, 5, row);
        addNumber(writableSheet, allTj, 11, row);
        addNumber(writableSheet, allCost, 14, row);


    }


}
