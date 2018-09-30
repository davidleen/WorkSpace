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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

/**
 * 导出情感发票单
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockOut_XK_Invoice extends SimpleExcelReporter<ErpStockOutDetail> {

    public static final String FILE_NAME = "咸康清单发票";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";
    int styleRow = 10;



    public Report_Excel_StockOut_XK_Invoice() {


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


    }


    /**
     * 导出sheet头部部分。三个sheet 头部一样
     */
    public void exportSheetHead(ErpStockOutDetail data, Sheet writableSheet) {
        //发票单号
        addString(writableSheet, "Invoice No.:" + data.erpStockOut.ck_no, 8, 4);
        //Po号
        addString(writableSheet, "PO#:" , 8, 5);
        // S/C NO:
        addString(writableSheet, "S/C NO:", 8, 6);
        //Date:
        addString(writableSheet, "Date:" + data.erpStockOut.ck_dd, 8, 7);
        //Country of origin:
       //   addString(writableSheet, "Country of origin:"+"FUZHOU", 10, 8);

        //客户信息

        addString(writableSheet, data.erpStockOut.fp_name, 0, 5);
        addString(writableSheet, data.erpStockOut.adr2, 0, 6);
        addString(writableSheet, data.erpStockOut.tel1, 0, 7);
        addString(writableSheet, data.erpStockOut.fax, 0, 8);


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

        addString(writableSheet, "PO#:" + StringUtils.combine(pos), 8, 5);

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
        int startItemRow = 25;


        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize + keys.size());


        int row = startItemRow;

        int totalXs = 0;
        for (String guihao : keys) {


            String fengqianhao = guihaoMap.get(guihao);
            //合并单元格
            combineRowAndCell(writableSheet, row, row, 0, 8);
            setCellAlignLeftCenter(workbook, writableSheet, row, 0);
            addString(writableSheet, guihao + "& seal # : " + fengqianhao, 0, row);
            row++;
            List<ErpStockOutItem> groupItems = groupMaps.get(guihao);
            for (ErpStockOutItem outItem : groupItems) {

                addString(writableSheet, outItem.bat_no, 0, row);
                addString(writableSheet, outItem.prd_no, 1, row);
                addString(writableSheet, outItem.cus_os_no, 2, row);
                addString(writableSheet, outItem.unit, 3, row);


                //单款箱数
                final int xs = (outItem.stockOutQty - 1) / outItem.so_zxs + 1;
                totalXs += xs;

                addNumber(writableSheet, xs, 4, row);
                //数量
                addNumber(writableSheet, outItem.stockOutQty, 5, row);

                addString(writableSheet, outItem.hsCode, 7, row);
//描述
                addString(writableSheet, outItem.describe, 8, row);
               addString(writableSheet, outItem.jmcc, 9, row);
                //fob

                addNumber(writableSheet, outItem.up, 10, row);

                //amount
                addNumber(writableSheet, FloatHelper.scale(outItem.up * outItem.stockOutQty), 11, row);

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
        addString(writableSheet, totalXs + " /CTNS", 4, row);
        addString(writableSheet, totalStockOutQty + " /PCS", 5, row);
        addNumber(writableSheet, totalAmt, 11, row);

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
        int startItemRow = 25;


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
            addString(writableSheet, guihao + "& seal # : " + fengqianhao, 0, row);
            row++;
            List<ErpStockOutItem> groupItems = groupMaps.get(guihao);
            for (ErpStockOutItem outItem : groupItems) {

                addString(writableSheet, outItem.bat_no, 0, row);
                addString(writableSheet, outItem.prd_no, 1, row);
                addString(writableSheet, outItem.cus_os_no, 2, row);
                addString(writableSheet, outItem.os_no, 3, row);
                addString(writableSheet, outItem.unit, 4, row);

                //addNumber(writableSheet, outItem.stockOutQty, 3, row);
                totalStockOutQty += outItem.stockOutQty;
                //单款箱数
                final int xs = (outItem.stockOutQty - 1) / outItem.so_zxs + 1;
                totalXs += xs;

                addNumber(writableSheet, xs, 5, row);
                //数量
                addNumber(writableSheet, outItem.stockOutQty, 6, row);
//描述
                  addString(writableSheet, outItem.describe, 7, row);


                //包装相关
                addNumber(writableSheet, Float.valueOf(outItem.so_zxs), 8, row);

                addString(writableSheet, "/", 9, row);


                float[] xg = StringUtils.decouplePackageString(outItem.khxg);
                addNumber(writableSheet, xg[0], 10, row);
                addString(writableSheet, "*", 11, row);
                addNumber(writableSheet, xg[1], 12, row);
                addString(writableSheet, "*", 13, row);
                addNumber(writableSheet, xg[2], 14, row);



                //  cbm
                addNumber(writableSheet, FloatHelper.scale(outItem.xgtj), 15, row);
                //nw
                addNumber(writableSheet, FloatHelper.scale(outItem.jz1), 16, row);
                addNumber(writableSheet, FloatHelper.scale(outItem.mz), 17, row);
                orderTiji = outItem.xgtj * xs;


                //ttl cbm
                addNumber(writableSheet, FloatHelper.scale(orderTiji), 18, row);
                totalTiji += orderTiji;





                final float zjz = outItem.jz1 * xs;
                totaljz += zjz;
                addNumber(writableSheet, FloatHelper.scale(zjz), 19, row);
//                //TTL G.W      毛重  按箱数结算
//                addNumber(writableSheet, FloatHelper.scale(outItem.mz), 15, row);
//
//                //TTL G.W       (kgs)
                final float zmz = outItem.mz * xs;
                totalmz += zmz;
                addNumber(writableSheet, FloatHelper.scale(zmz), 20, row);


//                //  cbm
//                addNumber(writableSheet, FloatHelper.scale(outItem.xgtj, 3), 17, row);
//                //ttl cbm
//                addNumber(writableSheet, FloatHelper.scale(orderTiji, 3), 18, row);

                //Product size (inch)
                addString(writableSheet, outItem.specInch, 21, row);


                row++;

            }


        }


        //多少箱子
        addString(writableSheet, totalXs + " /CTNS", 5, row);
        //多少数量
        addString(writableSheet, totalStockOutQty + " /PCS", 6, row);
        addString(writableSheet, FloatHelper.scale(totalTiji, 3) + "CBM", 18, row);
        addString(writableSheet, FloatHelper.scale(totaljz) + "KGS", 19, row);
        addString(writableSheet, FloatHelper.scale(totalmz) + "KGS", 20, row);


        row++;
        addNumber(writableSheet, FloatHelper.scale(totalTiji, 3), 5, row);
        row++;
        addNumber(writableSheet, FloatHelper.scale(totaljz), 5, row);
        row++;
        addNumber(writableSheet, FloatHelper.scale(totalmz), 5, row);


    }


    private void exportItem(Sheet sheet, ErpStockOutItem item, int rowIndex) {

    }
}
