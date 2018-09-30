package com.giants.hd.desktop.model;

import com.giants3.hd.entity.StockOutAuth;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  业务员表格数据模型
 */
public class StockOutAuthModel extends  BaseTableModel<StockOutAuth> {

    public static String[] columnNames = new String[]{    "用户名称",  "查看单价"    };
    public static int[] columnWidth=new int[]{   200,    40  };

    public static String[] fieldName = new String[]{ "user", "fobVisible"  };

    public  static Class[] classes = new Class[]{Object.class,Boolean.class  };


    @Inject
    public StockOutAuthModel() {
        super(columnNames, fieldName, classes, StockOutAuth.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if(columnIndex==0) return false;

        return true;
    }



    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        StockOutAuth authority=getItem(rowIndex);
        boolean booleanValue=false;
        try {
            booleanValue=Boolean.valueOf(aValue.toString().trim());
        }catch (Throwable t){}


        switch (columnIndex)
        {
            case 1:
                authority.fobVisible=booleanValue;
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
