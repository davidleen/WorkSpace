package com.giants3.android.widgets;

/**
 * 等待框接口
 */
public interface IViewWaiting {


    void showWait();
    void showWait(String message);
    void showWait(String message,int progress,int max);
    void hideWait();
}
