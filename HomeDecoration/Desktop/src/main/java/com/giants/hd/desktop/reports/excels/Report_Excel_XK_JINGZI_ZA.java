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

/**咸康 画杂  镜子类模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_JINGZI_ZA extends ExcelReportor {

    Workbook workbook;
    public Report_Excel_XK_JINGZI_ZA(QuotationFile modelName) {
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


    protected void writeOnExcel(Sheet writableSheet,QuotationDetail quotationDetail) throws IOException, HdException {




        int defaultRowCount=10;
        int startRow=5;
        int dataSize=quotationDetail.XKItems.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook,writableSheet,startRow,defaultRowCount,dataSize);







        //填充数据





        //报价日期

        addString(writableSheet,quotationDetail.quotation.qDate,5,1);




        addString(writableSheet,"Verdoer YUNFEI",14,1);


        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);

        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationXKItem item=quotationDetail.XKItems.get(i);



            //图片

            String photoUrl=item.productId>0?item.photoUrl:item.photo2Url;
            String productName=item.productId>0?item.productName:item.productName2;
            String pVersion=item.productId>0?item.pVersion:item.pVersion2;
            String unit=item.productId>0?item.unit:item.unit;
            if(isExportPicture())

                exportPicture(photoUrl,item.getFullProductName() );
            attachPicture(workbook,writableSheet, HttpUrl.loadProductPicture(photoUrl),4, rowUpdate ,4, rowUpdate);





            //读取咸康数据
//            ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);
            RemoteData<Xiankang> remoteData1=item.productId>0?apiManager.loadXiankangDataByProductId(item.productId):null;

            RemoteData<Xiankang>  remoteData2=item.productId2>0?apiManager.loadXiankangDataByProductId(item.productId2):null;

//            //读取咸康数据
//            ProductDetail productDetail2=    apiManager.loadProductDetail(item.productId2).datas.get(0);

            //行号
//            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
//            writableSheet.addCell(label1);
            addString(writableSheet,String.valueOf(i+1),0,rowUpdate);

            //设计号  版本号
//            label1 = new Label(1, rowUpdate, item.pVersion,format);
//            writableSheet.addCell(label1);

            addString(writableSheet,pVersion,1,rowUpdate);

            //货号
//            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
//            writableSheet.addCell(label1);

            addString(writableSheet,productName,2,rowUpdate);





            //单位
//            label1 = new Label(8, rowUpdate,  item.unit ,format);
//            writableSheet.addCell(label1);

            addString(writableSheet,unit,8,rowUpdate);
            Xiankang_Jingza xiankang_jingza=null;
            Xiankang xiankang=null;
            if(remoteData1!=null&&remoteData1.isSuccess()&&remoteData1.datas.size()>0)
            {



                xiankang=remoteData1.datas.get(0);
                 xiankang_jingza=xiankang.xiankang_jingza;
                //折盒包装描述
                addString(writableSheet, xiankang.pack_memo, 23, rowUpdate);
            }



            //折盒价格
//            label1 = new Label(10, rowUpdate, String.valueOf(item.price),format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, item.price, 10, rowUpdate);


            //加强价格
//            label1 = new Label(11, rowUpdate,  String.valueOf(item.price2),format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, item.price2, 11, rowUpdate);


            String spec=item.productId>0?item.spec:item.spec2;

            String[] specValue=  StringUtils.groupSpec(StringUtils.decoupleSpecString(spec));
            //总长
//            label1 = new Label(18, rowUpdate, String.valueOf(specValue[0]),format);
//            writableSheet.addCell(label1);

            addString(writableSheet, specValue[0], 19, rowUpdate);


            //总宽
//            label1 = new Label(19, rowUpdate, String.valueOf(specValue[1]),format);
//            writableSheet.addCell(label1);
            addString(writableSheet, specValue[1], 20, rowUpdate);

            //总深
//            label1 = new Label(20, rowUpdate,  String.valueOf(specValue[2]) ,format);
//            writableSheet.addCell(label1);
            addString(writableSheet, specValue[2], 21, rowUpdate);





            //重量
//            label1 = new Label(21, rowUpdate,  String.valueOf(item.weight) ,format);
//            writableSheet.addCell(label1);
            float weight=item.productId>0?item.weight:item.weight2;
            addNumber(writableSheet, weight, 22, rowUpdate);







            //几个装
//            label1 = new Label(28, rowUpdate,  String.valueOf(item.packQuantity) ,format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, item.packQuantity, 29, rowUpdate);


            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //折盒包装l
//            label1 = new Label(29, rowUpdate,  String.valueOf(packValue[0]) ,format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, UnitUtils.cmToInch(packValue[0]), 30, rowUpdate);
            //折盒包装w
//            label1 = new Label(30, rowUpdate,  String.valueOf(packValue[1]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[1]), 31, rowUpdate);
            //折盒包装h
//            label1 = new Label(31, rowUpdate,  String.valueOf(packValue[2]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[2]), 32, rowUpdate);


            //折盒包装l cm
//            label1 = new Label(33, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[0])) ,format);
//            writableSheet.addCell(label1);
            if(!StringUtils.isEmpty(item.packageSize)) {
                String[] specString = StringUtils.groupSpec(StringUtils.decoupleSpecString(item.packageSize),false);
                addString(writableSheet, (specString[0]), 34, rowUpdate);
                //折盒包装w cm
//            label1 = new Label(34, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[1])) ,format);
//            writableSheet.addCell(label1);
                addString(writableSheet, specString[1], 35, rowUpdate);
                //折盒包装h cm
//            label1 = new Label(35, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[2])) ,format);
//            writableSheet.addCell(label1);
                addString(writableSheet, specString[2], 36, rowUpdate);
//                System.out.println("specString:"+spec+"==="+specString[0]+","+specString[1]+","+specString[2]);

            }







            if(remoteData2!=null&&remoteData2.isSuccess()&&remoteData2.datas.size()>0) {




                xiankang=remoteData2.datas.get(0);

                  xiankang_jingza=xiankang.xiankang_jingza;
                //加强包装描述
                addString(writableSheet, xiankang.pack_memo, 38, rowUpdate);




            }





            //几个装
//            label1 = new Label(38, rowUpdate,  String.valueOf(item.packQuantity2) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, item.packQuantity2, 39, rowUpdate);

            float[] packValue2=  StringUtils.decouplePackageString(item.packageSize2);
            //加强包装包装l
//            label1 = new Label(39, rowUpdate,  String.valueOf(packValue2[0]) ,format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, UnitUtils.cmToInch(packValue2[0]), 40, rowUpdate);
            //加强包装w
//            label1 = new Label(40, rowUpdate,  String.valueOf(packValue2[1]) ,format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, UnitUtils.cmToInch(packValue2[1]), 41, rowUpdate);

            //加强包装h
//            label1 = new Label(41, rowUpdate,  String.valueOf(packValue2[2]) ,format);
//            writableSheet.addCell(label1);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue2[2]), 42, rowUpdate);


            //加强包装l cm
//            label1 = new Label(45, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[0])) ,format);
//            writableSheet.addCell(label1);

            if(!StringUtils.isEmpty(item.packageSize2)) {
                String[] specString = StringUtils.groupSpec(StringUtils.decoupleSpecString(item.packageSize2),false);
                addString(writableSheet, specString[0], 46, rowUpdate);

                //加强包装w cm
//            label1 = new Label(46, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[1])) ,format);
//            writableSheet.addCell(label1);
                addString(writableSheet,specString[1], 47, rowUpdate);// specString[1]


                //加强包装h cm
//            label1 = new Label(47, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[2])) ,format);
//            writableSheet.addCell(label1);

                addString(writableSheet, specString[2], 48, rowUpdate);

            }


            //备注
            String memo=item.productId>0?item.memo:item.memo2;
            addString(writableSheet, memo, 58, rowUpdate);





            if(xiankang!=null)
            {
                //同款货号
//                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
//                writableSheet.addCell(label1);

                addString(writableSheet,xiankang.getQitahuohao(),3,rowUpdate);
                //材料比重

//                label1 = new Label(6, rowUpdate,  productDetail.product.xiankang.getCaizhibaifenbi(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet,xiankang.getCaizhibaifenbi(),6,rowUpdate);
                //甲醛标示
//                label1 = new Label(7, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet,xiankang.getJiaquan(),7,rowUpdate);



            }


            //镜子相关描述，
            if(xiankang_jingza!=null)
            {
                //边框
//                label1 = new Label(12, rowUpdate,  productDetail.product.xiankang.getBiankuang(),format);
//                writableSheet.addCell(label1);

                //挂钩距离
                addString(writableSheet, xiankang_jingza.getGuaju(), 50, rowUpdate);

                //边框
                addString(writableSheet,xiankang_jingza.getBiankuang(),12,rowUpdate);
                //槽宽

//                label1 = new Label(13, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
//                writableSheet.addCell(label1);

                addString(writableSheet,xiankang_jingza.getCaokuan(),13,rowUpdate);

                //槽深
//                label1 = new Label(14, rowUpdate,  productDetail.product.xiankang.getCaoshen(),format);
//                writableSheet.addCell(label1);

                addString(writableSheet,xiankang_jingza.getCaoshen(),14,rowUpdate);

                //镜子尺寸	宽
//                label1 = new Label(15, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
//                writableSheet.addCell(label1);

                addString(writableSheet,xiankang_jingza.getJingzi_kuan(),15,rowUpdate);

                //镜子尺寸	高
//                label1 = new Label(16, rowUpdate,  productDetail.product.xiankang.getJingzi_gao(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet,xiankang_jingza.getJingzi_gao(),16,rowUpdate);


                //磨边
//                label1 = new Label(17, rowUpdate,  productDetail.product.xiankang.getMobian(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet,xiankang_jingza.getMobian(),17,rowUpdate);

                /**
                 * 背后框边尺寸（不含槽位）
                 */
                addString(writableSheet,xiankang_jingza.getBeikuanchicun(),18,rowUpdate);


            }

        }







    }
}
