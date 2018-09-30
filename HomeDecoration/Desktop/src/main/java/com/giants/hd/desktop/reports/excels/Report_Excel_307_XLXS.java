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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**9307客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_307_XLXS extends ExcelReportor {
    int styleRow=20;

    XSSFWorkbook workbook;

    public static final int PAGE_ITEM_COUNT=10;

    public Report_Excel_307_XLXS(QuotationFile modelName)   {
        super(modelName);


    }




    @Override
    public     void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException,   HdException {

        int dataSize = quotationDetail.items.size();


        URL url = new URL(HttpUrl.loadQuotationFile(file.name, file.appendix));

        int totalPages=(dataSize-1)/PAGE_ITEM_COUNT+1;


        String outputFilePath;
        for (int i = 0; i < totalPages; i++) {









                InputStream inputStream = url.openStream();
                workbook = new XSSFWorkbook(inputStream);
                inputStream.close();
                operation(quotationDetail, workbook, i, PAGE_ITEM_COUNT);
                outputFilePath = fileOutputDirectory + File.separator + quotationDetail.quotation.qNumber + (i < 1 ? "" : ("_" + i)) + "." + file.appendix;
                FileOutputStream fos = new FileOutputStream(outputFilePath);
                workbook.write(new FileOutputStream(outputFilePath));

                workbook.close();
                fos.flush();
                fos.close();





        }
    }



    private void operation(QuotationDetail detail,XSSFWorkbook workbook,int pageIndex, int pageCount) {


            XSSFSheet sheet=workbook.getSheet("Data Sheet");

        int fisrtRow=20;
        int startItem=pageIndex*pageCount;
        int endItem=Math.min((pageIndex+1)*pageCount,detail.items.size());
        for (int i=0;i<endItem-startItem;i++)
        {

            QuotationItem item=detail.items.get(i+startItem);

            int  rowUpdate=i+fisrtRow;

            //货号
            setCellValue(sheet,item.productName,1,rowUpdate);
            //描述
            setCellValue(sheet,item.memo,2,rowUpdate);
            //fist cost
            setCellValue(sheet,item.price,6,rowUpdate);

//case pack
            setCellValue(sheet,item.packQuantity,8,rowUpdate);
            //inner pack
            setCellValue(sheet,item.inBoxCount,9,rowUpdate);


            //外箱尺寸 case pack
            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);
//
            setCellValue(sheet, UnitUtils.cmToInch(packValue[0]), 11, rowUpdate);
            setCellValue(sheet, UnitUtils.cmToInch(packValue[1]), 12, rowUpdate);
            setCellValue(sheet, UnitUtils.cmToInch(packValue[2]), 13, rowUpdate);


            //产品尺寸

            String[]  specValue= groupSpec(StringUtils.decoupleSpecString(item.spec));
            setCellValue(sheet, specValue[0], 17, rowUpdate);
            setCellValue(sheet, specValue[1], 18, rowUpdate);
            setCellValue(sheet, specValue[2], 19, rowUpdate);


            //含包装重
            //setCellValue(sheet,UnitUtils.kgToPound(item.packWeight), 22, rowUpdate);
            //重量  英镑
            setCellValue(sheet,UnitUtils.kgToPound(item.weight), 22, rowUpdate);

            setCellValue(sheet,item.constitute, 24, rowUpdate);
            //插入图片
            XSSFSheet itemSheet=workbook.getSheet("Item ("+(i+1)+")");


            attachPicture(workbook,itemSheet,HttpUrl.loadProductPicture(item.photoUrl),6,19,14,34);



        }



    }




    protected void setCellValue(XSSFSheet sheet,String value, int column ,int rowUpdate)
    {
        Cell cell = sheet.getRow(rowUpdate).getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(CellUtil.getCell(CellUtil.getRow(styleRow,sheet),column).getCellStyle());
        cell.setCellValue(value);

    }

    protected void setCellValue(XSSFSheet sheet,double value, int column,int rowUpdate)
    {
        Row row=sheet.getRow(rowUpdate);
        Cell cell = row.getCell(column);
        if(cell==null)
        {
            cell=row.createCell(column,Cell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(CellUtil.getCell(CellUtil.getRow(styleRow,sheet),column).getCellStyle());

        }
        cell.setCellValue(value);

    }



    protected void addPicture(XSSFSheet sheet,String url,int column, int row,int column2, int row2)  {




        float columnWidth=sheet.getColumnWidthInPixels(column) ;
           float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4 ;


        byte[] photo=null;



        try {
            BufferedImage bufferedImage=ImageLoader.getInstance().loadImage(url );
            if(bufferedImage!=null)
            photo=   ImageUtils.scale(bufferedImage,1280, 1280,true);
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(photo!=null ) {
            int pictureIdx = workbook.addPicture(photo, Workbook.PICTURE_TYPE_PNG);
            Drawing drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(column);
            anchor.setRow1(row);
            anchor.setCol2(column2);
           anchor.setRow2(row2);
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
