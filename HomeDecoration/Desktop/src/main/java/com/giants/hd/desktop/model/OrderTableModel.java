package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 订单表格模型
 */

public class OrderTableModel extends BaseTableModel<ErpOrder> {




    public static String[] columnNames = new String[]{"合同日期", "合同编号","客户名称","客户订单号", "业务员",  "姓名",             "预计交期", "生产交期", "备注" };
    public static int[] columnWidth=new int[]{       120,            120,         120,       100,            60,      100,                        120,         120,   ConstantData.MAX_COLUMN_WIDTH};
    public static String[] fieldName = new String[]{"os_dd",       "os_no",  "cus_no",  "cus_os_no", "sal_name", "sal_cname",         "est_dd",    "so_data", "rem" };

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};



    @Inject
    public OrderTableModel() {
        super(columnNames,fieldName,classes,ErpOrder.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }




//
}
