package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.exception.HdException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 这个是单纯的表格数据导出。
 * Created by davidleen29 on 2018/9/23.
 */
public class JTableToExcelReporter extends AbstractExcelReporter<JTable> {


    private final String title;
    private final String heading;
    private final String describe;

    public JTableToExcelReporter(String title, String heading, String describe) {
        this.title = title;
        this.heading = heading;
        this.describe = describe;
    }

    @Override
    public void report(JTable table, String fileOutputDirectory) throws IOException, HdException {


        try {
            Workbook workbook = new HSSFWorkbook();


            Sheet writableSheet = workbook.createSheet(title);
            writableSheet.setDefaultColumnWidth(10);// 设置列宽
            writableSheet.setDefaultRowHeightInPoints(  100);// 设置列宽

            // 取得Table的行数(rowNum), 列数(colNum)
            int rowNum = table.getRowCount();
            int colNum = table.getColumnCount();

            // 填写主标题
            fillHeading(writableSheet, heading, colNum);

            // 填写列名
            fillColumnName(writableSheet, table, colNum);

            // 填写落款
            fillInscribe(writableSheet, describe, rowNum+4, Math.max(0,colNum-2));

            // 填写数据
            fillCell(writableSheet, table, rowNum, colNum);

            FileOutputStream fos = new FileOutputStream(fileOutputDirectory);

            workbook.write(fos);
            workbook.close();


            fos.close();

            // 导出成功提示框
            int dialog = JOptionPane.showConfirmDialog(null, "表导出成功！是否现在打开？", "提示", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.YES_OPTION) {
                Runtime.getRuntime().exec("cmd /c start \"\" \"" + new File(fileOutputDirectory) + "\"");
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "导入数据前请关闭工作表");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "没有进行筛选");
            e.printStackTrace();
        }
    }


    /**
     * 填写落款
     *
     * @param sheet
     * @param inscribe
     * @param colNum
     * @param rowNum
     */
    private void fillInscribe(Sheet sheet, String inscribe, int rowNum, int colNum) {
        if (inscribe == null || inscribe.length() < 1) {

            inscribe = "导出时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        addString(sheet, inscribe, colNum,rowNum );

//        WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.NO_BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 定义格式化对象
//
//        format.setAlignment(Alignment.RIGHT);// 水平居中显示
//
//        sheet.mergeCells(0, rowNum + 3, colNum - 1, rowNum + 3);// 合并单元格
//
//        sheet.addCell(new Label(0, rowNum + 3, inscribe, format));// 填写工作表

    }

    /**
     * 填写主标题
     *
     * @param sheet
     * @param heading
     * @param colNum
     */
    private void fillHeading(Sheet sheet, String heading, int colNum) {


        addString(sheet, heading,colNum  ,0 );


//        WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.RED);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 创建格式化对象
//
//        format.setAlignment(Alignment.CENTRE);// 水平居中显示
//
//        format.setVerticalAlignment(VerticalAlignment.CENTRE);// 垂直居中显示
//
//        sheet.mergeCells(0, 0, colNum - 1, 0); // 合并单元格
//
//        sheet.setRowView(0, 600); // 设置行高
//
//        sheet.addCell(new Label(0, 0, heading, format));// 填写工作表


    }

    /**
     * 填写列名
     *
     * @param sheet
     * @param table
     * @param colNum
     */
    private void fillColumnName(Sheet sheet, JTable table, int colNum) {


//        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 定义格式化对象
//
//        format.setAlignment(Alignment.CENTRE);// 水平居中显示
//
//        sheet.setColumnView(0, 15);// 设置列宽


        for (int col = 0; col < colNum; col++) {

//            Label colName = new Label(col, 1, table.getModel().getColumnName(col), format);
//
//            sheet.addCell(colName);
            addString(sheet, table.getModel().getColumnName(col), col, 1);
        }
    }


    /**
     * 填写数据
     *
     * @param sheet
     * @param table
     * @param rowNum
     * @param colNum
     */
    private void fillCell(Sheet sheet, JTable table, int rowNum, int colNum) {
//        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 定义格式化对象
//
//        format.setAlignment(Alignment.CENTRE); // 水平居中显示
        int startRow = 2;

        for (int i = 0; i < colNum; i++) {    // 列

            for (int j = 0; j <  rowNum; j++) {// 行
                Object object = table.getValueAt(j  , i);
                String str = object == null ? "" : object.toString();


                try {


                    addString(sheet, str, i, j+startRow);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }
}
