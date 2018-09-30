package com.giants.hd.desktop.model;

import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 出库单表格模型
 */

public class StockOutTableModel extends BaseTableModel<ErpStockOut> {


    public static String[] columnNames = new String[]{  "出库单","出库日期","客户","业务员", "目的港", "提单号", "柜数柜型" };
    public static int[] columnWidth=new int[]{   120, 120,60,120,100,100, 60 };
    public static String[] fieldName = new String[]{  "ck_no",  "ck_dd","cus_no","sal_cname","mdg","tdh", "gsgx" };

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class,Object.class, Object.class, Object.class, Object.class};



    @Inject
    public StockOutTableModel() {
        super(columnNames,fieldName,classes,ErpStockOut.class);
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
