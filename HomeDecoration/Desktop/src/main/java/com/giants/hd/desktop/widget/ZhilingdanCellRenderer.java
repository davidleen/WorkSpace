package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.model.ZhilingdanModel;
import com.giants3.hd.entity_erp.Zhilingdan;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * 指令单特殊定制表格绘制
 */
public class ZhilingdanCellRenderer extends DefaultTableCellRenderer {


    public ZhilingdanCellRenderer( ) {
        setOpaque(true);

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Component component = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

       ZhilingdanModel  model= (ZhilingdanModel) table.getModel();


        Zhilingdan  zhilingdan=model.getItem(row);



         if(zhilingdan!=null) {

             int modelColumn=table.convertColumnIndexToModel(column);
             if(zhilingdan.isCaigouOverDue&&model.isCaigouField(modelColumn))

                 component.setForeground(Color.RED);

            else
             if (zhilingdan.isJinhuoOverDue &&model.isJinhuoField(modelColumn))


                 component.setForeground(Color.RED);

             else
             if(isSelected)
             {
                 component.setForeground(Color.WHITE);

             }else
             {
                 component.setForeground(Color.BLACK);
             }


         }



        return component;
    }
}