package com.giants.hd.desktop.model;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 表格模型基类
 * 旧的模型   数组形式表示列
 */

public abstract class BaseTableModel<T> extends AbsTableModel<T> {


    public String[] columnNames;
    public String[] fieldName;
    public Field[] fields;
    public Class[] classes;
    //是否合成字段  以.为二级字段。
    public boolean[] combineField;


    protected int MiniRowCount = 20;

    protected boolean editable = true;
    private boolean adjustable = true;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }


    public BaseTableModel(String[] columnNames, String[] fieldName, Class[] classes, Class<T> itemClass) {

        this(new ArrayList<T>(), columnNames, fieldName, classes, itemClass);


    }


    /**
     * 调整表结构
     *
     * @param columnNames
     * @param fieldName
     * @param classes
     */
    public void updateStructure(String[] columnNames, String[] fieldName, Class[] classes) {
        this.classes = classes;
        this.fieldName = fieldName;
        this.columnNames = columnNames;

        fireTableStructureChanged();
    }


    public BaseTableModel(List<T> datas, String[] columnNames, String[] fieldName, Class[] classes, Class<T> itemClass) {
        super(datas, itemClass);

        this.classes = classes;
        this.fieldName = fieldName;
        this.columnNames = columnNames;
        int size = fieldName.length;
        fields = new Field[size];
        combineField = new boolean[size];
        for (int i = 0; i < size; i++) {
            combineField[i] = fieldName[i].indexOf(StringUtils.STRING_SPLIT_DOT) > -1;

            if (combineField[i]) {


            } else {


                try {
                    fields[i] = itemClass.getField(fieldName[i]);
                } catch (NoSuchFieldException e) {

                    Logger.getLogger("TEST").info(fieldName[i] + " is not a field of class :" + itemClass);

                }
            }

        }

        adjustRowCount();

    }

    /**
     * 调整默认显示的记录数
     */
    protected void adjustRowCount() {

        if (!adjustable) return;

        int currentSize = this.datas.size();

        if (currentSize < MiniRowCount) {
            for (int i = currentSize; i < MiniRowCount; i++)

                addNewRow(i);
//                try {
//                    this.datas.add(itemClass.newInstance());
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }

        }


    }


    @Override
    public int getColumnCount() {
        return columnNames.length;
    }


    @Override
    public Class getColumnClass(int c) {


        if (c >= 0 && c < classes.length)

            return classes[c];
        else
            return Object.class;

    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }


    public List<T> getValuableDatas() {
        ArrayList<T> newArrays = new ArrayList<>();
        for (T data : datas) {
            if (data instanceof Valuable) {
                if (!((Valuable) data).isEmpty()) {
                    newArrays.add(data);
                }
            }

        }

        return newArrays;

    }


    /**
     * 使用外部数据源  以便不同model可以共享数据
     *
     * @param datas
     */
    public void setExternalData(List<T> datas) {

        this.datas = datas;
        adjustRowCount();
        fireTableDataChanged();
    }


    /**
     * \
     * 添加新行
     *
     * @param index
     */
    public T addNewRow(int index) {

        T newItem = null;
        try {
            newItem = itemClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (null == newItem) return null;
        if (index < 0 || index >= getRowCount()) {
            datas.add(newItem);
        } else
            datas.add(index, newItem);
        fireTableDataChanged();
        return newItem;

    }

    /**
     * 删除行
     *
     * @param rowIndex
     */
    public void deleteRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < getRowCount()) {


            datas.remove(rowIndex);
            fireTableDataChanged();
        }


    }


    /**
     * 插入新数据
     *
     * @param insertDatas
     * @param index
     */
    public void insertNewRows(List<T> insertDatas, int index) {
        datas.addAll(index, insertDatas);
        fireTableDataChanged();
    }


    /**
     * 删除多行
     *
     * @param rowIndexs
     */
    public void deleteRows(int[] rowIndexs) {


        List<T> datasToDelete = new ArrayList<>();
        for (int rowIndex : rowIndexs) {

            if (rowIndex >= 0 && rowIndex < getRowCount()) {
                datasToDelete.add(getItem(rowIndex));
            }
        }

        datas.removeAll(datasToDelete);
        fireTableDataChanged();

    }


    @Override
    public  int getColumnWidth(int column) {

        int[] widthArray = getColumnWidth();
        if (widthArray != null) {
            return widthArray[column];
        }
        return super.getColumnWidth(column);
    }

    /**
     * 定制列宽度
     *
     * @return
     */
    @Override
    public int[] getColumnWidth() {
        return null;
    }


    /**
     * 获取文本为多行显示的列。默认为空。
     *
     * @return
     */
    public int[] getMultiLineColumns() {
        return null;
    }


    /**
     * 是否自动追加记录
     *
     * @param adjustable
     * @return
     */
    public void setRowAdjustable(boolean adjustable) {
        this.adjustable = adjustable;
        if (!adjustable) {
            datas.clear();

        }
    }


    /**
     * 追加新行
     *
     * @param appendCount
     */
    public void appendRows(int appendCount) {

        for (int i = 0; i < appendCount; i++) {
            T newItem = null;
            try {
                newItem = itemClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            datas.add(newItem);
        }
        fireTableDataChanged();


    }


    /**
     * 是否有.连接的字段
     *
     * @param column
     * @return
     */
    @Override
    protected boolean isCombineField(int column) {


        return combineField[column];
    }


    @Override
    protected String getFieldName(int column) {
        return fieldName[column];
    }

    @Override
    protected Field getField(int column) {
        return fields[column];
    }
}
