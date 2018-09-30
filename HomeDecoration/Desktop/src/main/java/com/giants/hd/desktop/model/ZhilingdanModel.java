package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity_erp.Zhilingdan;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 定时任务数据模型
 */
public class ZhilingdanModel extends BaseTableModel<Zhilingdan> {

    public static String[] columnNames = new String[]{"序号","单号 ", "日期", "订单号","货号", "品号", "品名"," prd_mark ", "定制数量", "采购单号",  "采购数量", "采购日期","采购前置期", "进货单号","进货数量", "进货日期", "进货前置期", };
    public static int[] columnWidth = new int[]{40,120, 100, 120,100, 100,100, 80,80, 80,100, 100, 80, 100,100, 60, 60};

    public static final String REPEAT_COUNT = "repeatCount";
    private static final String CAIGOU_QTY = "caigouQty";
    private static final String CAIGOU_DD = "caigou_dd";
    private static final String NEED_DD = "need_dd";
    private static final String CAIGOU_NO = "caigou_no";
    private static final String JINHUO_NO = "jinhuo_no";
    private static final String JINHUO_QTY = "jinhuoQty";
    private static final String JINHUO_DD = "jinhuo_dd";
    private static final String NEED_DAYS = "need_days";
    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"mo_no", "mo_dd", "os_no", "real_prd_name","prd_no","prd_name",  "prd_mark", "qty_rsv", CAIGOU_NO, CAIGOU_QTY, CAIGOU_DD, NEED_DD, JINHUO_NO, JINHUO_QTY, JINHUO_DD, NEED_DAYS};

    public static Class[] classes = new Class[]{Object.class, Object.class, Object.class};


    @Inject
    public ZhilingdanModel() {
        super(columnNames, fieldName, classes, Zhilingdan.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return false;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }


    public boolean  isCaigouField(int coloumIndex)
    {
        return   fieldName[coloumIndex].equals(CAIGOU_QTY)
                ||fieldName[coloumIndex].equals(CAIGOU_DD)
                ||fieldName[coloumIndex].equals(CAIGOU_NO)
                ||fieldName[coloumIndex].equals(NEED_DD);

    }


    public boolean  isJinhuoField(int coloumIndex)
    {
        return   fieldName[coloumIndex].equals(JINHUO_QTY)
                ||fieldName[coloumIndex].equals(JINHUO_DD)
                ||fieldName[coloumIndex].equals(JINHUO_NO)
                ||fieldName[coloumIndex].equals(NEED_DAYS);

    }
}
