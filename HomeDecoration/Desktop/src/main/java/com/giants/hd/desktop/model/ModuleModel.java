package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity.Module;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  业务员表格数据模型
 */
public class ModuleModel extends  BaseTableModel<Module> {

    public static String[] columnNames = new String[]{                         "名称 ",  "标题"  ,"" };
    public static int[] columnWidth=new int[]{   100,        120 , ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ "name", "title",  " "};

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class };


    @Inject
    public ModuleModel() {
        super(columnNames, fieldName, classes, Module.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return false;
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
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }
}
