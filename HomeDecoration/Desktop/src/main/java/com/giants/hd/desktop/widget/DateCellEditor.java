package com.giants.hd.desktop.widget;

import com.giants3.hd.utils.DateFormats;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class DateCellEditor extends AbstractCellEditor
                         implements TableCellEditor,
        ActionListener {


    public static final String DATE_SELECTED = "Date selected";
    JDialog dialog;
    protected static final String EDIT = "edit";
    String dateString;

    JButton button;
    public DateCellEditor( ) {

          button =new JButton();

        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        final JDatePanelImpl imp=new JDatePanelImpl(null);

        dialog =new JDialog();
        dialog.setTitle("日期选择") ;
        dialog.setModal(true);
        dialog.setContentPane(imp);
        dialog.setMinimumSize(new Dimension(400,300));
         imp.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {

                 if(e.getActionCommand().equals(DATE_SELECTED))
                 {

                     dialog.setVisible(false);
                             Calendar calendar= (Calendar) imp.getModel().getValue();
                     if(calendar==null)
                     {fireEditingCanceled();
                         return;

                     }
                     dateString= DateFormats.FORMAT_YYYY_MM_DD.format(calendar.getTime());


                 }


             }
         });


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
//            button.setBackground(currentColor);
//            colorChooser.setColor(currentColor);
        Component component= (Component) e.getSource();

          //  dialog.setLocation(component.getLocationOnScreen());
            dialog.setLocationRelativeTo(component);
            dialog.setVisible(true);


            fireEditingStopped(); //Make the renderer reappear.

        } else { //User pressed dialog's "OK" button.
//            currentColor = colorChooser.getColor();
        }
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return dateString;
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        dateString = (String) value;
        button.setText(dateString);
        return button;
    }
}