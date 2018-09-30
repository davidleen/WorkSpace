package com.giants.hd.desktop.dialogs;

import com.giants3.hd.entity_erp.ErpStockOutItem;

import javax.swing.*;
import java.awt.event.*;

public class SplitStockOutItemDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tf_qty;
    private JLabel message;

    ErpStockOutItem item;
    private OnNewQtyGetListener listener;

    public SplitStockOutItemDialog() {
        setContentPane(contentPane);
        setTitle("出库拆单");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        if(item==null) {
            dispose();
            return;
        }
        int  newQty=0;
        try {
            newQty = Integer.valueOf(tf_qty.getText().trim());
        }catch (Throwable t)
        {}

        if(newQty>0&& newQty<item.stockOutQty)
        {
            dispose();
            if(listener!=null)
            {
                listener.onNewQty(newQty);
            }


        }


    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }




    public  void setStockOutItem(ErpStockOutItem item,OnNewQtyGetListener listener)
    {
        this.item=item;


        message.setText("当前"+item.prd_no+"出库数量是:"+item.stockOutQty );
        this.listener = listener;
    }

    public static void main(String[] args) {
        SplitStockOutItemDialog dialog = new SplitStockOutItemDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    public interface  OnNewQtyGetListener
    {
        void onNewQty(int newQty);
    }
}
