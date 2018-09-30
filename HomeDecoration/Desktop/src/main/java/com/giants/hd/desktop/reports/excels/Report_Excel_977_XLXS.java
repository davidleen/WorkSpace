package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.noEntity.QuotationDetail;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 977客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_977_XLXS extends ExcelReportor {
    int styleRow = 10;

    XSSFWorkbook workbook;

    public Report_Excel_977_XLXS(QuotationFile modelName) {
        super(modelName);


    }


    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException {


        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream = url.openStream();
        workbook = new XSSFWorkbook(inputStream);


        writeOnSheet(workbook.getSheetAt(0), quotationDetail);


        FileOutputStream fos = new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();


    }


    protected void writeOnSheet(XSSFSheet sheet, QuotationDetail detail) {


        //  sheet.getRow(10).getRowStyle()

        int startRow = 11;

        int defaultRow = 20;
        int dataSize = detail.items.size();
        duplicateRow(workbook, sheet, startRow, defaultRow, dataSize);


        for (int i = 0; i < dataSize; i++) {

            QuotationItem item = detail.items.get(i);
            int rowUpdate = i + startRow;
            //添加照片


            //图片

            if (isExportPicture())
                exportPicture(item.photoUrl, item.getFullProductName());


            addPicture(sheet, HttpUrl.loadProductPicture(item.photoUrl), 1, rowUpdate, 1, rowUpdate);

//            attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion), 1 + pictureGap / 2, rowUpdate + pictureGap / 2, 1 - pictureGap, 1 - pictureGap);


            //货号
            setCellValue(sheet, item.productName.trim(), 2, rowUpdate);


            //   材质百分比
            setCellValue(sheet, item.constitute.trim(), 3, rowUpdate);

            //   产品描述
            setCellValue(sheet, item.memo.trim(), 4, rowUpdate);


            //产品尺寸

            String[] specValue = groupSpec(StringUtils.decoupleSpecString(item.spec));
            setCellValue(sheet, specValue[0], 7, rowUpdate);
            setCellValue(sheet, specValue[1], 8, rowUpdate);
            setCellValue(sheet, specValue[2], 9, rowUpdate);


            //外箱尺寸
            float[] packValue = StringUtils.decouplePackageString(item.packageSize);
//
            setCellValue(sheet, UnitUtils.cmToInch(packValue[0]), 10, rowUpdate);
            setCellValue(sheet, UnitUtils.cmToInch(packValue[1]), 11, rowUpdate);
            setCellValue(sheet, UnitUtils.cmToInch(packValue[2]), 12, rowUpdate);


            //重量  英镑
            setCellValue(sheet, item.weight, 13, rowUpdate);


            //cbf
            setCellValue(sheet, item.volumeSize, 14, rowUpdate);

            //内箱数
            setCellValue(sheet, item.inBoxCount, 15, rowUpdate);


            //装箱数
            setCellValue(sheet, item.packQuantity, 16, rowUpdate);
            //单价 fob
            setCellValue(sheet, item.price, 19, rowUpdate);


        }


    }


    private void setCellValue(XSSFSheet sheet, String value, int column, int rowUpdate) {
        Cell cell = sheet.getRow(rowUpdate).getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(CellUtil.getCell(CellUtil.getRow(styleRow, sheet), column).getCellStyle());
        cell.setCellValue(value);

    }

    private void setCellValue(XSSFSheet sheet, double value, int column, int rowUpdate) {
        Row row = sheet.getRow(rowUpdate);
        Cell cell = row.getCell(column);
        if (cell == null) {
            cell = row.createCell(column, Cell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(CellUtil.getCell(CellUtil.getRow(styleRow, sheet), column).getCellStyle());

        }
        cell.setCellValue(value);

    }


    private void addPicture(XSSFSheet sheet, String url, int column, int row, int column2, int row2) {


        float columnWidth = sheet.getColumnWidthInPixels(column);
        float rowHeight = sheet.getRow(row).getHeightInPoints() / 3 * 4;


        byte[] photo = null;


        try {


            BufferedImage bufferedImage = ImageLoader.getInstance().loadImage(url);
            if (bufferedImage != null)
                photo = ImageUtils.scale(bufferedImage, (int) columnWidth * 4, (int) rowHeight * 4, true);
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (photo != null) {
            int pictureIdx = workbook.addPicture(photo, Workbook.PICTURE_TYPE_PNG);
            Drawing drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(column);
            anchor.setRow1(row);
//            anchor.setCol2(column+1);
//            anchor.setRow2(row+1);
            //  anchor.setDx1((int) (columnWidth / 10   ));
            //   anchor.setDx2((int) (rowHeight / 10    ));
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);


            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(0.8);

            //auto-size picture relative to its top-left corner

//            float columnWidth=sheet.getColumnWidthInPixels(column);
//            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;

//            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
        }


    }
}
