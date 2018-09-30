package com.giants.hd.desktop.widget;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 定制table 弹出菜单选择
 * Created by david on 2016/4/4.
 */
public class TableMenuAdapter extends MouseAdapter {

    private JTable table;
    private String[] menuTitles;
    private TableMenuListener listener;

    public TableMenuAdapter(JTable jTable,String[] menu, TableMenuListener listener) {
        this.table = jTable;
        this.menuTitles = menu;
        this.listener = listener;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        showMenu(e);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mouseReleased(e);
        showMenu(e);

    }

    private void showMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {

            JPopupMenu menu = new TablePopMenu(table, listener,menuTitles);
            menu.show(e.getComponent(), e.getX(), e.getY());

        }
    }


    /**
     * 自定义表格右键弹出菜单
     */

    class TablePopMenu extends JPopupMenu {


        private final TableMenuListener lister;
        private JTable table;

        private String[] menus;




        public TablePopMenu(JTable table, TableMenuListener lister, String[] menu) {
            super();
            this.table = table;
            this.lister = lister;
            this.menus = menu;
            init();
        }

        private void init() {

            for (int i = 0; i < menus.length; i++) {
                final int  index=i;
                String menuValue= menus[i];
                JMenuItem item = new JMenuItem(menuValue);
                add(item);
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(lister!=null)
                        {
                            lister.onMenuClick(table,index);

                        }
                    }
                });
            }



        }


    }
    public interface  TableMenuListener
    {
        public void onMenuClick(JTable table,int index);

    }
}

