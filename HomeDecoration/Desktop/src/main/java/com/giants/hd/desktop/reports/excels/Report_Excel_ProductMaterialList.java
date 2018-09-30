package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ProductDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.List;

/** 产品材料清单导出
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_ProductMaterialList extends AbstractExcelReporter<ProductDetail> {
    private static final String TEMPLATE_FILE_NAME = "excel/材料导出清单.xls";
    int styleRow=10;

    Workbook workbook;

    public Report_Excel_ProductMaterialList( )   {
        super( );


    }


    @Override
    public void report(ProductDetail data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream=	getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME) ;
        String fileName=fileOutputDirectory+ File.separator+data.product.name+(StringUtils.isEmpty(data.product.pVersion)?"":("_"+data.product.pVersion))+"材料清单.xls";
        //Create Workbook instance holding reference to .xlsx file
        workbook = new HSSFWorkbook(inputStream);




        writeOnExcel(data ,workbook  );


        FileOutputStream fos=    new FileOutputStream(fileName);

        workbook.write(fos);
        workbook.close();


        fos.close();




    }

    private void writeOnExcel(ProductDetail data, Workbook workBook  ) {



        writeProductMaterial(data.conceptusMaterials,workBook.getSheetAt(0));
        writeProductMaterial(data.assembleMaterials,workBook.getSheetAt(1));
        writeProductMaterial(data.packMaterials,workBook.getSheetAt(2));
    }


    /**
     *
     * @param productMaterials
     * @param writableSheet
     */
    private  void writeProductMaterial(List<ProductMaterial> productMaterials, Sheet  writableSheet)
    {
        int defaultRowCount=30;
        int startItemRow=2;
        int dataSize=productMaterials.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook,writableSheet,startItemRow,defaultRowCount,dataSize);

        for (int i=0;i<dataSize;i++) {

            int row=i+startItemRow;
            ProductMaterial  productMaterial=productMaterials.get(i);
            addString(writableSheet, productMaterial.materialCode, 0, row);
            addString(writableSheet, productMaterial.materialName, 2, row);
            addNumber(writableSheet, productMaterial.quantity, 3, row);



            //材料长宽高
            addNumber(writableSheet, productMaterial.pLong, 4, row);
            addNumber(writableSheet, productMaterial.pWidth, 5, row);
            addNumber(writableSheet, productMaterial.pHeight, 6, row);
            //材料长宽高
            addNumber(writableSheet, productMaterial.wLong, 7, row);
            addNumber(writableSheet, productMaterial.wWidth, 8, row);
            addNumber(writableSheet, productMaterial.wHeight, 9, row);

            //定额
            addNumber(writableSheet, productMaterial.quota, 10, row);
            //单位
            addString(writableSheet, productMaterial.unitName, 11, row);
            //利用率
            addNumber(writableSheet, productMaterial.available, 12, row);

            //类型
            addNumber(writableSheet, productMaterial.type, 13, row);
            //单价
            addNumber(writableSheet, productMaterial.price, 14, row);
            //金额
            addNumber(writableSheet, productMaterial.amount, 15, row);

            //备注
            addString(writableSheet, productMaterial.memo, 16, row);
        }








    }
}
