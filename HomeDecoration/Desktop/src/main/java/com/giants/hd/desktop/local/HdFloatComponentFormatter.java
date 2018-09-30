package com.giants.hd.desktop.local;

import javax.swing.*;
import java.text.ParseException;

/**
 * 日期输入格式化
 */
public class HdFloatComponentFormatter extends JFormattedTextField.AbstractFormatter {






    public HdFloatComponentFormatter(){

    }

    @Override
    public String valueToString(Object value) throws ParseException {

        return value.toString();
    }

    @Override
    public Object stringToValue(String text) throws ParseException {

        return  Float.valueOf(text);
    }
}
