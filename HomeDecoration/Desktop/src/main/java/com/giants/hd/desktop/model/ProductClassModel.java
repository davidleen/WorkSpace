package com.giants.hd.desktop.model;

import javax.swing.*;

/**
 * 产品类型选择模型    与jcombox 适配使用
 */
@Deprecated
public class ProductClassModel<PClass> extends AbstractListModel  implements ComboBoxModel{
    @Override
    public void setSelectedItem(Object anItem) {

    }

    @Override
    public Object getSelectedItem() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Object getElementAt(int index) {
        return null;
    }
}
