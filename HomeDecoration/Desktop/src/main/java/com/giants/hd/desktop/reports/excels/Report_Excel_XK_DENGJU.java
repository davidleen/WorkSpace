package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.Xiankang;
import com.giants3.hd.entity.Xiankang_Dengju;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;
import com.google.inject.Guice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 报价导出咸康灯具模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_DENGJU extends ExcelReportor {

    Workbook workbook;

    public Report_Excel_XK_DENGJU(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {


        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream = url.openStream();
        workbook = new HSSFWorkbook(inputStream);


        writeOnExcel(workbook.getSheetAt(0), quotationDetail);


        FileOutputStream fos = new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();


    }

    protected void writeOnExcel(Sheet writableSheet, QuotationDetail quotationDetail) throws IOException, HdException {
        int defaultRowCount = 10;

        int startRow = 4;


        int dataSize = quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startRow, defaultRowCount, dataSize);


        //填充数据


        //报价日期
        //设计号  版本号
//        label1 = new Label(7, 1, quotationDetail.quotation.qDate,format);
//        writableSheet.addCell(label1);
        addString(writableSheet, quotationDetail.quotation.qDate, 7, 1);


        ApiManager apiManager = Guice.createInjector().getInstance(ApiManager.class);

        for (int i = 0; i < dataSize; i++) {
            int rowUpdate = startRow + i;
            QuotationItem item = quotationDetail.items.get(i);


            //图片
            if (isExportPicture())
                exportPicture(item.photoUrl, item.getFullProductName());


            attachPicture(workbook, writableSheet, HttpUrl.loadProductPicture(item.photoUrl), 4, rowUpdate, 4, rowUpdate);


            //行号
            //设计号  版本号
//            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, i + 1, 0, rowUpdate);


            //设计号  版本号
//            label1 = new Label(1, rowUpdate, item.pVersion,format);
//            writableSheet.addCell(label1);

            addString(writableSheet, item.pVersion, 1, rowUpdate);

            //货号
//            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
//            writableSheet.addCell(label1);
            addString(writableSheet, item.productName, 2, rowUpdate);


            //材料比重
            //货号
//            label1 = new Label(8, rowUpdate,   constitute);
//            writableSheet.addCell(label1);
            addString(writableSheet, item.constitute, 8, rowUpdate);


            //净重
            addNumber(writableSheet, item.weight, 39, rowUpdate);
            addString(writableSheet, item.memo, 58, rowUpdate);


            //读取咸康数据
            RemoteData<Xiankang> xiankangRemoteData = apiManager.loadXiankangDataByProductId(item.productId);


            if (xiankangRemoteData.isSuccess() && xiankangRemoteData.datas.size() > 0) {

                Xiankang xiankang = xiankangRemoteData.datas.get(0);
                Xiankang_Dengju xiankang_dengju = xiankang.xiankang_dengju;

                //同款货号
//                label1 = new Label(3, rowUpdate,  xiankang.getQitahuohao() ,format);
//                writableSheet.addCell(label1);

                addString(writableSheet, xiankang.getQitahuohao(), 3, rowUpdate);

                //材料百分比
                addString(writableSheet, xiankang.getQitahuohao(), 8, rowUpdate);

                //甲醛标示

                addString(writableSheet, xiankang.getJiaquan(), 9, rowUpdate);

                //包装描述
                addString(writableSheet, xiankang.getPack_memo(), 50, rowUpdate);


                //材质
//                label1 = new Label(10, rowUpdate,   xiankang.getCaizhi(),format);
//                writableSheet.addCell(label1);


                if (xiankang_dengju != null) {


                    addString(writableSheet, xiankang_dengju.getCaizhi(), 10, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengyongtu(), 7, rowUpdate);

                    addString(writableSheet, xiankang_dengju.getDengtichangshang(), 13, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengzhaochangshang(), 14, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getFeiniuhao(), 15, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getFeiniukuan(), 16, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getFeiniuchang(), 17, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getFeiniugao(), 18, rowUpdate);


                    addString(writableSheet, xiankang_dengju.getDengdikuan(), 27, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengdichang(), 28, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengzhaochicun(), 29, rowUpdate);

                    addString(writableSheet, xiankang_dengju.getDengpaoleixing(), 31, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengtouleixing(), 32, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getWashu(), 33, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengtoukaiguangleixing(), 34, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getHafujia(), 35, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getHafujiayanse(), 36, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengzhaobaozhuang(), 43, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengzhaobaozhuang(), 45, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getDengzhaobuliao(), 49, rowUpdate);
                    addString(writableSheet, xiankang_dengju.get_8dtt(), 51, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getKdyn(), 52, rowUpdate);
                    addString(writableSheet, xiankang_dengju.getULApproval(), 53, rowUpdate);

                }


            }

        }


    }
}
