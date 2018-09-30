package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 订单生产流程表格模型
 */

public class OrderItemWorkFlowTableModel extends BaseTableModel<ErpOrderItem> {


    public static String[] columnNames = new String[]{"序号", "图片","订单号" ,"货号", "配方号", "客号", "单位", "订单数量", "当前数量", "生产进度", "备注"};
    public static int[] columnWidth = new int[]{40, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 60, 60, 60, 100, 40, 80,80, 120, 400};


    public static String[] fieldName = new String[]{"itm", "thumbnail", "os_no","prd_no", "pVersion", "bat_no", "ut", "qty","tranQty", "currentWorkFlow", "memo"};

    public static Class[] classes = new Class[]{Object.class, ImageIcon.class, Object.class, Object.class, Object.class, String.class, String.class, String.class  };
    /**
     * 单价是否可见
     */
    private boolean fobPriceVisible;


    @Inject
    public OrderItemWorkFlowTableModel() {
        super(columnNames, fieldName, classes, ErpOrderItem.class);
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
