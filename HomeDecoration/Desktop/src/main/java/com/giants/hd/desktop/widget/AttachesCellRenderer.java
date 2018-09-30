package com.giants.hd.desktop.widget;

import com.giants3.hd.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * 附件在表格中展示多行文本输入
 * Created by davidleen29 on 2016/7/29.
 */
public class AttachesCellRenderer implements
        TableCellRenderer {




    public AttachesCellRenderer() {






    }




    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String urls=(value==null?"":String.valueOf(value));
        String[] fileNames = StringUtils.isEmpty(urls) ? new String[]{} : urls.split(";");

//        if(fileNames==null||fileNames.length==0)
//        {
//            return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
//        }
         AttachPanel attachPanel=new AttachPanel();
        attachPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //attachPanel.setBackground(Color.RED);

      //  JScrollPane jScrollPane=new JScrollPane(attachPanel);
        attachPanel.setAttachFiles(fileNames);

        return attachPanel;

    }
}
