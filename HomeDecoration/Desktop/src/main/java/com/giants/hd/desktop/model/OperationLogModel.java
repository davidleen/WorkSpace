package com.giants.hd.desktop.model;

import com.giants3.hd.entity.OperationLog;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  业务员表格数据模型
 */
public class OperationLogModel extends  BaseTableModel<OperationLog> {

    public static String[] columnNames = new String[]{"id"," 时间", "编号 ",  "名称" ,"中文名称 ","操作类型" ,"操作表","记录id","相关信息" };
    public static int[] columnWidth=new int[]{ 100,  120,        60 ,60,120,60,60,40,200};

    public static String[] fieldName = new String[]{"id","timeString", "userCode", "userName", "userCName","operationType", "tableName","recordId", "message"};

    public  static Class[] classes = new Class[]{ };


    @Inject
    public OperationLogModel() {
        super(columnNames, fieldName, classes, OperationLog.class);
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
