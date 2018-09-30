package com.giants.hd.desktop.model;

import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 订单表格模型
 */

public class OrderItemArrangeTableModel extends BaseTableModel<ErpOrderItem> {

    public static final String UP = "up";
    public static final String AMT = "amt";
    private static final String DESCRIBE = "workFlowDescribe";
    public static String[] columnNames = new String[]{"序号", "图片", "订单号", "货号", "配方号", "客号", "单位", "数量", "产品尺寸", "生产进度", "备注", "操作","生产类型"};
    public static int[] columnWidth = new int[]{40, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 120, 120, 100, 100, 60, 60, 100, 100, 150, 60,80};
    private static final String OPERATE = "operate";
    private static final String PRODUCE_TYPE = "produceType";
    public static String[] fieldName = new String[]{"itm", "thumbnail", "os_no", "prd_no", "pVersion", "bat_no", "ut", "qty", "hpgg", DESCRIBE, "memo", OPERATE, PRODUCE_TYPE};

    public static Class[] classes = new Class[]{Object.class, ImageIcon.class, Object.class, Object.class, Object.class, String.class, String.class, String.class,String.class,String.class,String.class, String.class,String.class};




    @Inject
    public OrderItemArrangeTableModel() {
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
    public Object getValueAt(final int rowIndex, final int columnIndex) {

        ErpOrderItem item = getItem(rowIndex);
        if (item == null) return null;


        if (columnIndex == StringUtils.index(fieldName, DESCRIBE)) {

            Object value = super.getValueAt(rowIndex, columnIndex);
            if (value == null || StringUtils.isEmpty(value.toString())) {
                return "未排产";
            }
            return value;
        }
        if (columnIndex == StringUtils.index(fieldName, OPERATE)) {

            if(!StringUtils.isEmpty(item.workFlowDescribe))
            {
                return "已经排厂";
            }



            return "可以排厂";

        } if (columnIndex == StringUtils.index(fieldName, PRODUCE_TYPE)) {


            switch (item.produceType) {
                case ProduceType.SELF_MADE:
                    return "自制";
                case ProduceType.NOT_SET:
                    return "未设置";
                default:
                    return "外购";
            }



        }


        return super.getValueAt(rowIndex, columnIndex);
    }


}
