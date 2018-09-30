//package com.giants.hd.desktop.reports.excels;
//
//import com.giants.hd.desktop.local.ImageLoader;
//import com.giants3.hd.exception.HdException;
//import com.giants3.hd.utils.file.ImageUtils;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.CellUtil;
//
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.net.MalformedURLException;
//
///**
// * Created by david on 2015/11/4.
// */
//public class ExecelWorker {
//
//
//
//
//
//    private void setCellValue(Sheet sheet,String value, int column ,int rowUpdate)
//    {
//        Cell cell = sheet.getRow(rowUpdate).getCell(column, Row.CREATE_NULL_AS_BLANK);
//        cell.setCellStyle(CellUtil.getCell(CellUtil.getRow(styleRow, sheet), column).getCellStyle());
//        cell.setCellValue(value);
//
//
//
//
//    }
//
//    private void setCellValue(Sheet sheet,double value, int column,int rowUpdate)
//    {
//        Row row=sheet.getRow(rowUpdate);
//        Cell cell = row.getCell(column);
//        if(cell==null)
//        {
//            cell=row.createCell(column,Cell.CELL_TYPE_NUMERIC);
//            cell.setCellStyle(CellUtil.getCell(CellUtil.getRow(styleRow,sheet),column).getCellStyle());
//
//        }
//        cell.setCellValue(value);
//
//    }
//
//
//
//    private void addPicture(Workbook workbook,Sheet sheet,String url,int column, int row,int column2, int row2)  {
//
//
//
//
//        float columnWidth=sheet.getColumnWidthInPixels(column) ;
//        float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4 ;
//
//
//        byte[] photo=null;
//
//
//        try {
//
//
//            BufferedImage bufferedImage= ImageLoader.getInstance().loadImage(url );
//            if(bufferedImage!=null)
//                photo=   ImageUtils.scale(bufferedImage, (int) columnWidth * 4, (int) rowHeight * 4, true);
//        } catch (HdException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        if(photo!=null ) {
//            int pictureIdx = workbook.addPicture(photo, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_PNG);
//            Drawing drawing = sheet.createDrawingPatriarch();
//            CreationHelper helper = workbook.getCreationHelper();
//            //add a picture shape
//            ClientAnchor anchor = helper.createClientAnchor();
//            //set top-left corner of the picture,
//            //subsequent call of Picture#resize() will operate relative to it
//            anchor.setCol1(column);
//            anchor.setRow1(row);
////            anchor.setCol2(column+1);
////            anchor.setRow2(row+1);
//            //  anchor.setDx1((int) (columnWidth / 10   ));
//            //   anchor.setDx2((int) (rowHeight / 10    ));
//            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
//
//
//            Picture pict = drawing.createPicture(anchor, pictureIdx);
//            pict.resize(0.8);
//
//            //auto-size picture relative to its top-left corner
//
////            float columnWidth=sheet.getColumnWidthInPixels(column);
////            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;
//
////            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
//        }
//
//
//
//    }
//
//}
