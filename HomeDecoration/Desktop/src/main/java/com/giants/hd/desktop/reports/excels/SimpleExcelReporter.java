package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.exception.HdException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 所有excel表格导出基类
 * Created by davidleen29 on 2017/9/9.
 */
public abstract class SimpleExcelReporter<T> extends AbstractExcelReporter<T> {

    @Override
    public final void report(T data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        final String templateFilePath = getTemplateFilePath();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templateFilePath);
        String fileName = fileOutputDirectory + File.separator + getDestFileName(data);
        System.out.println(fileName);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        report(data, workbook);
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        workbook.close();
        fos.close();



    }


    /**
     * @param data
     * @param workbook
     */
    protected abstract void report(T data, Workbook workbook);


    /**
     * 获取excel 模板文件
     *
     * @return
     */
    protected abstract String getTemplateFilePath();


    /**
     * 获取目标文件
     *
     * @return
     */
    protected abstract String getDestFileName(T data);

}
