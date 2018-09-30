package com.giants.hd.desktop.dialogs;


import com.giants.hd.desktop.mvp.IModel;
import com.giants.hd.desktop.mvp.IViewer;

import java.awt.*;

/**
 * 所有对话框类的基类 提供注射功能
 */
public abstract class MVPDialog<T, K extends IViewer,M extends IModel> extends BaseDialog<T> {


    K viewer;
    M  model;

    public MVPDialog(Window window, String title) {
        super(window, title);

        viewer = createViewer();
        if (viewer != null)

            setContentPane(viewer.getRoot());

        model=createModel();



        setMinimumSize(new Dimension(400, 300));



    }


    protected abstract K createViewer();



    protected  abstract  M createModel();

    public  M  getModel()
    {
        return model;
    }

    protected K getViewer() {
        return viewer;
    }


}




