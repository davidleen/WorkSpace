package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;

/**
 * Created by davidleen29 on 2016/6/19.
 */
public abstract class AbstractExcelReporter<T> {


    //HSSF 中excel表格 插图片  dx1 dx2  是基于宽以1024为单位，高以256 单位
    private static final int WIDTH_PARAM = 1024;
    private static final int HEIGHT_PARAM = 256;

    private static final int DEFAULT_PIXEL_A_POINT = 1;


    public AbstractExcelReporter() {

    }

    public   void report(T data, String fileOutputDirectory) throws IOException, HdException
    {}


    /**
     * 复制空行
     *
     * @param sheet
     * @param startRow
     * @param defaultRowCount
     * @param dataSize
     */
    protected void duplicateRow(org.apache.poi.ss.usermodel.Workbook workbook, Sheet sheet, int startRow, int defaultRowCount, int dataSize) {


        //实际数据超出范围 插入空行
        if (dataSize > defaultRowCount) {
            //插入空行
            int rowNumToInsert = dataSize - defaultRowCount;

            if (startRow + defaultRowCount < sheet.getLastRowNum())
                sheet.shiftRows(startRow + defaultRowCount, sheet.getLastRowNum(), rowNumToInsert, true, true);

            Row rowForCopy = getRow(sheet,startRow);
            for (int j = 0; j < rowNumToInsert; j++) {

                int rowToInsert = startRow + defaultRowCount + j;

                Row row = sheet.createRow(rowToInsert);


                POIUtils.copyRow(workbook, rowForCopy, row, true, true);

            }
        }


    }


    /**
     * 产品规格转换成inch类型  并分段显示
     *
     * @param spec
     * @return
     */
    public String[] groupSpec(float[][] spec) {


        return groupSpec(spec, false);


    }

    /**
     * 产品规格转换成inch类型  并分段显示
     *
     * @param spec
     * @return
     */
    public String[] groupSpec(float[][] spec, boolean toInch) {


        int length = spec.length;
        String[] result = new String[]{"", "", ""};
        for (int i = 0; i < length; i++) {

            for (int j = 0; j < 3; j++) {
                result[j] += toInch ? UnitUtils.cmToInch(spec[i][j]) : spec[i][j];

                if (i < length - 1)
                    result[j] += StringUtils.row_separator;
            }

        }


        return result;

    }
    protected void addString(Sheet sheet, double value, int column, int rowUpdate) {
       addString(sheet,String.valueOf(value),column,rowUpdate);
    }

    protected void addString(Sheet sheet, String value, int column, int rowUpdate) {
        Cell cell = getRow(sheet,rowUpdate).getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);

    }
    protected void setColumnWidth(Sheet sheet,   int column, int width) {

        try {
            sheet.setColumnWidth(column,width);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void addNumber(Sheet sheet, double value, int column, int rowUpdate) {

        Row row = getRow(sheet,rowUpdate);
        Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);

    }protected void addNumberScaled(Sheet sheet, double value, int column, int rowUpdate) {

        Row row = getRow(sheet,rowUpdate);
        Workbook workbook= sheet.getWorkbook();
        Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
        CellStyle contextstyle =workbook.createCellStyle();

            contextstyle.cloneStyleFrom(cell.getCellStyle());

            cell.setCellStyle(contextstyle);


          DataFormat df = workbook.createDataFormat(); // 此处设置数据格式


        contextstyle.setDataFormat(df.getFormat("#,##0.00"));//保留两位小数点



        cell.setCellValue(value);

    }
    protected void addNumber(Sheet sheet, int value, int column, int rowUpdate) {
        Row row = getRow(sheet,rowUpdate);
        Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);

        cell.setCellValue(value);

    }

    private Row getRow(Sheet sheet,int rowUpdate)
    {
        Row row = sheet.getRow(rowUpdate);
        if(row==null)
        {
            row=sheet.createRow(rowUpdate);
        }
        return row;
    }

    /**
     *
     * @param workbook
     * @param sheet
     * @param url
     * @param column  第一个cell 列
     * @param row      第一个cell 的行
     * @param column2  第二个cell 列 如果图片只占用一个格子，则第一个第二个都是column row 都是一样的
     * @param row2    第二个cell 列
     */
    protected void attachPicture(org.apache.poi.ss.usermodel.Workbook workbook, Sheet sheet, String url, int column, int row, int column2, int row2) {


        int columnWidth = 0;

        for (int i = column; i <= column2; i++) {
            columnWidth += sheet.getColumnWidthInPixels(column);
        }

        int rowHeight = 0;
        for (int i = row; i <= row2; i++) {
            rowHeight +=  getRow(sheet,row).getHeightInPoints() * DEFAULT_PIXEL_A_POINT;
        }


        float padding = 0;


        int maxPictureWidth = columnWidth * 10;
        int maxPictureHeight = rowHeight * 10;


        //计算图片大小


        //图片右上角在第一个cell的宽的百分比
        float fractionX1 = 0;


        byte[] photo = null;
        //计算在cell表格中的占位参数
        int destCol1 = -1;
        int destRow1 = -1;
        int destCol2 = -1;
        int destRow2 = -1;
        int destDx1 = -1;
        int destDy1 = -1;
        int destDx2 = -1;
        int destDy2 = -1;

        //图片比例
        float pictureRatio;
        try {


            BufferedImage bufferedImage = ImageLoader.getInstance().loadImage(url);

            if (bufferedImage != null) {


                float destWidth = bufferedImage.getWidth();
                float destHeight = bufferedImage.getHeight();


                float destCellWidth;
                float destCellHeight;
                if (columnWidth >= destWidth && rowHeight >= destHeight) {
                    destCellWidth = destWidth;
                    destCellHeight = destHeight;
                } else {
                    pictureRatio = destWidth / destHeight;
                    destCellWidth = Math.min(columnWidth, pictureRatio * rowHeight);
                    destCellHeight = destCellWidth / pictureRatio;
                }
                float left = (columnWidth - destCellWidth) / 2;
                float top = (rowHeight - destCellHeight) / 2;
                float right = left + destCellWidth;
                float bottom = top + destCellHeight;


                float widthPixel = padding;
                for (int columnIndex = column; columnIndex <= column2; columnIndex++) {

                    float thisColumnPixel = sheet.getColumnWidthInPixels(column);
                    float stepPixel = widthPixel + thisColumnPixel;
                    if (stepPixel >= left && destCol1 < 0) {
                        destCol1 = columnIndex;
                        destDx1 = (int) ((left - widthPixel + padding) / thisColumnPixel * WIDTH_PARAM);
                    }


                    if (stepPixel >= right && destCol2 < 0) {
                        destCol2 = columnIndex;
                        destDx2 = (int) ((right - widthPixel + padding) / thisColumnPixel * WIDTH_PARAM);
                    }


                    widthPixel = stepPixel;
                }


                float heightPixel = padding;
                for (int rowIndex = row; rowIndex <= row2; rowIndex++) {

                    float thisRowHeight = getRow(sheet,rowIndex).getHeightInPoints() * DEFAULT_PIXEL_A_POINT;
                    float stepPixel = heightPixel + thisRowHeight;
                    if (stepPixel >= top && destRow1 < 0) {
                        destRow1 = rowIndex;
                        destDy1 = (int) ((top - heightPixel + padding) / thisRowHeight * HEIGHT_PARAM);
                    }


                    if (stepPixel >= bottom && destRow2 < 0) {
                        destRow2 = rowIndex;
                        destDy2 = (int) ((bottom - heightPixel + padding) / thisRowHeight * HEIGHT_PARAM);
                    }


                    heightPixel = stepPixel;
                }


                photo = ImageUtils.scale(bufferedImage, maxPictureWidth, maxPictureHeight, true);


            }
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (photo != null) {
            int pictureIdx = workbook.addPicture(photo, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_PNG);
            Drawing drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it


            anchor.setCol1(destCol1);
            anchor.setRow1(destRow1);

            anchor.setCol2(destCol2);
            anchor.setRow2(destRow2);

            anchor.setDx1(destDx1);
            anchor.setDy1(destDy1);
            anchor.setDx2(destDx2);
            anchor.setDy2(destDy2);
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);


            Picture pict = drawing.createPicture(anchor, pictureIdx);
            // pict.resize(1);

            //auto-size picture relative to its top-left corner

//            float columnWidth=sheet.getColumnWidthInPixels(column);
//            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;

//            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
        }


    }


    /**
     * 合并单元格
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     */

    public void combineRowAndCell(Sheet sheet,int firstRow,int lastRow,int firstCol,int lastCol)
    {


        POIUtils.addMergedRegion(sheet,firstRow, lastRow, firstCol, lastCol );
    }

    public  void setCellAlign(Workbook workbook,Sheet sheet,int firstRow,int firstCol,short horizontalAlign,short verticalAlign)
    {
        POIUtils.setCellAlign( workbook, sheet,  firstRow,  firstCol,  horizontalAlign,  verticalAlign);
    }


    public  void setCellAlignLeftCenter(Workbook workbook,Sheet sheet,int firstRow,int firstCol)
    {
        POIUtils.setCellAlign( workbook, sheet,  firstRow,  firstCol,  CellStyle.ALIGN_LEFT,  CellStyle.VERTICAL_CENTER);
    }



    protected Workbook open(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(inputStream);
        inputStream.close();

        return workbook;

    }


    protected void write(Workbook workbook, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();
        fos.flush();
        fos.close();

    }
}
