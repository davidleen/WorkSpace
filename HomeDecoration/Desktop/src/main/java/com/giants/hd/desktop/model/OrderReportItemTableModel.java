package com.giants.hd.desktop.model;

import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.noEntity.OrderReportItem;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 订单报表表格模型
 */

public class OrderReportItemTableModel extends BaseTableModel<OrderReportItem> {


    public static String[] columnNames = new String[]{"客户", "合同号 ", "货号", "图片", "客号", "单位", "数量", "验货日期", "出柜日期", "业务员"};
    public static int[] columnWidth = new int[]{80, 100, 80, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 80, 60, 60, 100, 100, 80};


    public static String[] fieldName = new String[]{"cus_no", "os_no", "prd_no", "thumbnail", "cus_prd_no", "unit", "qty", "verifyDate", "sendDate", "saleName"};

    public static Class[] classes = new Class[]{Object.class, Object.class, Object.class, ImageIcon.class, String.class, String.class, String.class, String.class};
    /**
     * 单价是否可见
     */
    private boolean fobPriceVisible;


    @Inject
    public OrderReportItemTableModel() {
        super(columnNames, fieldName, classes, OrderReportItem.class);
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
