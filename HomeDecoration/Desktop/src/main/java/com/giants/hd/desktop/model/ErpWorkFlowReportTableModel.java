package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 订单款项的排厂列表
 */

public class ErpWorkFlowReportTableModel extends BaseTableModel<ErpWorkFlowReport> {


    public static String[] columnNames = new String[]{"序号", "流程编号", "流程类型", "完成百分比"};
    public static int[] columnWidth = new int[]{40, 120, 120, 120};

    public static String[] fieldName = new String[]{"itm", "workFlowCode", "workFlowName", "percentage"};

    public static Class[] classes = new Class[]{Object.class, String.class, Object.class, Object.class};

    @Inject
    public ErpWorkFlowReportTableModel() {
        super(columnNames, fieldName, classes, ErpWorkFlowReport.class);
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
