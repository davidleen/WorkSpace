package com.giants.hd.desktop.widget;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * 表格多行文本输入
 * Created by davidleen29 on 2016/7/29.
 */
public class TextAreaCellRenderer extends JScrollPane
        implements TableCellRenderer {


    JTextArea textArea;

    public TextAreaCellRenderer() {
        textArea=new JTextArea();
        textArea.setBorder(null);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(null);
        getViewport().add(textArea, null);





    }




    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        textArea.setText(value==null?"":String.valueOf(value));
        return
                this;
    }
}
