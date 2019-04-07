package com.giants.hd.desktop.model;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 出库单细项表格模型
 */

public class StockOutItemTableModel extends BaseTableModel<ErpStockOutItem> {

    public static final String DESCRIBE = "describe";
    public static String[] columnNames = new String[]{ "序号","图片",                                "货号",       "版本号",    "产品描述"  ,    "柜号     ","   封签号"   , "柜型"   ,"合同号", "客号",    "客号订单号", "单位","单价","数量" ,  "金额"  ,"每箱套数",  "箱数"    ,"箱规"    ,"箱规体积" ,"总体积",  "净重" ,"毛重",};
    public static int[] columnWidth=new int[]{       60, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH,    120,          120,            200,           160,      160,               120,    120,       100,       100        ,  40 ,     60,   40 ,       60       ,40  ,         40    ,   120      ,    60       ,     60    ,      40  ,40   };

    public static final String KHXG = "khxg";
    public static final String GUIHAO = "guihao";
    public static final String FENGQIANHAO = "fengqianhao";
    public static final String UP = "up";
    public static final String AMT = "amt";
    public static final String XS = "xs";
    public static final String ZXGTJ = "zxgtj";
    public static String[] fieldName = new String[]{  "itm", "thumbnail",                                   "prd_no",  "pVersion",    DESCRIBE, GUIHAO,FENGQIANHAO,            "guixing" ,"os_no",   "bat_no","cus_os_no", "unit"  , UP,  "stockOutQty", AMT, "so_zxs"     , XS, KHXG,    "xgtj", ZXGTJ,    "jz1",  "mz"   };

    public  static Class[] classes = new Class[]{Object.class,ImageIcon.class,                       Object.class,   Object.class, Object.class,String.class,String.class,  Object.class};


    public int[] multiLineColumn=new int[]{StringUtils.index(fieldName,KHXG)};

    /**
     * 是否可以查看价格
     */
    private boolean priceVisible;

    @Inject
    public StockOutItemTableModel() {
        super(columnNames,fieldName,classes,ErpStockOutItem.class);
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

        if(columnIndex==StringUtils.index(fieldName,DESCRIBE)) {


            if(!StringUtils.isEmpty(getItem(rowIndex).ps_no))
                 return true;

        }


        return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        ErpStockOutItem item=getItem(rowIndex);

        if (columnIndex == StringUtils.index(fieldName, UP)||columnIndex == StringUtils.index(fieldName, AMT)) {

            if(!priceVisible) return "***";
        }


        if(columnIndex==StringUtils.index(fieldName,XS))
        {
            return item.so_zxs==0?0: item.stockOutQty/item.so_zxs;
        }
        if(columnIndex==StringUtils.index(fieldName,ZXGTJ))
        {
            return item.so_zxs==0?0:FloatHelper.scale(item.stockOutQty/item.so_zxs*item.xgtj);
        }
        if(columnIndex==StringUtils.index(fieldName,AMT))
        {
            return FloatHelper.scale(item.stockOutQty*item.up);
        }
        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        if(columnIndex==StringUtils.index(fieldName,DESCRIBE))
        {

         ErpStockOutItem  item=     getItem(rowIndex);
            if(item!=null)
            {
                item.describe=String.valueOf(aValue);
                fireTableCellUpdated(rowIndex,columnIndex);
            }

        }
        if(columnIndex==StringUtils.index(fieldName,GUIHAO+FENGQIANHAO))
        {
            ErpStockOutItem item=getItem(rowIndex)                    ;
            if(item!=null&&aValue instanceof StockOutDetailFrame.GuiInfo)
            {
                StockOutDetailFrame.GuiInfo guiInfo=(StockOutDetailFrame.GuiInfo)aValue;
                item.guihao=guiInfo.guihao;
                item.fengqianhao=guiInfo.fengqianhao;
                item.guixing=guiInfo.guixing;
                fireTableRowsUpdated(rowIndex,rowIndex);

            }


        }

        super.setValueAt(aValue, rowIndex, columnIndex);
    }


    @Override
    public int[] getMultiLineColumns() {
        return multiLineColumn;
    }

    public void setPriceVisible(boolean priceVisible) {
        this.priceVisible = priceVisible;
    }
}
