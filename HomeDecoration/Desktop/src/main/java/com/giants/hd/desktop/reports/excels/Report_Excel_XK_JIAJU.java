package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.Xiankang;
import com.giants3.hd.entity.Xiankang_Jiaju;
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

/** 报价导出咸康灯具模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_JIAJU extends ExcelReportor {

    Workbook workbook;
    public Report_Excel_XK_JIAJU(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {




        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream=url.openStream();
        workbook = new HSSFWorkbook(inputStream);




        writeOnExcel( quotationDetail,workbook.getSheetAt(0));


        FileOutputStream fos=    new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();






    }




    protected void writeOnExcel(QuotationDetail quotationDetail, Sheet writableSheet ) throws   IOException, HdException {

        int defaultRowCount=10;
        int startRow=4;
        int dataSize=quotationDetail.items.size();
        duplicateRow(workbook,writableSheet,startRow,defaultRowCount,dataSize);







        //填充数据




        //报价日期
        addString(writableSheet,quotationDetail.quotation.qDate,5,1);

        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);


        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片

            if(isExportPicture())

                exportPicture(item.photoUrl,item.getFullProductName() );

            attachPicture(workbook,writableSheet, HttpUrl.loadProductPicture(item.photoUrl),4 , rowUpdate ,4, rowUpdate);






            addNumber(writableSheet, i + 1, 0, rowUpdate);

            //设计号  版本号
           addString(writableSheet, item.pVersion.trim(),1,rowUpdate);

            //货号
            addString(writableSheet, item.productName.trim(), 2, rowUpdate);


            //产品描述
            addString(writableSheet, item.memo, 5, rowUpdate);


            addString(writableSheet, item.spec, 6, rowUpdate);


            addNumber(writableSheet, item.price, 7, rowUpdate);





            //读取咸康数据

            RemoteData<Xiankang> xiankangRemoteData=apiManager.loadXiankangDataByProductId(item.productId);


            if(xiankangRemoteData.isSuccess()&&xiankangRemoteData.datas.size()>0)
            {

                Xiankang xiankang=xiankangRemoteData.datas.get(0);

                Xiankang_Jiaju xiankang_jiaju=xiankang.xiankang_jiaju;

                //同款货号
                addString(writableSheet,  xiankang.getQitahuohao().trim(), 3, rowUpdate);

                //甲醛标示
                addString(writableSheet,  xiankang.getJiaquan().trim(), 16, rowUpdate);
                //材料比重
                addString(writableSheet, xiankang.getCaizhibaifenbi() ,15, rowUpdate);
                addString(writableSheet, xiankang.getPack_memo() ,20,rowUpdate);
                if(xiankang_jiaju!=null)
                {


                    addString(writableSheet, xiankang_jiaju.getMucailiao() ,8,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getMupi() ,9,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getDalishi() ,10,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getBolijingzi() ,11,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getBolijingziguige_kuan() ,12,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getBolijingziguige_gao() ,13,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getChoutichicun() ,17,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getMenkaikouchicun() ,18,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getChucangchicun() ,19,rowUpdate);
                    addString(writableSheet, xiankang_jiaju.getGandu() ,31,rowUpdate);






                }



            }


            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //包装尺寸 cm
            addNumber(writableSheet, packValue[0], 25, rowUpdate);
            addNumber(writableSheet, packValue[1], 26, rowUpdate);
            addNumber(writableSheet, packValue[2], 27, rowUpdate);

            //  inch
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[0]), 22, rowUpdate);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[1]), 23, rowUpdate);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[2]), 24, rowUpdate);



            //体积
            addNumber(writableSheet, item.volumeSize, 28, rowUpdate);


            //净重
            addNumber(writableSheet, item.weight, 29, rowUpdate);


            //备注
            addString(writableSheet, item.memo, 39, rowUpdate);

        }





    }
}
