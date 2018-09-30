package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.model.AbsTableModel;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.CellConfig;
import com.giants.hd.desktop.utils.Config;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.utils.JTableUtils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * 自定义表格
 * <p/>
 * 拦截setModel 方法， 满足一定条件 定制列宽  行高
 */
public class JHdTable extends JTable {


    /**
     * Constructs a default <code>JTable</code> that is initialized with a default
     * data model, a default column model, and a default selection
     * model.
     *
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public JHdTable() {


        // setDefaultRenderer(Action.class,new ActionCellRenderer());

//       setDefaultRenderer(Action.class,new ButtonRenderer());
//        setDefaultEditor(Action.class,
//                new ButtonEditor(new JCheckBox()));
//
//
//          TableColumn operate=null;
//        try {
//            operate=getColumn("操作");
//        }catch (Throwable t){
//            Config.log(t.getLocalizedMessage());
//            t.printStackTrace();
//        }
//        if(operate!=null) {
//            operate.setCellRenderer(new ButtonRenderer());
//            operate.setCellEditor(new ButtonEditor(new JCheckBox()));
//        }
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);


        if (dataModel instanceof CellConfig) {

            CellConfig absTableModel = (CellConfig) dataModel;
            //配置行高
            int rowHeight = absTableModel.getRowHeight();
            if (rowHeight > 0) {
                setRowHeight(rowHeight);
            }

            int[] columnWidths = absTableModel.getColumnWidth();
            JTableUtils.setJTableColumnsWidth(this, columnWidths);

        }




        if (dataModel instanceof BaseTableModel) {


            //配置定制列宽
            BaseTableModel baseTableModel = (BaseTableModel) dataModel;
            //配置多行文本展示。
            int[] multiLineTextColumnIndexes = baseTableModel.getMultiLineColumns();

            if (multiLineTextColumnIndexes != null && multiLineTextColumnIndexes.length > 0) {

                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {


                    @Override
                    protected void setValue(Object value) {

                        String valueString = value == null ? "" : value.toString();
                        super.setValue(HdSwingUtils.multiLineForLabel(valueString));
                    }

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {


                        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                };
//             TableCellRenderer renderer=new TextAreaCellRenderer();
                TableColumnModel columnModel = getColumnModel();
                for (int index : multiLineTextColumnIndexes) {
                    TableColumn column = columnModel.getColumn(index);
                    column.setCellRenderer(renderer);
                }


            }
        }


    }


    private class ActionCellRenderer extends JPanel implements TableCellRenderer, Serializable  {


        private JButton jButton;

        public ActionCellRenderer() {
            jButton = new JButton();
            setLayout(new BorderLayout());
            add(jButton);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    if(Config.DEBUG
                            )
                    {
                        Config.log("action click...........");
                    }
                }
            });
        }


        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (table == null) {
                jButton.setText("");
            } else

                jButton.setText(String.valueOf(value));

            setBackground(Color.BLUE);

            return this;
        }
    }


    private class ImageIconCellRenderer extends DefaultTableCellRenderer {
        /**
         * Returns the component used for drawing the cell.  This method is
         * used to configure the renderer appropriately before drawing.
         * <p/>
         * The <code>TableCellRenderer</code> is also responsible for rendering the
         * the cell representing the table's current DnD drop location if
         * it has one. If this renderer cares about rendering
         * the DnD drop location, it should query the table directly to
         * see if the given row and column represent the drop location:
         * <pre>
         *     JTable.DropLocation dropLocation = table.getDropLocation();
         *     if (dropLocation != null
         *             && !dropLocation.isInsertRow()
         *             && !dropLocation.isInsertColumn()
         *             && dropLocation.getRow() == row
         *             && dropLocation.getColumn() == column) {
         *
         *         // this cell represents the current drop location
         *         // so render it specially, perhaps with a different color
         *     }
         * </pre>
         * <p/>
         * During a printing operation, this method will be called with
         * <code>isSelected</code> and <code>hasFocus</code> values of
         * <code>false</code> to prevent selection and focus from appearing
         * in the printed output. To do other customization based on whether
         * or not the table is being printed, check the return value from
         * {@link JComponent#isPaintingForPrint()}.
         *
         * @param table      the <code>JTable</code> that is asking the
         *                   renderer to draw; can be <code>null</code>
         * @param value      the value of the cell to be rendered.  It is
         *                   up to the specific renderer to interpret
         *                   and draw the value.  For example, if
         *                   <code>value</code>
         *                   is the string "true", it could be rendered as a
         *                   string or it could be rendered as a check
         *                   box that is checked.  <code>null</code> is a
         *                   valid value
         * @param isSelected true if the cell is to be rendered with the
         *                   selection highlighted; otherwise false
         * @param hasFocus   if true, render cell appropriately.  For
         *                   example, put a special border on the cell, if
         *                   the cell can be edited, render in the color used
         *                   to indicate editing
         * @param row        the row index of the cell being drawn.  When
         *                   drawing the header, the value of
         *                   <code>row</code> is -1
         * @param column     the column index of the cell being drawn
         * @see JComponent#isPaintingForPrint()
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {


            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (table == null)
                return component;
            ImageIcon icon = null;
            String text = "";
            if (value instanceof ImageIcon) {


                icon = (ImageIcon) value;
            } else {
                text = "loading...";
            }

            setIcon(icon);
            setText(text);
            return component;
        }
    }
    /**
     * @version 1.0 11/09/98
     */

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * @version 1.0 11/09/98
     */

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                JOptionPane.showMessageDialog(button, label + ": Ouch!");
                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

}
