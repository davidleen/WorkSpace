package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.local.TableDuplicateHelper;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.utils.JTableUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 自定义表格右键弹出菜单
 */

public class TablePopMenu extends JPopupMenu {



    private JTable table;
    private TableMenuLister lister;


    public static  final int ITEM_INSERT=1;
    public static  final int ITEM_DELETE=2;
    public static final int ITEM_COPY=4;
    public static  final int ITEM_PAST=3;

    public TablePopMenu(JTable table,TableMenuLister lister)
    {
        super();
        this.table=table;
        this.lister=lister;
        init();
    }

    private void init() {


        JMenuItem insertItem = new JMenuItem("添加行");
        JMenuItem deleteItem = new JMenuItem("删除行");
        JMenuItem copyItem = new JMenuItem("整表复制");
        JMenuItem pastItem = new JMenuItem("整表黏贴");
        add(insertItem);
        add(deleteItem);
        add(copyItem);
        add(pastItem);

       // copyItem.setEnabled(!TableDuplicateHelper.hasBufferData());
        pastItem.setEnabled(TableDuplicateHelper.hasBufferData());

        insertItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int tableRow[] = JTableUtils.getSelectedRowSOnModel(table);
                if(tableRow==null||tableRow.length==0)
                {
                    JOptionPane.showMessageDialog(table,"请选中一行进行操作。");

                }else {
                    if (table.getModel() instanceof BaseTableModel) {
                        BaseTableModel model = (BaseTableModel) table.getModel();

                        if (lister != null)
                            lister.onTableMenuClick(ITEM_INSERT, model, tableRow);
                    }
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


        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {







                int modelIndex[] = JTableUtils.getSelectedRowSOnModel(table);

                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();
                    if(lister!=null)
                        lister.onTableMenuClick(ITEM_COPY ,model,modelIndex);



                }







            }
        });

        pastItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {







                int modelIndex[] = JTableUtils.getSelectedRowSOnModel(table);

                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();
                    if(lister!=null)
                        lister.onTableMenuClick(ITEM_PAST ,model,modelIndex);



                }







            }
        });


    }



    public  interface  TableMenuLister
    {
        /**
         *
         * @param index  菜单项目序列号
         * @param tableModel  表格模型
         * @param rowIndex  选中行的序号
         */
        public void onTableMenuClick(int index, BaseTableModel tableModel,int rowIndex[]);
    }

}
