package com.giants.hd.desktop.reports.excels;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Iterator;

public class POIUtils {

    /**
     * 复制一个单元格样式到目的单元格样式
     * @param fromStyle
     * @param toStyle
     */
    public static void copyCellStyle(CellStyle fromStyle,
                                     CellStyle toStyle) {
        toStyle.setAlignment(fromStyle.getAlignment());
        //边框和边框颜色
        toStyle.setBorderBottom(fromStyle.getBorderBottom());
        toStyle.setBorderLeft(fromStyle.getBorderLeft());
        toStyle.setBorderRight(fromStyle.getBorderRight());
        toStyle.setBorderTop(fromStyle.getBorderTop());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        //背景和前景
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPattern());
//		toStyle.setFont(fromStyle.getFont(null));
        toStyle.setHidden(fromStyle.getHidden());
        toStyle.setIndention(fromStyle.getIndention());//首行缩进
        toStyle.setLocked(fromStyle.getLocked());
        toStyle.setRotation(fromStyle.getRotation());//旋转
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
        toStyle.setWrapText(fromStyle.getWrapText());

    }
    /**
     * Sheet复制
     * @param fromSheet
     * @param toSheet
     * @param copyValueFlag
     */
    public static void copySheet(Workbook wb,Sheet fromSheet, Sheet toSheet,
                                 boolean copyValueFlag) {
        //合并区域处理
        mergerRegion(fromSheet, toSheet);
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
            Row tmpRow = (Row) rowIt.next();
            Row newRow = toSheet.createRow(tmpRow.getRowNum());
            //行复制
            copyRow(wb,tmpRow,newRow,copyValueFlag);
        }
    }

    /**
     * 行复制功能
     * @param fromRow
     * @param toRow
     */
    public static void copyRow(Workbook wb,Row fromRow,Row toRow,boolean copyValueFlag)
    {

        copyRow(  wb,  fromRow,  toRow,  copyValueFlag,  true);

    }
    /**
     * 行复制功能
     * @param fromRow
     * @param toRow
      * @param copyValueFlag
     * @param useSourceStyle  是否使用源格子的 style
     *
     */
    public static void copyRow(Workbook wb,Row fromRow,Row toRow,boolean copyValueFlag,boolean useSourceStyle){
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
            Cell tmpCell =  (Cell) cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
            toRow.setHeight(fromRow.getHeight());

            copyCell(wb, tmpCell, newCell, copyValueFlag,useSourceStyle);
        }
    }
    /**
     * 复制原有sheet的合并单元格到新创建的sheet
     *
     * @param fromSheet 新创建sheet
     * @param toSheet      原有的sheet
     */
    public static void mergerRegion(Sheet fromSheet, Sheet toSheet) {
        int sheetMergerCount = fromSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(mergedRegionAt);
        }
    }
    public static void copyCell(Workbook wb,Cell srcCell, Cell distCell,
                                boolean copyValueFlag) {


        copyCell(  wb,  srcCell,   distCell,
          copyValueFlag,false);
    }

    /**
     * 复制单元格
     *
     * @param srcCell
     * @param distCell
     * @param copyValueFlag
     *            true则连同cell的内容一起复制
     */
    public static void copyCell(Workbook wb,Cell srcCell, Cell distCell,
                                boolean copyValueFlag,boolean useFromStyle) {

        CellStyle oldCellStyle = srcCell.getCellStyle();

        if(useFromStyle)
        {

            distCell.setCellStyle(oldCellStyle);
        }else
        {

            CellStyle newstyle=wb.createCellStyle();



            copyCellStyle(oldCellStyle, newstyle);
            //样式
            distCell.setCellStyle(newstyle);
        }

        //评论
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }

        // 不同数据类型处理
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (copyValueFlag) {
            if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
                if (DateUtil.isCellDateFormatted(srcCell)) {
                    distCell.setCellValue(srcCell.getDateCellValue());
                } else {
                    distCell.setCellValue(srcCell.getNumericCellValue());
                }
            } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
                // nothing21
            } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            } else { // nothing29
            }
        }
    }

    public static int  addMergedRegion(Sheet sheet,int firstRow,int lastRow,int firstCol,int lastCol)
    {


      return  sheet.addMergedRegion(new CellRangeAddress(  firstRow,   lastRow,   firstCol,   lastCol));

    }

    public static void setCellAlign(Workbook workbook, Sheet sheet, int row, int col, short horizontalAlign, short verticalAlign) {


        Cell cell=        sheet.getRow(row).getCell(col);
;
        CellStyle cellStyle=cell.getCellStyle();
      if (cellStyle==null) cellStyle=workbook.createCellStyle();
        cellStyle.setAlignment(horizontalAlign);
        cellStyle.setVerticalAlignment(verticalAlign);
        cell.setCellStyle(cellStyle);
    }
}