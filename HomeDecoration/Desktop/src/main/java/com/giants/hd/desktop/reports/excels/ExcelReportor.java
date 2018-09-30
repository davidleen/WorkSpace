package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by davidleen29 on 2015/8/6.
 */
public   class ExcelReportor extends AbstractExcelReporter<QuotationDetail> {



    //HSSF 中excel表格 插图片  dx1 dx2  是基于宽以1024为单位，高以256 单位
    private static final int WIDTH_PARAM=1024;
    private static final int HEIGHT_PARAM=256;

    private static final int DEFAULT_PIXEL_A_POINT =1;


    public boolean isExportPicture() {
        return isExportPicture;
    }

    private  boolean  isExportPicture;

    protected QuotationFile file;


    PictureFileExporter fileExporter;
    public ExcelReportor(QuotationFile file)
    {this.file=file;

    }
    @Override
    public     void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException,   HdException {


        final String filePath = fileOutputDirectory + File.separator + quotationDetail.quotation.qNumber;
        String outputFilePath= filePath + "." + file.appendix;
        if(fileExporter==null)
            fileExporter=new PictureFileExporter(filePath);
        operation(quotationDetail, new URL(HttpUrl.loadQuotationFile(file.name, file.appendix)), outputFilePath);


    }






    protected  void operation(QuotationDetail quotationDetail,URL url,String outputFile) throws IOException,   HdException {

//        InputStream inputStream=url.openStream();
//        Workbook existingWorkbook = Workbook.getWorkbook(inputStream);
//        inputStream.close();
//        WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(outputFile), existingWorkbook,s);
//        workbookCopy.write();
//        workbookCopy.close();
//
//        doOnLocalFile(quotationDetail, new File(outputFile));

    }

    /**
     * 导出图片
     * @param url
     * @param fileName
     */
    protected  void exportPicture(String url,String fileName)
    {

        if(fileExporter!=null)
        {
            fileExporter.exportFile(url,fileName);
        }

    }


    /**
     * 设置是否导出图片
     * @param exportPicture
     */
    public  void setExportPicture(boolean  exportPicture)
    {

        isExportPicture=exportPicture;

    }

}
