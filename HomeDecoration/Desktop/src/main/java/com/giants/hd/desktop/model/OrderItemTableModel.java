package com.giants.hd.desktop.model;

import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

import static com.giants.hd.desktop.local.ConstantData.COLUMN_INDEX;

/**
 * 订单表格模型
 */

public class OrderItemTableModel extends BaseTableModel<ErpOrderItem> {


    public static final String TITLE_VERIFY_DATE = "验货日期";
    public static final String TITLE_SEND_DATE = "出柜日期";
    public static final String TITLE_PACKAGE_INFO = "包装信息";
    public static final String TITLE_MAITOU = "唛头";
    public static final String TITLE_GUAGOU = "挂钩说明";
    public static String[] columnNames = new String[]{"序号", "图片", "货号", "配方号", "客号", TITLE_VERIFY_DATE, TITLE_SEND_DATE, TITLE_PACKAGE_INFO, "包装图片", TITLE_MAITOU, TITLE_GUAGOU, "单位", "单价", "数量", "金额", "箱数", "每箱数", "箱规", "立方数", "总立方数", "产品尺寸", "生产进度", "备注"};
    public static int[] columnWidth = new int[]{40, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 60, 60, 100, 100, 100, 100, 100, 150, 100, 40, 40, 40, 80, 120, 60, 120, 120, 120, 120, 120, 400};


    public static final String VERIFY_DATE = "verifyDate";
    public static final String SEND_DATE = "sendDate";
    public static final String PACKAGE_INFO = "packageInfo";
    public static final String MAITOU = "maitou";
    public static final String GUAGOU = "guagou";

    public static final String PACK_ATTACHES = "packAttaches";
    public static final String UP = "up";
    public static final String AMT = "amt";
    private static final String DESCRIBE = "workFlowDescribe";
    public static String[] fieldName = new String[]{COLUMN_INDEX, "thumbnail", "prd_no", "pVersion", "bat_no", VERIFY_DATE, SEND_DATE, PACKAGE_INFO, PACK_ATTACHES, MAITOU, GUAGOU, "ut", UP, "qty", AMT, "htxs", "so_zxs", "khxg", "xgtj", "zxgtj", "hpgg", DESCRIBE, "memo"};

    public static Class[] classes = new Class[]{Object.class, ImageIcon.class, Object.class, Object.class, Object.class, String.class, String.class, String.class, ImageIcon.class};
    /**
     * 单价是否可见
     */
    private boolean fobPriceVisible;


    @Inject
    public OrderItemTableModel() {
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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        ErpOrderItem item = getItem(rowIndex);

        //订单明细是不许增加记录的  所有空行都为不可编辑
        if (StringUtils.isEmpty(item.os_no)) {
            return false;
        }

        if (columnIndex == StringUtils.index(fieldName, VERIFY_DATE))
            return true;
        if (columnIndex == StringUtils.index(fieldName, SEND_DATE))
            return true;
        if (columnIndex == StringUtils.index(fieldName, PACKAGE_INFO))
            return true;
        if (columnIndex == StringUtils.index(fieldName, MAITOU))
            return true;
        if (columnIndex == StringUtils.index(fieldName, GUAGOU))
            return true;


        return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {

        ErpOrderItem item = getItem(rowIndex);
        if (item == null) return null;


        if (columnIndex == StringUtils.index(fieldName, UP) || columnIndex == StringUtils.index(fieldName, AMT)) {

            if (!fobPriceVisible) return "***";
        }
        if (columnIndex == StringUtils.index(fieldName, DESCRIBE)) {

            Object value = super.getValueAt(rowIndex, columnIndex);
            if (value==null||StringUtils.isEmpty(value.toString())) {
                return "未排产";
            }
            return value;
        }


        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        ErpOrderItem item = getItem(rowIndex);
        if (item == null) return;
        boolean update = false;
        if (columnIndex == StringUtils.index(fieldName, PACKAGE_INFO)) {


            item.packageInfo = String.valueOf(aValue);
            update = true;


        }
        if (columnIndex == StringUtils.index(fieldName, MAITOU)) {
            item.maitou = String.valueOf(aValue);
            update = true;


        }
        if (columnIndex == StringUtils.index(fieldName, GUAGOU)) {
            item.guagou = String.valueOf(aValue);
            update = true;


        }


        if (columnIndex == StringUtils.index(fieldName, VERIFY_DATE)) {
            item.verifyDate = String.valueOf(aValue);
            update = true;


        }

        if (columnIndex == StringUtils.index(fieldName, SEND_DATE)) {
            item.sendDate = String.valueOf(aValue);
            update = true;
        }
        if (update)
            fireTableCellUpdated(rowIndex, columnIndex);
        super.setValueAt(aValue, rowIndex, columnIndex);
    }


    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName, PACKAGE_INFO), ArrayUtils.indexOnArray(fieldName, MAITOU), ArrayUtils.indexOnArray(fieldName, GUAGOU)};
    }


//    //异步加载的图片缓存
//    private HashMap<String,ImageIcon> pictureMaps = new HashMap<>();


    /**
     * 设置单价是否可见
     *
     * @param visible
     */
    public void setFobPriceVisible(boolean visible) {
        this.fobPriceVisible = visible;
    }
}
