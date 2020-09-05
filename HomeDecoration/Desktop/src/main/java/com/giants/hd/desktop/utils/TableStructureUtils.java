package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.model.TableField;
import com.giants3.hd.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * 表格结构配置工具类。
 * Created by davidleen29 on 2017/4/2.
 */
public class TableStructureUtils {

    public static final String DIRECTORY = "tableStructure/";

    public static final Type typeToken = new TypeToken<List<TableField>>() {
    }.getType();

    public  static List<TableField> fromJson(String jsonFileName) {

        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream = TableStructureUtils.class.getClassLoader().getResourceAsStream(DIRECTORY + jsonFileName);
        List<TableField> tableFields = GsonUtils.fromInputStreamUTF8(inputStream, typeToken);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return tableFields;
    }


    public static List<TableField> getWorkFlowArrange() {


        return fromJson("workFlowArrange.json");

    }

    public static List<TableField> getWorkFlowWorker() {
        return fromJson("workFlowWorker.json");
    }
    public static List<TableField> getWorkFlowArranger() {
        return fromJson("workFlowArranger.json");
    }
    public static List<TableField> getQuotationPictureModel() {
        return fromJson("quotationPicture.json");
    }
    public static List<TableField> getAppQuotation() {
        return fromJson("appQuotation.json");
    }
public static List<TableField> getWorkFlowMessage() {
        return fromJson("workFlowMessage.json");
    }

    public static List<TableField> getAppQuotationItem() {
        return fromJson("appQuotationItem.json");
    }

    public static List<TableField> getAppQuoteCountReport() {


        return fromJson("appQuotationCountReport.json");

    }

    public static List<TableField> getProductValueHistoryTable() {
        return fromJson("productValueHistory.json");
    }
}
