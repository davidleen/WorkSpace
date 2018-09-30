package com.giants.hd.desktop.model;

import com.giants3.hd.entity.WorkFlowTimeLimit;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 客户路表格数据模型
 */
public class WorkFlowTimeLimitModel extends BaseTableModel<WorkFlowTimeLimit> {

    public static String[] columnNames = new String[]{"订单货款类型", "期限", "预警", "期限", "预警","期限", "预警", "期限", "预警", "期限", "预警", "期限", "预警", "期限", "预警", "期限", "预警"};
    public static int[] columnWidth = new int[]{100, 40, 40, 40, 40, 40, 40, 40, 40, 40,40, 40, 40, 40, 40, 40, 40};

    public static String[] fieldName = new String[]{"orderItemTypeName", "limit_mu_baipeijg", "alert_mu_baipeijg", "limit_tie_baipeijg", "alert_tie_baipeijg","limit_mu_baipei", "alert_mu_baipei", "limit_tie_baipei", "alert_tie_baipei", "limit_mu_yanse", "alert_mu_yanse", "limit_tie_yanse", "alert_tie_yanse", "limit_baozhuang", "alert_baozhuang", "limit_tie_baozhuang", "alert_tie_baozhuang"};

    public static Class[] classes = new Class[]{Object.class, Object.class};


    @Inject
    public WorkFlowTimeLimitModel() {
        super(columnNames, fieldName, classes, WorkFlowTimeLimit.class);
        setRowAdjustable(false);

    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        if (columnIndex <= 0) return;


        WorkFlowTimeLimit limit = getItem(rowIndex);
        int day;
        try {
            day = Integer.valueOf(aValue.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        switch (columnIndex) {
            case 1:
                limit.limit_mu_baipeijg = day;
                break;
            case 2:
                limit.alert_mu_baipeijg = day;
                break;
            case 3:
                limit.limit_tie_baipeijg = day;
                break;
            case 4:
                limit.alert_tie_baipeijg = day;
                break;
            case 5:
                limit.limit_mu_baipei = day;
                break;
            case 6:
                limit.alert_mu_baipei = day;
                break;
            case 7:
                limit.limit_tie_baipei = day;
                break;
            case 8:
                limit.alert_tie_baipei = day;
                break;
            case 9:
                limit.limit_mu_yanse = day;
                break;
            case 10:
                limit.alert_mu_yanse = day;
                break;
            case 11:
                limit.limit_tie_yanse = day;
                break;
            case 12:
                limit.alert_tie_yanse = day;
                break;
            case 13:
                limit.limit_baozhuang = day;
                break;
            case 14:
                limit.alert_baozhuang = day;
                break;
            case 15:
                limit.limit_tie_baozhuang = day;
                break;
            case 16:
                limit.alert_tie_baozhuang = day;
                break;


        }
        fireTableCellUpdated(rowIndex, columnIndex);

    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if (columnIndex > 0) return true;
        return super.isCellEditable(rowIndex, columnIndex);
    }

    /**
     * 返回 设置的行高
     *
     * @return
     */
    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }
}
