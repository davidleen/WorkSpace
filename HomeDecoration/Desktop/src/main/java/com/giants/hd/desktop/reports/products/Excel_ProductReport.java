package com.giants.hd.desktop.reports.products;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.reports.excels.AbstractExcelReporter;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FileUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.noEntity.ProductDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 2015/10/7.
 */
public class Excel_ProductReport  extends AbstractExcelReporter<Product> {


    /**
     * 产品格式2导出
     */
    public void reportProduct2(List<Product> products, String path) throws IOException, InvalidFormatException {


        URL url = new URL(HttpUrl.loadQuotationFile("产品批量导出格式2", "xls"));


        Workbook workbook = open(url);

        Sheet sheet = workbook.getSheetAt(0);


        int fisrtRow = 1;

        int size = products.size();

        for (int i = 0; i < size; i++) {

            int rowUpdate = fisrtRow + i;
            Product product = products.get(i);

            setRowHeight(sheet, rowUpdate, 1000);


            //货号
            addString(sheet, product.name, 3, rowUpdate);
            //版本
            addString(sheet, product.pVersion, 4, rowUpdate);
            addString(sheet, "S/", 5, rowUpdate);
            //单位
            int unitSize = 1;
            try {
                unitSize = Integer.valueOf(product.pUnitName.substring(product.pUnitName.lastIndexOf("/") + 1).trim());
            } catch (Throwable t) {
                t.printStackTrace();

            }
            addNumber(sheet, unitSize, 6, rowUpdate);
            //fob
            addNumber(sheet, product.fob, 7, rowUpdate);


            //装箱数
            addNumber(sheet, product.insideBoxQuantity, 8, rowUpdate);
            addString(sheet, "/", 9, rowUpdate);

            //外箱
            addNumber(sheet, product.packQuantity, 10, rowUpdate);
            addString(sheet, "/", 11, rowUpdate);


//
            addNumber(sheet, product.packLong, 12, rowUpdate);
            addString(sheet, "*", 13, rowUpdate);
            addNumber(sheet, product.packWidth, 14, rowUpdate);
            addString(sheet, "*", 15, rowUpdate);
            addNumber(sheet, product.packHeight, 16, rowUpdate);

            addNumber(sheet, product.packVolume, 17, rowUpdate);


            addString(sheet, product.spec, 18, rowUpdate);

            addNumber(sheet, product.weight, 19, rowUpdate);

            addNumber(sheet, product.cost, 20, rowUpdate);

            addNumber(sheet, product.price, 21, rowUpdate);


            try {
                //缓存图片
                if (!StringUtils.isEmpty(product.url)) {
                    String file = ImageLoader.getInstance().cacheFile(HttpUrl.loadProductPicture(product.url));
                    String basePath = new File("").getAbsolutePath();
                    FileUtils.copyFile(new File(path + File.separator + product.name + (StringUtils.isEmpty(product.pVersion) ? "" : "_" + product.pVersion) + ".JPG"), new File(basePath + File.separator + file));
                }
            } catch (Throwable t) {
            }

        }

        String fileName = DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime()) + ".xls";
        write(workbook, new File(path + File.separator + fileName));


    }

    /**
     * 产品格式1导出
     */
    public void reportProduct1(List<Product> products, String path) throws IOException, InvalidFormatException {


        reportProduct1_3(products,path,false);

    }



    /**
     * 导出材料清单
     */
    public void exportProductDetail(ProductDetail productDetail, String path) throws IOException {

        exportProductDetail(productDetail, path, 0);
        exportProductDetail(productDetail, path, 1);
        exportProductDetail(productDetail, path, 3);

    }

    private void exportProductDetail(ProductDetail detail, String path, int index) throws IOException {


        String nameAppendix = "";
        List<ProductMaterial> datas = null;
        switch (index) {
            case 0:
                nameAppendix = "白胚";
                datas = detail.conceptusMaterials;
                break;
            case 1:
                nameAppendix = "组装";
                datas = detail.assembleMaterials;
                break;
            case 2:


                break;
            case 3:
                nameAppendix = "包装";
                datas = detail.packMaterials;
                break;
        }


        Workbook workbook = new HSSFWorkbook();


        Sheet writableSheet = workbook.createSheet("Sheet1");


        addString(writableSheet, "子件代码", 0, 0);
        addString(writableSheet, "用量", 1, 0);
        addString(writableSheet, "特征", 2, 0);
        addString(writableSheet, "损耗", 3, 0);


        if (datas == null) return;


        int startRow = 1;


        int size = datas.size();
        for (int i = 0; i < size; i++) {

            ProductMaterial material = datas.get(i);
            int updateRow = i + startRow;

            addString(writableSheet, material.materialCode, 0, updateRow);

            addNumber(writableSheet, FloatHelper.scale(material.quota, 3), 1, updateRow);
            String value = "";
            if (index == 3) {
                value = (material.pLong > 0 ? (material.pLong) : "") + (material.pWidth > 0 ? ("*" + material.pWidth) : "") + (material.pHeight > 0 ? ("*" + material.pHeight) : "");

            }
            addString(writableSheet, value, 2, updateRow);

            addNumber(writableSheet, FloatHelper.scale(1 - material.available, 3), 3, updateRow);


        }


        String fileName = path + File.separator + detail.product.name + (StringUtils.isEmpty(detail.product.pVersion) ? "" : ("-" + detail.product.pVersion)) + "_" + nameAppendix + ".xls";

        write(workbook, new File(fileName));


    }


    private Workbook open(URL url) throws IOException {
        InputStream inputStream = url.openStream();

        Workbook workbook = new HSSFWorkbook(inputStream);
        inputStream.close();

        return workbook;

    }











    private void setRowHeight(Sheet sheet, int rowUpdate, int height) {

        Row row = sheet.getRow(rowUpdate);
        if (row == null)
            row = sheet.createRow(rowUpdate);
        row.setHeight((short) height);
    }


    private void setRowHeight(Row writeRow, int height) {


        writeRow.setHeight((short) height);
    }




    public void reportProduct3(List<Product> products, String path ) throws IOException {
        reportProduct1_3(products,path,true);
    }

    /**
     * 批量导出格式 3  跟 1 的区别是 1 是图片导出至文件夹  3 是 图片内嵌至excel中
     * @param products
     * @param path
     * @Param pictureInExcel 图片是否嵌入excel 。  false  图片导出到文件夹
     */
    public void reportProduct1_3(List<Product> products, String path,boolean pictureInExcel) throws IOException {


        URL url = new URL(HttpUrl.loadQuotationFile("产品批量导出格式1", "xls"));

        Workbook workbook = open(url);


        Date time = Calendar.getInstance().getTime();
        String fileName = path + File.separator + DateFormats.FORMAT_YYYY_MM_DD.format(time) + "_" + time.getTime() + ".xls";

        write(workbook, new File(fileName));


        workbook = open(new File(fileName));


        Sheet sheet = workbook.getSheetAt(0);


        int fisrtRow = 1;

        int size = products.size();


        String basePath = new File("").getAbsolutePath();


        for (int i = 0; i < size; i++) {

            int rowUpdate = fisrtRow + i;
            Product product = products.get(i);


            Row writeRow = sheet.createRow(rowUpdate);

            setRowHeight(writeRow, 1000);


            //货号
            addString(sheet, product.name, 1, rowUpdate);
            //版本
            addString(sheet, product.pVersion, 2, rowUpdate);
            addString(sheet, product.pUnitName, 3, rowUpdate);
            addString(sheet, product.insideBoxQuantity + "/" + product.packQuantity + "/" + product.packLong + "*" + product.packWidth + "*" + product.packHeight, 4, rowUpdate);
            addNumber(sheet, FloatHelper.scale(product.fob, 2), 5, rowUpdate);

            addNumber(sheet, FloatHelper.scale(product.cost, 2), 6, rowUpdate);
            //白胚综合
            addNumber(sheet, product.conceptusCost + product.conceptusWage, 7, rowUpdate);
            addNumber(sheet, product.assembleCost + product.assembleWage, 8, rowUpdate);
            addNumber(sheet, product.paintCost + product.paintWage, 9, rowUpdate);
            addNumber(sheet, product.packCost + product.packWage, 10, rowUpdate);


            if(pictureInExcel)
            {

                attachPicture(workbook,sheet,HttpUrl.loadPicture(product.url),0,rowUpdate,0,rowUpdate   );
            }else {
                try {
                    //缓存图片
                    if (!StringUtils.isEmpty(product.url)) {
                        String file = ImageLoader.getInstance().cacheFile(HttpUrl.loadProductPicture(product.url));
                        //存放图片绝对路径
                        addString(sheet, basePath + File.separator + file, 100, rowUpdate);

                        FileUtils.copyFile(new File(path + File.separator + product.name + (StringUtils.isEmpty(product.pVersion) ? "" : "_" + product.pVersion) + ".JPG"), new File(basePath + File.separator + file));
                    }
                } catch (Throwable t) {
                }
            }





        }


        write(workbook, new File(fileName));




    }
}
