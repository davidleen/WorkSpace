package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.model.DefaultModel;
import com.giants.hd.desktop.mvp.IViewer;

import java.awt.*;

/**
 * 所有对话框类的基类 提供注射功能
 */
public abstract class BaseMVPDialog<T, K extends IViewer> extends MVPDialog<T, K, DefaultModel> {


    public BaseMVPDialog(Window window, String title) {
        super(window, title);

    }



    @Override
    public DefaultModel createModel() {
        return new DefaultModel();
    }


}




