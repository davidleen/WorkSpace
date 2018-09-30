package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.local.ConstantData;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * JTable 控件的功能类。
 */

public class JTableUtils {




    /**
     * 定制列宽
     * @param table
     * @param widths
     */
    public static void setJTableColumnsWidth(JTable table, int... widths) {

        if(widths==null) return;

         int length = widths.length;


        TableColumnModel columnModel = table.getColumnModel();
        int columnCount = columnModel.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            if(i<length) {
                TableColumn column = columnModel.getColumn(i);

                if(widths[i]>= ConstantData.MAX_COLUMN_WIDTH)
                    column.setPreferredWidth(  widths[i]);
                else
                    column.setMinWidth(  widths[i]);

            }

        }


    }


    /**
     * 获取被选中行数据 基于模型（model）。
     * @param table
     * @return
     */
    public static final int[] getSelectedRowSOnModel(JTable table)
    {


        int[] rows=table.getSelectedRows();

        int length = rows.length;
        for (int i = 0; i < length; i++) {

            rows[i]=table.convertRowIndexToModel(rows[i]);

        }
        return rows;

    }
    /**
     * 获取被选中行数据 基于模型（model）。
     * @param table
     * @return
     */
    public static final int  getFirstSelectedRowSOnModel(JTable table)
    {


        int[] rows=getSelectedRowSOnModel(table);
        if(rows==null||rows.length==0) return -1;
        return rows[0];


    }


}