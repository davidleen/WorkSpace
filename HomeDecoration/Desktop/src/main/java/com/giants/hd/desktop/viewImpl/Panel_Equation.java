package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.EquationIPresenter;
import com.giants.hd.desktop.mvp.viewer.EquationViewer;

import javax.swing.*;

/**
 * Created by davidleen29 on 2017/3/7.
 */
public class Panel_Equation extends BasePanel implements EquationViewer {
    private JPanel root;

    public Panel_Equation(EquationIPresenter presenter) {

    }

    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return root;
    }
}
