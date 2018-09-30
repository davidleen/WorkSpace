package com.giants.hd.desktop.utils;


import com.giants.hd.desktop.reports.excels.JTableToExcelReporter;
import com.giants.hd.desktop.reports.excels.POIUtils;
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


/**
 * 把JTable中的数据导出到Excel表格中工具类
 *
 * @author Administrator
 */
public class JTableToExcel {

    /**
     * 外部调用的方法
     *
     * @param file
     * @param heading
     * @param describe
     * @param table
     */
    public static void export(File file, String heading, String describe, String title, JTable table) {


        JTableToExcelReporter tableToExcelReporter=new JTableToExcelReporter(title,heading,describe);

        try {
            tableToExcelReporter.report(table,file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HdException e) {
            e.printStackTrace();
        }


    }








}