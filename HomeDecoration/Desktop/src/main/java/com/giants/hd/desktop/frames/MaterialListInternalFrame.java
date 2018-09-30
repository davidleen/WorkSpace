package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.viewImpl.Panel_Material;

import java.awt.*;

/**
 * Created by david on 2015/11/23.
 */
public class MaterialListInternalFrame extends BaseInternalFrame {
    public MaterialListInternalFrame() {
        super("材料列表");
    }

    @Override
    protected Container getCustomContentPane() {
        return new Panel_Material("").getRoot();
    }
}
