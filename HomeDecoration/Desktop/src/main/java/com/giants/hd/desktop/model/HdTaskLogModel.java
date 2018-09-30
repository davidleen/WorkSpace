package com.giants.hd.desktop.model;

import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  定时任务数据模型
 */
public class HdTaskLogModel extends  BaseTableModel<HdTaskLog> {

    public static String[] columnNames = new String[]{                         "任务名称 ",  "运行时刻"  ,"运行耗时（秒）" ,"任务状态","错误信息" };
    public static int[] columnWidth=new int[]{   150,        150 ,150, 120,200};


    public static final String TIME_SPEND = "timeSpend";
    public static String[] fieldName = new String[]{ "taskTypeName", "executeTimeString", TIME_SPEND, "stateName","memo"  };

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class };


    @Inject
    public HdTaskLogModel() {
        super(columnNames, fieldName, classes, HdTaskLog.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return false;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        HdTaskLog task=getItem(rowIndex);
        if(task.id<=0) return "";


        if(TIME_SPEND.equals(fieldName[columnIndex]))
        {
            long timeSpend=task.timeSpend;

            int hour= (int) (timeSpend/3600);
            int minute= (int) (timeSpend%3600/60);
            int second= (int) (timeSpend%60);
            return hour+" 时 "+minute+" 分 "+second+" 秒 ";
        }





        return super.getValueAt(rowIndex, columnIndex);
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
