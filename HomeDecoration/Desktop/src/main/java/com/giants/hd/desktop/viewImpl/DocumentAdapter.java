package com.giants.hd.desktop.viewImpl;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public  abstract  class DocumentAdapter implements DocumentListener {
    @Override
    public void insertUpdate(DocumentEvent e) {
        onTextChange(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        onTextChange(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public abstract  void onTextChange(DocumentEvent documentEvent);
}
