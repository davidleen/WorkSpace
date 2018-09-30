package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.utils.AccumulateMap;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**127 客户报价模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_127 extends ExcelReportor {


    public Report_Excel_127(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {





        InputStream inputStream=url.openStream();
         Workbook workbook = new HSSFWorkbook(inputStream);





        writeOnExcel(quotationDetail,workbook);





        FileOutputStream fos=    new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();






    }



    protected void writeOnExcel(QuotationDetail quotationDetail, Workbook writableWorkbook) throws  IOException, HdException {


        int size = quotationDetail.items.size();

        AccumulateMap names=new AccumulateMap();


        Quotation quotation=quotationDetail.quotation;

        Sheet sampleSheet = writableWorkbook.getSheetAt(0);
        for(int i=0;i< size;i++) {
            QuotationItem item = quotationDetail.items.get(i);

            names.accumulate(item.productName);


            int duplicateCount = names.get(item.productName).intValue();




            Sheet writableSheet = writableWorkbook.createSheet(item.productName + (duplicateCount > 1 ? ("_" + (duplicateCount - 1)) : ""));
            POIUtils.copySheet(writableWorkbook, sampleSheet, writableSheet, true);







            //????????
            addString(writableSheet, quotation.qDate, 5, 2);

            //????
            addString(writableSheet, item.productName, 1, 3);



            //??????
            addString(writableSheet, item.memo, 1, 4);


            //???
            addString(writableSheet, item.spec, 1, 6);


            //????
            addNumber(writableSheet, item.weight, 6, 18);


            //?????
            addNumber(writableSheet, item.inBoxCount, 6, 22);

            //?????
            addNumber(writableSheet, item.packQuantity, 6, 23);


            //??????
            addString(writableSheet, item.packageSize, 6, 26);



            //cbm ???
            addNumber(writableSheet, item.volumeSize, 6, 27);



            //fob
            float fob=item.price;
            addNumber(writableSheet, fob, 6, 41);


            if(isExportPicture())

                exportPicture(item.photoUrl,item.getFullProductName() );


             attachPicture(writableWorkbook,writableSheet, HttpUrl.loadProductPicture(item.photoUrl), 0  , 8 , 5  , 48  );



//
//            //???????????
//             addString(writableSheet, "", 5, 25);
//            addString(writableSheet, "", 13, 18);
//            addString(writableSheet, "", 13, 19);


        }







   }

}
