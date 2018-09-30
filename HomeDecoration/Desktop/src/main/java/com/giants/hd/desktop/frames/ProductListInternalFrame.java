package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.viewImpl.Panel_ProductList;

import java.awt.*;

/**
 * Created by david on 2015/11/23.
 */
public class ProductListInternalFrame extends BaseInternalFrame {
    public ProductListInternalFrame( ) {
        super("产品列表");
    }

    @Override
    protected Container getCustomContentPane() {
        return new Panel_ProductList().getRoot();
    }
}
