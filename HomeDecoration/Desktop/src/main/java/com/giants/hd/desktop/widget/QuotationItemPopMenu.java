package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.utils.JTableUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 自定义表格右键弹出菜单
 */

public class QuotationItemPopMenu extends JPopupMenu {



    private JTable table;
    private TableMenuLister lister;


    public static  final int ITEM_INSERT=1;
    public static  final int ITEM_DELETE=2;
    public static  final int ITEM_APPEND=3;


    public QuotationItemPopMenu(JTable table, TableMenuLister lister)
    {
        super();
        this.table=table;
        this.lister=lister;
        init();
    }

    private void init() {


        JMenuItem insertItem = new JMenuItem("插入行");
        JMenuItem deleteItem = new JMenuItem("删除行");
        JMenuItem appendItem = new JMenuItem("追加10行");
        add(insertItem);
        add(deleteItem);
        add(appendItem);



        insertItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int tableRow[] = JTableUtils.getSelectedRowSOnModel(table);
                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();

                    if (lister != null)
                        lister.onTableMenuClick(ITEM_INSERT, model, tableRow);
                }


            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableRow[]= JTableUtils.getSelectedRowSOnModel(table);
                if(tableRow.length==0 )
                {
                    JOptionPane.showMessageDialog(table,"请选择行进行删除。");
                    return;
                }

                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();

                    if(lister!=null)
                        lister.onTableMenuClick(ITEM_DELETE,model,tableRow);
                }


            }
        });
        appendItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();
                    int rowCount=     model.getRowCount();
                    if (lister != null)
                        lister.onTableMenuClick(ITEM_APPEND, model, new int[]{rowCount,rowCount+10});
                }


            }
        });



    }



    public  interface  TableMenuLister
    {
        public void onTableMenuClick(int index, BaseTableModel tableModel, int rowIndex[]);
    }

}
