package com.giants.hd.desktop.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 基于List<TableField> 的表格模型基类
 * <p/>
 * Created by davidleen29 on 2017/4/2.
 */
public class BaseListTableModel<T> extends AbsTableModel<T> {



    int rowHeight;

    List<TableField> tableFields;

    List<Field> fields;

    int[] columnWidth;

    public BaseListTableModel(Class<T> itemClass) {
        super(itemClass);

    }

    public BaseListTableModel(Class<T> itemClass, List<TableField> tableFields) {
        super(itemClass);
        updateStructure(tableFields);
    }
    @Override
    protected String getFieldName(int column) {
        return tableFields.get(column).fileName;
    }

    @Override
    protected Field getField(int column) {
        return fields.get(column);
    }

    /**
     * 调整表结构
     */
    public void updateStructure(List<TableField> tableFieldList) {
        this.tableFields = tableFieldList;
        resetField();
        fireTableStructureChanged();
    }


    /**
     * 重置每一个关联的Field 对象
     */
    private void resetField() {


        columnWidth=new int[tableFields.size()];
        fields = new ArrayList<>();
        int i=0;
        for (TableField tableField : tableFields) {
            Field field = null;
            try {
                field = itemClass.getField(tableField.fileName);

            } catch (Throwable e) {
                e.printStackTrace();
                Logger.getLogger("TEST").info(tableField.fileName + " is not a field of class :" + itemClass);

            }
            fields.add(field);
            columnWidth[i++]=tableField.width;
        }


    }

    @Override
    public Class getColumnClass(int c) {


          Class aClass = TableField.getClass(tableFields.get(c).aClass);
        if(aClass==Integer.class||aClass==Long.class||aClass==Float.class)
            aClass=String.class;
        return aClass;

    }

    @Override
    public String getColumnName(int column) {
        return tableFields.get(column).columnName;
    }

    public void setDatas(List<T> newDatas) {


        this.datas.clear();

        if (newDatas != null)
            this.datas.addAll(newDatas);


        fireTableDataChanged();

    }


    @Override
    public int[] getColumnWidth() {


        return columnWidth;

    }

    @Override
    public int getColumnCount() {
        if(tableFields==null) return 0;
        return tableFields.size();
    }

    /**
     * 是否有.连接的字段
     *
     * @param column
     * @return
     */
    @Override
    protected boolean isCombineField(int column) {
        return tableFields.get(column).isCombineField;
    }

    /**
     * 返回 设置的行高
     *
     * @return
     */
    @Override
    public int getRowHeight() {

        if(rowHeight>0) return rowHeight;
        return 50;
    }
    public void setRowHeight(int newRowHeight)
    {
        this.rowHeight=newRowHeight;
    }
}
