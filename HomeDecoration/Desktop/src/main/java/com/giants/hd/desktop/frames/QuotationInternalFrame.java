package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.viewImpl.Panel_Quotation;

import java.awt.*;

/**
 * Created by david on 2015/11/23.
 */
public class QuotationInternalFrame extends BaseInternalFrame {
    public QuotationInternalFrame() {
        super("报价列表");
    }

    @Override
    protected Container getCustomContentPane() {
        return new Panel_Quotation().getRoot();
    }
}
