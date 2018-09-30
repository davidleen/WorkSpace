package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 流程任务
 */
public class OrderItemWorkFlowStateModel extends BaseTableModel<ErpOrderItemProcess> {

    public static String[] columnNames = new String[]{  "订单号", "货号", "版本号", "单号", "生产厂家", "生产属性", "订单数量", "排厂数量", "已发送数量"};
    public static int[] columnWidth = new int[]{  120, 120, 120, 120, 120, 120, 120, 100, 100, 100};

    public static String[] fieldName = new String[]{  "osNo", "prdNo", "pVersion", "mrpNo", "jgh", "scsx", "orderQty", "qty", "sentQty"};
    public static Class[] classes = new Class[]{Object.class, Object.class, Object.class};


    @Inject
    public OrderItemWorkFlowStateModel() {
        super(columnNames, fieldName, classes, ErpOrderItemProcess.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }
}
