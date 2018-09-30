package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.entity.QuotationXKItem;
import com.giants3.hd.entity.Xiankang;
import com.giants3.hd.entity.Xiankang_Jingza;
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

/**咸康 画类  类模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_HUALEI extends ExcelReportor {


    Workbook workbook;
    public Report_Excel_XK_HUALEI(QuotationFile modelName) {
        super(modelName);
    }
    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {




        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream=url.openStream();
        workbook = new HSSFWorkbook(inputStream);




        writeOnExcel(workbook.getSheetAt(0), quotationDetail);


        FileOutputStream fos=    new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();






    }

    protected void writeOnExcel( Sheet writableSheet,QuotationDetail quotationDetail ) throws  IOException, HdException {






        int defaultRowCount=10;

        int startRow=5;





        int dataSize=quotationDetail.XKItems.size();

        //实际数据超出范围 插入空行
        duplicateRow(workbook,writableSheet,startRow,defaultRowCount,dataSize);









        //报价日期
//        label1 = new Label(5, 1, quotationDetail.quotation.qDate,format);
//        writableSheet.addCell(label1);
        addString(writableSheet,quotationDetail.quotation.qDate ,5,1);




        //报价日期
//                label1 = new Label(14, 1, "Verdoer YUNFEI",format);
//        writableSheet.addCell(label1);

        addString(writableSheet,"Verdoer YUNFEI" ,14,1);

        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);


        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationXKItem item=quotationDetail.XKItems.get(i);



            //图片
            final String productUrl = !StringUtils.isEmpty(item.photoUrl) ? item.photoUrl : item.photo2Url;
            if(isExportPicture())
                exportPicture(productUrl,item.getFullProductName() );

                attachPicture(workbook,writableSheet, HttpUrl.loadProductPicture(productUrl), 4  , rowUpdate  ,4, rowUpdate);


            //读取咸康数据


            RemoteData<Xiankang> xiankangRemoteData=item.productId>0?apiManager.loadXiankangDataByProductId(item.productId):item.productId2>0?apiManager.loadXiankangDataByProductId(item.productId2):null;


          

//            //读取咸康数据
//            ProductDetail productDetail2=    apiManager.loadProductDetail(item.productId2).datas.get(0);

            //行号
//            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,i+1 ,0,rowUpdate);

            //设计号  版本号
//            label1 = new Label(1, rowUpdate, item.pVersion,format);
//            writableSheet.addCell(label1);
            addString(writableSheet,!StringUtils.isEmpty(item.pVersion)?item.pVersion:item.pVersion2 ,1,rowUpdate);
            //货号
//            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
//            writableSheet.addCell(label1);


            addString(writableSheet,!StringUtils.isEmpty(item.productName)?item.productName:item.productName2 ,2,rowUpdate);




            //单位
//            label1 = new Label(7, rowUpdate,  item.unit ,format);
//            writableSheet.addCell(label1);

            addString(writableSheet,!StringUtils.isEmpty(item.unit)?item.pVersion:item.unit2,7,rowUpdate);


                if(xiankangRemoteData!=null&&xiankangRemoteData.isSuccess()&&xiankangRemoteData.datas.size()>0)
                {

                    Xiankang xiankang=xiankangRemoteData.datas.get(0);


                //同款货号
//                label1 = new Label(3, rowUpdate,  xiankang.getQitahuohao() ,format);
//                writableSheet.addCell(label1);

                addString(writableSheet, xiankang.getQitahuohao(),3,rowUpdate);
                //材料比重

//                label1 = new Label(5, rowUpdate,   xiankang.getCaizhibaifenbi(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang.getCaizhibaifenbi(),5,rowUpdate);
                //甲醛标示
//                label1 = new Label(6, rowUpdate,   xiankang.getJiaquan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang.getJiaquan(),6,rowUpdate);

                //边框
//                label1 = new Label(11, rowUpdate,   xiankang.getBiankuang(),format);
//                writableSheet.addCell(label1);





                    Xiankang_Jingza xiankang_jingza = xiankang.xiankang_jingza;
                    addString(writableSheet, xiankang_jingza.getBiankuang(),11,rowUpdate);

                //槽宽

//                label1 = new Label(12, rowUpdate,   xiankang.getCaokuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getCaokuan(),12,rowUpdate);
                //槽深
//                label1 = new Label(13, rowUpdate,   xiankang.getCaokuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getCaoshen(),13,rowUpdate);
                //画尺寸	宽
//                label1 = new Label(16, rowUpdate,   xiankang.getHuangui_kuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getHuangui_kuan(),16,rowUpdate);

                //画尺寸	高
//                label1 = new Label(17, rowUpdate,   xiankang.getJingzi_kuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getHuangui_gao(),17,rowUpdate);

                //玻璃尺寸	宽
//                label1 = new Label(18, rowUpdate,   xiankang.getJingzi_kuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getBoliguige_kuan(),18,rowUpdate);
                //玻璃尺寸	高
//                label1 = new Label(19, rowUpdate,   xiankang.getJingzi_kuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getBoliguige_gao(),19,rowUpdate);

                //正面开口尺寸	宽
//                label1 = new Label(20, rowUpdate,   xiankang.getJingzi_kuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getJingzi_kuan(),20,rowUpdate);

                //正面开口尺寸	高
//                label1 = new Label(21, rowUpdate,   xiankang.getJingzi_kuan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet, xiankang_jingza.getJingzi_gao(),21,rowUpdate);

//                //磨边
//                label1 = new Label(17, rowUpdate,   xiankang.getMobian(),format);
//                writableSheet.addCell(label1);

                    //折盒包装描述
                    addString(writableSheet,  xiankang.pack_memo,26,rowUpdate);



                    //挂钩距离
//            label1 = new Label(53, rowUpdate,  String.valueOf( xiankang.getGuaju()) ,format);
//            writableSheet.addCell(label1);
                    addString(writableSheet, xiankang_jingza.getGuaju(),53,rowUpdate);

                    //画芯号
//            label1 = new Label(57, rowUpdate,  String.valueOf( xiankang.getHuaxinbianhao()) ,format);
//            writableSheet.addCell(label1);
                    addString(writableSheet, xiankang_jingza.getHuaxinbianhao(),57,rowUpdate);

                    //画芯厂商
//            label1 = new Label(58, rowUpdate,  String.valueOf( xiankang.getHuaxinchangshang()) ,format);
//            writableSheet.addCell(label1);
                    addString(writableSheet, xiankang_jingza.getHuaxinchangshang(),58,rowUpdate);

                    //画芯效果
//            label1 = new Label(59, rowUpdate,  String.valueOf( xiankang.getHuaxinxiaoguo()) ,format);
//            writableSheet.addCell(label1);
                    addString(writableSheet, xiankang_jingza.getHuaxinxiaoguo(),59,rowUpdate);

                }



//            //折盒价格
//            num = new Number(8, rowUpdate,  item.price,format);
//            writableSheet.addCell(num);

            addNumber(writableSheet, item.price,8,rowUpdate);

            //加强价格
//            num = new Number(9, rowUpdate,  item.price2 ,format);
//            writableSheet.addCell(num);
            addNumber(writableSheet, item.price2,9,rowUpdate);



            float[] specValue=  StringUtils.decouplePackageString(item.spec);

            //总长
//            label1 = new Label(22, rowUpdate, String.valueOf(specValue[0]),format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, specValue[0],22,rowUpdate);

            //总宽
//            label1 = new Label(23, rowUpdate, String.valueOf(specValue[1]),format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, specValue[1],23,rowUpdate);

            //总深
//            label1 = new Label(24, rowUpdate,  String.valueOf(specValue[2]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, specValue[2],24,rowUpdate);

            //重量
//            label1 = new Label(25, rowUpdate,  String.valueOf(item.weight) ,format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, item.weight,25,rowUpdate);

            //折盒包装描述
//            label1 = new Label(26, rowUpdate,  String.valueOf( xiankang.pack_memo) ,format);
//            writableSheet.addCell(label1);




            //几个装
//            label1 = new Label(32, rowUpdate,  String.valueOf(item.packQuantity) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, item.packQuantity,32,rowUpdate);

            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //折盒包装l
//            label1 = new Label(33, rowUpdate,  String.valueOf(packValue[0]) ,format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, packValue[0],33,rowUpdate);
            addNumber(writableSheet, packValue[0],33,rowUpdate);
            //折盒包装w
//            label1 = new Label(34, rowUpdate,  String.valueOf(packValue[1]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, packValue[1],34,rowUpdate);
            //折盒包装h
//            label1 = new Label(35, rowUpdate,  String.valueOf(packValue[2]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, packValue[2],35,rowUpdate);

            //折盒包装l cm
//            label1 = new Label(37, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[0])) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,UnitUtils.cmToInch(packValue[0]),37,rowUpdate);
            //折盒包装w cm
//            label1 = new Label(38, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[1])) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,UnitUtils.cmToInch(packValue[1]),38,rowUpdate);

            //折盒包装h cm
//            label1 = new Label(39, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[2])) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,UnitUtils.cmToInch(packValue[2]),39,rowUpdate);
           // PF Pack G.W. (KGS)
//            label1 = new Label(40, rowUpdate, "" ,format);
//            writableSheet.addCell(label1);

            addString(writableSheet,"",40,rowUpdate);

                //加强包装描述
//            label1 = new Label(41, rowUpdate,  String.valueOf(item.memo) ,format);
//            writableSheet.addCell(label1);
            addString(writableSheet,item.memo2,41,rowUpdate);
            //几个装
//            label1 = new Label(42, rowUpdate,  String.valueOf(item.packQuantity2) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,item.packQuantity2,42,rowUpdate);

            float[] packValue2=  StringUtils.decouplePackageString(item.packageSize2);

            //加强包装包装l
//            label1 = new Label(43, rowUpdate,  String.valueOf(packValue2[0]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,packValue2[0],43,rowUpdate);
            //加强包装w
//            label1 = new Label(44, rowUpdate,  String.valueOf(packValue2[1]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,packValue2[1],44,rowUpdate);

            //加强包装h
//            label1 = new Label(45, rowUpdate,  String.valueOf(packValue2[2]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,packValue2[2],45,rowUpdate);


            //加强包装l cm
//            label1 = new Label(49, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[0])) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,UnitUtils.cmToInch(packValue2[0]),49,rowUpdate);

            //加强包装w cm
//            label1 = new Label(50, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[1])) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet,UnitUtils.cmToInch(packValue2[1]),50,rowUpdate);

            //加强包装h cm
//            label1 = new Label(51, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[2])) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue2[2]), 51, rowUpdate);
            //Reshipper Pack N.W. (KGS)

//            label1 = new Label(52, rowUpdate, "" ,format);
//            writableSheet.addCell(label1);

            addString(writableSheet,"",52,rowUpdate);




            //备注
//            label1 = new Label(64, rowUpdate,  String.valueOf(item.memo) ,format);
//            writableSheet.addCell(label1);

            String memo=!StringUtils.isEmpty(item.memo)?item.memo:item.memo2;

            addString(writableSheet,memo,64,rowUpdate);

        }





    }
}
