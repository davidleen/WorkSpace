package com.giants3.android.widgets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;


public class WaitingDialog extends ProgressDialog implements IViewWaiting {
    public WaitingDialog(Context context) {
        super(context);
    }

    public WaitingDialog(Context context, int theme) {
        super(context, theme);
    }






    @Override
    public void showWait() {
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setMessage(null);
        show();
    }

    @Override
    public void showWait(String message) {
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setMessage(message);
        show();
    }

    @Override
    public void showWait(String message, int progress, int max) {
        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        setMessage(message);
        setMax(max);
        setProgress(progress);
        show();
    }

    @Override
    public void hideWait() {

        hide();

    }
}
