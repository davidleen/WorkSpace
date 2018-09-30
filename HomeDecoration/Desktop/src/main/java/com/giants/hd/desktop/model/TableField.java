package com.giants.hd.desktop.model;

import javax.swing.*;

/**
 * 表格模式 列定义
 * Created by davidleen29 on 2017/4/2.
 */
public class TableField {


    /**
     * 列名  展示用
     */
    public String columnName;
    /**
     * 列对应字段值 使用反射
     */
    public String fileName;


    /**
     *  是否组合字段 用.连接 表示对象的对象
     */
    public boolean isCombineField;
    /**
     * 列宽度
     */
    public int width;
    /**
     * 列对应字段值类型
     */
    public FieldType aClass;


    /**
     * 表格字段类型定义
     */
    public  static enum FieldType {

        B, I, L, F, S, O,iMG


    }

    public static Class getClass(FieldType fieldType) {
        if (fieldType == FieldType.B) return Boolean.class;
        if (fieldType == FieldType.I) return Integer.class;
        if (fieldType == FieldType.L) return Long.class;
        if (fieldType == FieldType.F) return Float.class;
        if (fieldType == FieldType.S) return String.class;
        if (fieldType == FieldType.O) return Object.class;
        if (fieldType == FieldType.iMG) return ImageIcon.class;
        return null;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "columnName='" + columnName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", width=" + width +
                ", aClass=" + aClass +
                '}';
    }
}
