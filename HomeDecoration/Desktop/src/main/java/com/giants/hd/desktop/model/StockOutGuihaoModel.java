package com.giants.hd.desktop.model;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 出库柜号表格数据模型
 */
public class StockOutGuihaoModel extends BaseTableModel<StockOutDetailFrame.GuiInfo> {

    public static String[] columnNames = new String[]{"   柜号 ", "  封签号  ","柜型"};
    public static int[] columnWidth = new int[]{80, 80,100};


    public static String[] fieldName = new String[]{"guihao", "fengqianhao","guixing"};

    public static Class[] classes = new Class[]{Object.class, Object.class};


    @Inject
    public StockOutGuihaoModel() {
        super(columnNames, fieldName, classes, StockOutDetailFrame.GuiInfo.class);
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT / 2;
    }
}
