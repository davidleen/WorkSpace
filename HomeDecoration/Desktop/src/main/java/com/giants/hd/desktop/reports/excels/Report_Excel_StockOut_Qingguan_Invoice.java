package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.google.inject.Guice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 导出情感发票单
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockOut_Qingguan_Invoice extends SimpleExcelReporter<ErpStockOutDetail> {

    public static final String FILE_NAME = "福州云飞清关发票";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";
    int styleRow = 10;

    Workbook workbook;

    public Report_Excel_StockOut_Qingguan_Invoice() {


    }


    /**
     * @param data
     * @param workbook
     */
    @Override
    protected void report(ErpStockOutDetail data, Workbook workbook) {


        writeOnExcel(data,workbook);

    }

    /**
     * 获取excel 模板文件
     *
     * @return
     */
    @Override
    protected String getTemplateFilePath() {


        return
                TEMPLATE_FILE_NAME;
    }

    /**
     * 获取目标文件
     *
     * @return
     */
    @Override
    protected String getDestFileName(ErpStockOutDetail data) {
        return data.erpStockOut.ck_no + "_" + FILE_NAME + "_" + DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime()) + ".xls";

    }

    /**
     * 报表
     *
     * @param data
     * @param workbook
     */
    private void writeOnExcel(ErpStockOutDetail data, Workbook workbook) {




        exportList(data, workbook);
        exportInvoice(data, workbook);
        exportOrder(data, workbook);

    }


    /**
     * 导出sheet头部部分。三个sheet 头部一样
     */
    public void exportSheetHead(ErpStockOutDetail data, Sheet writableSheet) {
        //发票单号
        addString(writableSheet, "Invoice No.:" + data.erpStockOut.ck_no, 6, 4);
        //Po号
        addString(writableSheet, "PO#:" , 6, 5);
        // S/C NO:
        addString(writableSheet, "S/C NO:", 6, 6);
        //Date:
        addString(writableSheet, "Date:" + data.erpStockOut.ck_dd, 6, 7);
        //Country of origin:
       //   addString(writableSheet, "Country of origin:"+"FUZHOU", 10, 8);

        //客户信息

        addString(writableSheet, data.erpStockOut.fp_name, 0, 5);
        addString(writableSheet, data.erpStockOut.adr2, 0, 6);
        addString(writableSheet, data.erpStockOut.tel1, 0, 7);
        addString(writableSheet, data.erpStockOut.fax, 0, 8);

        addString(writableSheet, data.erpStockOut.maitou, 1, 12);


        //目的港
        String mdgText = "FROM FUZHOU TO %s BY SEA";
        addString(writableSheet, String.format(mdgText, data.erpStockOut.mdg), 0, 10);
        //所有唛头数据
        StringBuilder maitous = new StringBuilder();

        ApiManager apiManager = Guice.createInjector().getInstance(ApiManager.class);

        Set<String> orders = new HashSet<>();
        Set<String> pos = new HashSet<>();
        for (ErpStockOutItem item : data.items) {
            orders.add(item.os_no);
            pos.add(item.cus_os_no);
        }
        for (String os_no : orders) {

            try {
                RemoteData<ErpOrderDetail> remoteData = apiManager.getOrderDetail(os_no);
                if (remoteData.isSuccess()) {
                    String zhengmai = remoteData.datas.get(0).erpOrder.zhengmai;
                    if (!StringUtils.isEmpty(zhengmai))
                        maitous.append(zhengmai).append(",");
                }
            } catch (HdException e) {
                e.printStackTrace();
            }
        }

        if (maitous.length() > 1)
            maitous.setLength(maitous.length() - 1);
        //
        addString(writableSheet, maitous.toString(), 1, 11);

        addString(writableSheet, "PO#:" + StringUtils.combine(pos), 6, 5);

    }


    /**
     * 合同sheet数据
     */
    public void exportOrder(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(2);
        exportSheetHead(data, writableSheet);

        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 27;
//
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);
        int row = 0;
        int totalXs = 0;
        for (int i = 0; i < dataSize; i++) {
            row = i + startItemRow;
            ErpStockOutItem outItem = items.get(i);
            addString(writableSheet, outItem.bat_no, 0, row);
            addString(writableSheet, outItem.prd_no, 1, row);
            addString(writableSheet, outItem.unit, 2, row);

            //单款箱数
            final int xs =outItem.so_zxs==0?0:( (outItem.stockOutQty - 1) / outItem.so_zxs + 1);
            totalXs += xs;
            //净重
            addNumber(writableSheet, xs, 3, row);
            //数量
            addNumber(writableSheet, outItem.stockOutQty, 4, row);
//描述
            addString(writableSheet, outItem.describe, 5, row);
            //fob

            addNumber(writableSheet, outItem.up, 6, row);

            //amount
            addNumber(writableSheet, FloatHelper.scale(outItem.up * outItem.stockOutQty), 7, row);

            //cbm
            addNumber(writableSheet, outItem.xgtj, 8, row);

            addString(writableSheet, outItem.specInch, 9, row);

        }


        row++;
        //添加上汇总行

        int totalStockOutQty = 0;
        float totalAmt = 0;
        for (int i = 0; i < dataSize; i++) {

            totalStockOutQty += items.get(i).stockOutQty;

            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up
            );
            ;
        }
        addString(writableSheet, totalXs + " /CTNS", 3, row);
        addString(writableSheet, totalStockOutQty + " /PCS", 4, row);
        addNumber(writableSheet, totalAmt, 7, row);


    }

    /**
     * 发票sheet数据
     */
    public void exportInvoice(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(1);
        exportSheetHead(data, writableSheet);

        //所关联的所有订单号。
        Set<String> orders = new HashSet<>();
        //分类柜号 封签号
        Set<String> guihaoSet = new HashSet<>();
        Map<String, String> guihaoMap = new HashMap();
        Map<String, List<ErpStockOutItem>> groupMaps = new HashMap<>();

        for (ErpStockOutItem item : data.items) {

            String guihao = StringUtils.isEmpty(item.guihao) ? "" : item.guihao;
            guihaoSet.add(guihao);
            guihaoMap.put(guihao, item.fengqianhao);
            List<ErpStockOutItem> guiItems = groupMaps.get(guihao);
            if (guiItems == null) {
                guiItems = new ArrayList<>();
                groupMaps.put(guihao, guiItems);
            }
            orders.add(item.os_no);
            guiItems.add(item);

        }


        //
        //   addString(writableSheet, maitous.toString(), 0, 10);
        //未进行分柜的 显示在前


        Set<String> keys = groupMaps.keySet();
        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 27;


        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize + keys.size());


        int row = startItemRow;

        int totalXs = 0;
        for (String guihao : keys) {


            String fengqianhao = guihaoMap.get(guihao);
            //合并单元格
            combineRowAndCell(writableSheet, row, row, 0, 8);
            setCellAlignLeftCenter(workbook, writableSheet, row, 0);
            addString(writableSheet, StockStringHelper.generateCombineString(guihao, fengqianhao), 0, row);
            row++;
            List<ErpStockOutItem> groupItems = groupMaps.get(guihao);
            for (ErpStockOutItem outItem : groupItems) {

                addString(writableSheet, outItem.bat_no, 0, row);
                addString(writableSheet, outItem.prd_no, 1, row);
                addString(writableSheet, outItem.unit, 2, row);


                //单款箱数
                final int xs =outItem.so_zxs==0?0: ((outItem.stockOutQty - 1) / outItem.so_zxs + 1);
                totalXs += xs;

                addNumber(writableSheet, xs, 3, row);
                //数量
                addNumber(writableSheet, outItem.stockOutQty, 4, row);

                addString(writableSheet, outItem.hsCode, 6, row);
//描述
               // addString(writableSheet, outItem.describe, 7, row);
                //fob

                addNumber(writableSheet, outItem.up, 8, row);

                //amount
                addNumber(writableSheet, FloatHelper.scale(outItem.up * outItem.stockOutQty), 9, row);

//                //Product size (inch)
//                addString(writableSheet, outItem.specInch, 8, row);


                row++;

            }


        }


        //添加上汇总行
        int totalStockOutQty = 0;
        float totalAmt = 0;
        for (int i = 0; i < dataSize; i++) {

            totalStockOutQty += items.get(i).stockOutQty;

            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up );

        }
        addString(writableSheet, totalXs + " /CTNS", 3, row);
        addString(writableSheet, totalStockOutQty + " /PCS", 4, row);
        addNumber(writableSheet, totalAmt, 9, row);

    }

    /**
     * 清单sheet数据
     */
    public void exportList(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(0);
        exportSheetHead(data, writableSheet);

        //所关联的所有订单号。
        Set<String> orders = new HashSet<>();
        //分类柜号 封签号
        Set<String> guihaoSet = new HashSet<>();
        Map<String, String> guihaoMap = new HashMap();
        Map<String, List<ErpStockOutItem>> groupMaps = new HashMap<>();

        for (ErpStockOutItem item : data.items) {

            String guihao = StringUtils.isEmpty(item.guihao) ? "" : item.guihao;
            guihaoSet.add(guihao);
            guihaoMap.put(guihao, item.fengqianhao);
            List<ErpStockOutItem> guiItems = groupMaps.get(guihao);
            if (guiItems == null) {
                guiItems = new ArrayList<>();
                groupMaps.put(guihao, guiItems);
            }
            orders.add(item.os_no);
            guiItems.add(item);

        }
        //所有唛头数据
        StringBuilder maitous = new StringBuilder();

        ApiManager apiManager = Guice.createInjector().getInstance(ApiManager.class);
        for (String os_no : orders) {

            try {
                RemoteData<ErpOrderDetail> remoteData = apiManager.getOrderDetail(os_no);
                if (remoteData.isSuccess()) {
                    String zhengmai = remoteData.datas.get(0).erpOrder.zhengmai;
                    if (!StringUtils.isEmpty(zhengmai))
                        maitous.append(zhengmai).append(",");
                }
            } catch (HdException e) {
                e.printStackTrace();
            }
        }

        if (maitous.length() > 1)
            maitous.setLength(maitous.length() - 1);
        //
        addString(writableSheet, maitous.toString(), 1, 11);

        //未进行分柜的 显示在前


        Set<String> keys = groupMaps.keySet();
        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 27;


        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize + keys.size());


        //订单体积
        float orderTiji = 0;
        //总提交  sum(体积*数量)
        float totalTiji = 0;

        int row = startItemRow;
        //总箱数目
        int totalXs = 0;
        //总毛重
        float totalmz = 0;
        //总净重
        float totaljz = 0;
        //总数量
        int totalStockOutQty = 0;

        for (String guihao : keys) {


            String fengqianhao = guihaoMap.get(guihao);
            //合并单元格
            combineRowAndCell(writableSheet, row, row, 0, 8);
            setCellAlignLeftCenter(workbook, writableSheet, row, 0);
            addString(writableSheet, StockStringHelper.generateCombineString(guihao, fengqianhao), 0, row);
            row++;
            List<ErpStockOutItem> groupItems = groupMaps.get(guihao);
            for (ErpStockOutItem outItem : groupItems) {

                addString(writableSheet, outItem.bat_no, 0, row);
                addString(writableSheet, outItem.prd_no, 1, row);
                addString(writableSheet, outItem.unit, 2, row);

                //addNumber(writableSheet, outItem.stockOutQty, 3, row);
                totalStockOutQty += outItem.stockOutQty;
                //单款箱数
                final int xs =outItem.so_zxs==0?0:( (outItem.stockOutQty - 1) / outItem.so_zxs + 1);
                totalXs += xs;

                addNumber(writableSheet, xs, 3, row);
                //数量
                addNumber(writableSheet, outItem.stockOutQty, 4, row);
//描述
                  addString(writableSheet, outItem.describe, 5, row);


                //包装相关
                addNumber(writableSheet, Float.valueOf(outItem.so_zxs), 6, row);

                addString(writableSheet, "/", 7, row);


                float[] xg = StringUtils.decouplePackageString(outItem.khxg);
                addNumber(writableSheet, xg[0], 8, row);
                addString(writableSheet, "*", 9, row);
                addNumber(writableSheet, xg[1], 10, row);
                addString(writableSheet, "*", 11, row);
                addNumber(writableSheet, xg[2], 12, row);


                orderTiji = outItem.xgtj * xs;

                //ttl cbm
                addNumber(writableSheet, FloatHelper.scale(orderTiji), 13, row);
                totalTiji += orderTiji;


                //净重 按产品个数量结算
                //ttl nw
//                addNumber(writableSheet, FloatHelper.scale(orderTiji), 13, row);


                final float zjz = outItem.jz1 * xs;
                totaljz += zjz;
                addNumber(writableSheet, FloatHelper.scale(zjz), 14, row);
//                //TTL G.W      毛重  按箱数结算
//                addNumber(writableSheet, FloatHelper.scale(outItem.mz), 15, row);
//
//                //TTL G.W       (kgs)
                final float zmz = outItem.mz * xs;
                totalmz += zmz;
                addNumber(writableSheet, FloatHelper.scale(zmz), 15, row);


//                //  cbm
//                addNumber(writableSheet, FloatHelper.scale(outItem.xgtj, 3), 17, row);
//                //ttl cbm
//                addNumber(writableSheet, FloatHelper.scale(orderTiji, 3), 18, row);

                //Product size (inch)
                addString(writableSheet, outItem.specInch, 16, row);


                row++;

            }


        }


        //多少箱子
        addString(writableSheet, totalXs + " /CTNS", 3, row);
        //多少数量
        addString(writableSheet, totalStockOutQty + " /PCS", 4, row);
//        addString(writableSheet, FloatHelper.scale(totalTiji, 3) + "CBM", 18, row);
//        addString(writableSheet, FloatHelper.scale(totaljz) + "KGS", 14, row);
//        addString(writableSheet, FloatHelper.scale(totalmz) + "KGS", 15, row);


        row++;
        addNumber(writableSheet, FloatHelper.scale(totalTiji, 3), 3, row);
        row++;
        addNumber(writableSheet, FloatHelper.scale(totaljz), 3, row);
        row++;
        addNumber(writableSheet, FloatHelper.scale(totalmz), 3, row);


    }


    private void exportItem(Sheet sheet, ErpStockOutItem item, int rowIndex) {

    }
}
