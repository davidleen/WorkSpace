package com.giants.hd.desktop.model;

import com.giants3.hd.entity.QuotationDelete;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 已经删除的产品表格模型
 */

public class QuotationDeleteModel extends BaseTableModel<QuotationDelete> {





    public static String[] columnNames = new String[]{"报价单号", "业务员","客户","删除人", "删除人", "删除日期" };
    public static int[] columnWidth=new int[]{ 120, 60, 60, 100,100,160 };
    public static String[] fieldName = new String[]{"qNumber", "saleMan",  "customer","userName","userCName","timeString"};

    public  static Class[] classes = new Class[]{ };



    @Inject
    public QuotationDeleteModel() {
        super(columnNames,fieldName,classes,QuotationDelete.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }





}
