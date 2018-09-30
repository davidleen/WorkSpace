package com.giants.hd.desktop.widget;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 表格鼠标事件监听， 提供右点击弹出菜单功能。
 */
public class TableMouseAdapter extends MouseAdapter{

    TablePopMenu.TableMenuLister lister;

    //监听器， 监听表格右键点击功能
        public TableMouseAdapter(TablePopMenu.TableMenuLister lister)
        {
            this.lister=lister;

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
                JTable source = (JTable) e.getSource();
                JPopupMenu menu = new TablePopMenu(source, lister);
                //  取得右键点击所在行
                int row = e.getY() / source.getRowHeight();
                menu.show(e.getComponent(), e.getX(), e.getY());

            }
        }

}
