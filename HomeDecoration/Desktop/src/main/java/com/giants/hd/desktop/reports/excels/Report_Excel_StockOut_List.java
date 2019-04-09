package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import com.google.inject.Guice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**导出出库清单
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockOut_List extends  AbstractExcelReporter<ErpStockOutDetail>{
    private static final String TEMPLATE_FILE_NAME = "excel/PACKING-LIST-YUNFEI-RM.xls";
    int styleRow=10;

    Workbook workbook;
    public Report_Excel_StockOut_List()
    {




    }


    @Override
    public void report(ErpStockOutDetail data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream=	getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME) ;
        String fileName=fileOutputDirectory+ File.separator+data.erpStockOut.ck_no+"-PACKING-LIST-YUNFEI-RM.xls";
        //Create Workbook instance holding reference to .xlsx file
        workbook = new HSSFWorkbook(inputStream);
        writeOnExcel(data ,workbook  );
        FileOutputStream fos=    new FileOutputStream(fileName);

        workbook.write(fos);
        workbook.close();


        fos.close();





    }

    /**
     *报表
     * @param data
     * @param workbook
     */
    private void writeOnExcel(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(0);


        //日期
        addString(writableSheet, "Invoice No:"+data.erpStockOut.ck_dd, 11, 7);

        //客户
        addString(writableSheet, data.erpStockOut.cus_no, 1, 4);

        //发票号
        addString(writableSheet,"Invoice No:"+ data.erpStockOut.ck_no, 11,4);


        //目的港
        String mdgText="FROM FUZHOU TO %s BY SEA";
        addString(writableSheet, String.format(mdgText,  data.erpStockOut.mdg), 0, 17);

        //提单号
        addString(writableSheet,"提单号："+ data.erpStockOut.tdh, 2, 8);


        //客户信息

        addString(writableSheet,  data.erpStockOut.adr2, 0, 5);
        addString(writableSheet,  data.erpStockOut.tel1, 0, 6);
        addString(writableSheet,  data.erpStockOut.fax, 0, 7);

        //添加唛头信息
      ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        //唛头所有订单的唛头

        //所关联的所有订单号。
        Set<String> orders=new HashSet<>();


        List<ErpStockOutItem> items = data.items;


        //分类柜号 封签号
        Set<String> guihaoSet = new HashSet<>();
        Map<String, String> guihaoMap = new HashMap();
        Map<String, List<ErpStockOutItem>> groupMaps = new HashMap<>();

        for (ErpStockOutItem item : items) {

            guihaoSet.add(item.guihao);
            guihaoMap.put(item.guihao, item.fengqianhao);
            List<ErpStockOutItem> guiItems = groupMaps.get(item.guihao);
            if (guiItems == null) {
                guiItems = new ArrayList<>();
                groupMaps.put(item.guihao, guiItems);
            }
            orders.add(item.os_no);
            guiItems.add(item);

        }


        //所有唛头数据
        StringBuilder  maitous=new StringBuilder();

        for(String os_no:orders)
        {

            try {
                RemoteData<ErpOrderDetail>  remoteData=apiManager.getOrderDetail(os_no);
                if (remoteData.isSuccess())
                {
                    String zhengmai=remoteData.datas.get(0).erpOrder.zhengmai;
                    if(!StringUtils.isEmpty(zhengmai))
                    maitous.append(zhengmai).append(",");
                }
            } catch (HdException e) {
                e.printStackTrace();
            }
        }

        if(maitous.length()>1)
            maitous.setLength(maitous.length()-1);
        //
        addString(writableSheet, maitous.toString(), 0, 10);
        //未进行分柜的 显示在前


        int row = 21;
        Set<String> keys = groupMaps.keySet();

        int totalxs
                =0;
        int totalqty=0;
        //体积
        float totaltiji=0;


        /**
         * 总毛重
         */
        int totalmz=0;
        //总敬重
        float totaljz=0;

        for (String key : keys) {



            int xs
                    =0;
            int stockOutQty=0;
            //体积
            float tiji=0;
            //净重
            float jz=0;
            //净重
            float mz=0;
            addString(writableSheet,  "CONT#:"+(StringUtils.isEmpty(key)?"":(key +"  "+guihaoMap.get(key))), 0, row);

            row++;
            List<ErpStockOutItem> stockOutItems = groupMaps.get(key);
            for (ErpStockOutItem stockOutItem : stockOutItems) {

                addString(writableSheet, stockOutItem.bat_no, 0, row);
                addString(writableSheet, stockOutItem.prd_no, 1, row);
                //包装
                addString(writableSheet, stockOutItem.describe, 2, row);
                //包装
                addString(writableSheet, stockOutItem.unit, 3, row);
                int itemXs=stockOutItem.so_zxs==0?0: (stockOutItem.stockOutQty/ stockOutItem.so_zxs) ;

                addNumber(writableSheet, itemXs, 5, row);

                addNumber(writableSheet, stockOutItem.stockOutQty, 6, row);

                addNumber(writableSheet, Float.valueOf(stockOutItem.so_zxs), 7, row);

                addString(writableSheet, "/", 8, row);


                float[] xg = StringUtils.decouplePackageString(stockOutItem.khxg);
                addNumber(writableSheet, xg[0], 9, row);
                addString(writableSheet, "*", 10, row);
                addNumber(writableSheet, xg[1], 11, row);
                addString(writableSheet, "*", 12, row);
                addNumber(writableSheet, xg[2], 13, row);

                tiji+= (xg[0]*xg[1]*xg[2])/1000000*stockOutItem.stockOutQty;
                addString(writableSheet, stockOutItem.cus_os_no, 16, row);

                xs+=itemXs;
                stockOutQty+= stockOutItem.stockOutQty;
                mz+=stockOutItem.mz*stockOutItem.stockOutQty;
                jz+=stockOutItem.jz1*stockOutItem.stockOutQty;
                row++;

            }


            addString(writableSheet,  "TTL:", 3, row);
            addNumber(writableSheet, xs, 5, row);
            addNumber(writableSheet, stockOutQty, 6, row);
            addString(writableSheet,  "N.W.:", 9, row);
            addNumber(writableSheet,  jz, 11, row);
            addString(writableSheet,  "KGS", 12, row);
            row++;
            addNumber(writableSheet,  tiji, 5, row);
            addString(writableSheet,  "CBM", 6, row);
            addString(writableSheet,  "G.W.:", 9, row);
            addNumber(writableSheet,  mz, 11, row);
            addString(writableSheet,  "KGS", 12, row);
            row++;
            row++;


            totalqty+=stockOutQty;
            totaltiji+=tiji;
            totalxs+=xs;
            totaljz+=jz;
            totalmz+=mz;

        }




        row++;
        row++;

        addString(writableSheet,  "总计", 0, row);
        addString(writableSheet,  "TTL:", 3, row);
        addNumber(writableSheet, totalxs, 5, row);
        addNumber(writableSheet, totalqty, 6, row);
        addString(writableSheet,  "N.W.:", 9, row);
        addNumber(writableSheet,  totaljz, 11, row);
        addString(writableSheet,  "KGS", 12, row);
        row++;
        addNumber(writableSheet,  totaltiji, 5, row);
        addString(writableSheet,  "CBM", 6, row);
        addString(writableSheet,  "G.W.:", 9, row);
        addNumber(writableSheet,  totalmz, 11, row);
        addString(writableSheet,  "KGS", 12, row);
        row++;
        row++;

        addString(writableSheet,  "CALIFORNIA 93120 PHASE 2 COMPLIANT FOR FORMALDEHYDE", 0, row);
        row++;
        addString(writableSheet,  "THIS SHIPMENT DOES NOT CONTAIN ANY SOLID WOOD PACKING MATERIAL.", 0,row);



    }







}
