package com.giants.hd.desktop.model;

import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import java.util.List;

/**
 * 客户路表格数据模型
 */
public class OutFactoryModel extends  BaseTableModel<OutFactory> {

    public static String[] columnNames = new String[]{           "编号",         "名称"   };
    public static int[] columnWidth=new int[]{      100,  100   };

    public static String[] fieldName = new String[]{  "dep",  "name" };

    public  static Class[] classes = new Class[]{Object.class,Object.class  };


    @Inject
    public OutFactoryModel( ) {
        super(columnNames, fieldName, classes, OutFactory.class);
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


    @Override
    public void setDatas(List<OutFactory> newDatas) {

        MiniRowCount=newDatas.size()+20;
        super.setDatas(newDatas);
    }
}
