package com.giants.hd.desktop.widget;

import javax.swing.*;
import java.awt.*;

/**
 * 背景绘制类， 主要用户绘制 分页标签的选中状态背景
 */
public class BackgroundPainter implements Painter<JComponent> {

    private Color color = null;

    public BackgroundPainter(Color c) {
        color = c;
    }

    @Override
    public void paint(Graphics2D g, JComponent object, int width, int height) {
        if (color != null) {
            g.setColor(color);
            g.fillRoundRect(0, 0, width - 1, height - 1,width/10,height/10);
            //g.fillRect(0, 0, width - 1, height - 1);
        }
    }
}
