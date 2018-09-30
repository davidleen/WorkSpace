package com.giants.hd.desktop;

import javax.swing.*;

public class ProgressDialog extends JDialog {
    private JPanel contentPane;
    private JProgressBar progressBar1;
    private JButton buttonOK;

    public ProgressDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }
}
