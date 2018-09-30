package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.viewImpl.Panel_ProductReport;

import java.awt.*;

/**
 * Created by davidleen29 on 2017/3/17.
 */
public class ProductReportInternalFrame extends BaseInternalFrame {
    public ProductReportInternalFrame() {
        super("产品信息批量导出");
    }

    @Override
    protected Container getCustomContentPane() {
        return new Panel_ProductReport().getRoot();
    }
}
