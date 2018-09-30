package com.giants.hd.desktop.model;

import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 销库单表格模型  比  StockInAndSubmitTableModel 增加显示一些字段
 */

public class StockOutSubmitTableModel extends BaseTableModel<StockSubmit> {


    public static String[] columnNames = new String[]{"柜号","柜编号",  "日期","单号" ,"订单号","客户","产品编号","产品名称","客号", "客户订单号", "箱子规格", "装箱数","总箱数","总体积","区域","单价","总金额","厂家" };
    public static int[] columnWidth=new int[]{         60,      120,      100    ,120,     120,     80,     120,       120,        100,    140,            100,        80,       80 ,      120,     80,     80,     80,     80};
    private static final String XHDG = "xhdg";
    public static String[] fieldName = new String[]{ XHDG,"xhgh" ,"dd",  "no","so_no", "cus_no", "prd_no","prd_name","bat_no","cus_os_no","khxg", "so_zxs","xs","zxgtj","area","price","cost","dept" };

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class,Object.class, Object.class, Object.class, Object.class};



    @Inject
    public StockOutSubmitTableModel() {
        super(columnNames,fieldName,classes,StockSubmit.class);
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




        Object o= super.getValueAt(rowIndex, columnIndex);

        if(XHDG.equals(fieldName[columnIndex]))
        {

            try{

                return Float.valueOf(o.toString()).intValue();
            }catch (Throwable t)
            {
                t.printStackTrace();
            }
        }

        return o;
    }




}
