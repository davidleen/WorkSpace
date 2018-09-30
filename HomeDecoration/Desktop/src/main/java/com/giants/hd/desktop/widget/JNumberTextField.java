package com.giants.hd.desktop.widget;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 * 自定义数字输入控件
 */
public class JNumberTextField  extends JFormattedTextField{


    public JNumberTextField() {
        super();
        setFormatter(new NumberFormatter());
    }
}
