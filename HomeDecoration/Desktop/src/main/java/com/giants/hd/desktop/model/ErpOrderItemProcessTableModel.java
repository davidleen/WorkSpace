package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 订单款项的排厂列表
 */

public class ErpOrderItemProcessTableModel extends BaseTableModel<ErpOrderItemProcess> {


    public static String[] columnNames = new String[]{"序号", "单号", "排厂日期", "预计开工期", "预计完工期", "排厂特征", "生产厂家", "生产属性", "数量"};
    public static int[] columnWidth = new int[]{40, 120, 120, 120, 120, 100, 100, 60, 100, 100, 150, 60, 80};

    public static String[] fieldName = new String[]{"itm", "moNo", "moDd", "staDd", "endDd", "mrp_No", "jgh", "scsx", "qty"};

    public static Class[] classes = new Class[]{Object.class, String.class, Object.class, Object.class, Object.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class};


    @Inject
    public ErpOrderItemProcessTableModel() {
        super(columnNames, fieldName, classes, ErpOrderItemProcess.class);
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
