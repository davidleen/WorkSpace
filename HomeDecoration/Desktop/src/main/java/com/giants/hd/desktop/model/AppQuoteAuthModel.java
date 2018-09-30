package com.giants.hd.desktop.model;

import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.app.AppQuoteAuth;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  报价权限表格数据模型
 */
public class AppQuoteAuthModel extends  BaseTableModel<AppQuoteAuth> {

    public static String[] columnNames = new String[]{    "用户名称","查看成本", "查看FOB", "修改FOB","只能查看自己"   };
    public static int[] columnWidth=new int[]{   200,    40,40,   40 , 40  };

    public static String[] fieldName = new String[]{ "user","costVisible", "fobVisible",  "fobEditable","limitSelf" };

    public  static Class[] classes = new Class[]{Object.class,Boolean.class, Boolean.class, Boolean.class, Boolean.class ,Boolean.class, Boolean.class,Boolean.class };


    @Inject
    public AppQuoteAuthModel() {
        super(columnNames, fieldName, classes, AppQuoteAuth.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if(columnIndex==0) return false;

        return true;
    }



    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        AppQuoteAuth authority=getItem(rowIndex);
        boolean booleanValue=false;
        try {
            booleanValue=Boolean.valueOf(aValue.toString().trim());
        }catch (Throwable t){}


        switch (columnIndex)
        {
            case 1:
                authority.costVisible=booleanValue;
                break;
            case 2:
            authority.fobVisible=booleanValue;

            break;
            case 3:
                authority.fobEditable=booleanValue;
                break;

            case 4:
                authority.limitSelf=booleanValue;
                break;
        }

        fireTableCellUpdated(rowIndex,columnIndex);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }
    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }
}
