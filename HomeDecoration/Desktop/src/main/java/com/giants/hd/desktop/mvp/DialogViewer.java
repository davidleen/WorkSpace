package com.giants.hd.desktop.mvp;

import javax.swing.*;
import java.awt.*;

public class DialogViewer implements IViewer {

    private Dialog dialog;

    public DialogViewer(Dialog dialog)
    {
        this.dialog = dialog;
    }
    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void showLoadingDialog(String hint) {



    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

        JOptionPane.showMessageDialog(dialog,message);

    }

    @Override
    public Window getWindow() {
        return dialog;
    }

    @Override
    public Container getRoot() {
        return null;
    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void showMesssage(String message) {
        JOptionPane.showMessageDialog(dialog,message);
    }

    @Override
    public boolean showConfirmMessage(String message) {
        return false;
    }

    @Override
    public void showLoadingDialogCarefully() {

    }
}
