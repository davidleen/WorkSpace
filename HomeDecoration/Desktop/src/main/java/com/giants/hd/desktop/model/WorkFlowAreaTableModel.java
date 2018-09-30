package com.giants.hd.desktop.model;

import com.giants3.hd.entity.WorkFlowArea;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 客户路表格数据模型
 */
public class WorkFlowAreaTableModel extends  BaseTableModel<WorkFlowArea> {

    public static String[] columnNames = new String[]{           "编号",         "名称"  ,"描述" };
    public static int[] columnWidth=new int[]{      100,  200 ,200  };

    public static String[] fieldName = new String[]{  "code",  "name" ,"description"};

    public  static Class[] classes = new Class[]{Object.class,Object.class  };


    @Inject
    public WorkFlowAreaTableModel( ) {
        super(columnNames, fieldName, classes, WorkFlowArea.class);
        setRowAdjustable(true);
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
