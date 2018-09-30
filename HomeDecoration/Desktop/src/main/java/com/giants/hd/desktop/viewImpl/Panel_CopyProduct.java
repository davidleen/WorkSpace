package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.entity.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  产品翻单面板
 */
public class Panel_CopyProduct  extends BasePanel
{
    private JPanel panel1;
    private JTextField tf_oldName;
    private JTextField tf_oldVersion;
    private JTextField tf_newName;
    private JTextField tf_newVersion;
    private JButton btn_confirm;
    private JCheckBox copyPicture;


    public boolean isCopyPicture()
    {

        return copyPicture.isSelected();
    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    public Panel_CopyProduct() {


        btn_confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null)
                {
                    listener.save();
                }
            }
        });
    }

    public void setData(Product data) {
        tf_oldName.setText(data.getName());
        tf_oldVersion.setText(data.getpVersion());
        tf_newName.setText(data.getName());
        tf_newVersion.setText(data.getpVersion());
    }

    public void getData(Product data) {
        data.setName(tf_newName.getText());
        data.setpVersion(tf_newVersion.getText());
    }

    public boolean isModified(Product data) {
        if (tf_oldName.getText() != null ? !tf_oldName.getText().equals(data.getName()) : data.getName() != null)
            return true;
        if (tf_oldVersion.getText() != null ? !tf_oldVersion.getText().equals(data.getpVersion()) : data.getpVersion() != null)
            return true;
        return false;
    }
}
