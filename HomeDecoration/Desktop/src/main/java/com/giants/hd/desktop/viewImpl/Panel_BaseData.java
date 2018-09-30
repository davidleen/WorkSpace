package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.dialogs.ProductProcessDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 基础数据汇总面板
 */
public class Panel_BaseData extends  BasePanel {
    private JPanel root;
    private JButton btn_process;

    @Override
    public JComponent getRoot() {
        return root;
    }


    public Panel_BaseData() {

        btn_process.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ProductProcessDialog dialog=new ProductProcessDialog(SwingUtilities.getWindowAncestor(root));
                dialog.setMinimumSize(new Dimension(480,600));
                dialog.pack();;
                dialog.setLocationRelativeTo(root);
                dialog.setVisible(true);
            }
        });
    }
}
