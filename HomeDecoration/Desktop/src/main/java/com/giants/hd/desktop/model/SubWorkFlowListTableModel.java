package com.giants.hd.desktop.model;

import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 出库单表格模型
 */

public class SubWorkFlowListTableModel extends BaseTableModel<Sub_workflow_state> {


    public static String[] columnNames = new String[]{ "生产通知单号","制令单号", "订单号","货号" ,"完工时间","工序代码", "工序名称", "数量" ,"工序日期" };
    public static int[] columnWidth=new int[]{   120, 120,120,120,120,120,120,120,120 };
    public static String[] fieldName = new String[]{  "tz_no",  "mo_no", "os_no",  "mrp_no" ,"completeDate","zc_no","zc_name","qty" ,"tz_dd" };

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class,Object.class, Object.class, Object.class };



    @Inject
    public SubWorkFlowListTableModel() {
        super(columnNames,fieldName,classes,Sub_workflow_state.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }



    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {




        return super.getValueAt(rowIndex, columnIndex);
    }




}
