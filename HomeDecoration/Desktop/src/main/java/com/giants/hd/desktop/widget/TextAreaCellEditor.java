package com.giants.hd.desktop.widget;

import com.giants3.hd.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * 表格多行文本输入
 * Created by davidleen29 on 2016/7/29.
 */
public class TextAreaCellEditor extends AbstractCellEditor
        implements TableCellEditor, TableCellRenderer {


    JTextArea textArea;

    public TextAreaCellEditor() {

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);


    }


    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return textArea.getText();
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        textArea.setText(value==null?"":String.valueOf(value));
        return textArea;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        textArea.setText(String.valueOf(value));
        return textArea;
    }
}
