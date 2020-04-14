package com.giants.hd.desktop.reports.excels.product_report;

import com.giants.hd.desktop.reports.excels.SimpleExcelReporter;
import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.ProductAgent;
import com.giants3.report.ResourceUrl;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.util.List;




/**
 * 产品报表批量导出5
 * Created by davidleen29 on 2016/8/7.
 */
public class ProductReport_Style_5 extends SimpleExcelReporter<List<Product>> {
    private static final String TEMPLATE_FILE_NAME = "excel/产品批量导出格式5.xls";
    private String destFileName;

    public ProductReport_Style_5(String destFileName) {


        this.destFileName = destFileName;
    }


    public static final int PAGE_ITEM_COUNT = 10;


    @Override
    protected void report(List<Product> data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(0);
        int startItemRow = 1;
        int defaultRowCount = 250;
        int dataSize = data.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);


        int rowIndex = startItemRow;
        for (int i = 0; i < dataSize; i++) {


            Product item = data.get(i);

            attachPicture(workbook, writableSheet, ResourceUrl.completeUrl(item.url), 0, rowIndex, 0, rowIndex );

            addString(writableSheet, ProductAgent.getFullName(item), 1, rowIndex);
            addString(writableSheet, item.pUnitName, 2, rowIndex);
            addString(writableSheet, "$" + item.fob, 3, rowIndex);
            addString(writableSheet, item.spec, 4, rowIndex);
            addString(writableSheet, ProductAgent.getProductFullPackageInfo(item), 5, rowIndex);
            addNumber(writableSheet, item.packVolume, 6, rowIndex);
            addNumber(writableSheet, item.weight, 7, rowIndex);

            rowIndex++;

        }


    }

    @Override
    protected String getTemplateFilePath() {
        return TEMPLATE_FILE_NAME;
    }

    @Override
    protected String getDestFileName(List<Product> data) {
        return destFileName;
    }


}
