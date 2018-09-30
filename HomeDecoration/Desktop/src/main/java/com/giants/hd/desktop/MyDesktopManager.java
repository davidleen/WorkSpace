package com.giants.hd.desktop;

import javax.swing.*;

/**
 * Created by david on 2015/11/17.
 */
public class MyDesktopManager extends DefaultDesktopManager {

    JDesktopPane desk;

    public MyDesktopManager(JDesktopPane desk) {
        this.desk = desk;
    }

    int iconX = 0;
    int iconWidth = 160;
    int iconHeight = 40;

    @Override
    public void iconifyFrame(JInternalFrame f) {
        super.iconifyFrame(f);
        JInternalFrame.JDesktopIcon icon = f.getDesktopIcon();
        //  desk is the JDesktopPane object
        int iconY = desk.getSize().height - iconHeight;
        icon.setBounds(iconX, iconY, iconWidth, iconHeight);
        // Calculate for next icon bounds
        iconX += iconWidth;

    }

    @Override
    public void deiconifyFrame(JInternalFrame f) {
        super.deiconifyFrame(f);
        // Calculate for next icon bounds
        iconX = 0;
        for (JInternalFrame frame : desk.getAllFrames()) {
            if (frame.isIcon()) {
                JInternalFrame.JDesktopIcon icon = frame.getDesktopIcon();
                icon.setLocation(iconX, icon.getY());
                iconX += iconWidth;
            }
        }



    }

    @Override
    public void maximizeFrame(JInternalFrame f) {
         super.maximizeFrame(f);

//        f.setNormalBounds(f.getBounds());
//        Rectangle desktopBounds = f.getParent().getBounds();
//        setBoundsForFrame(f, 0, 0,
//                desktopBounds.width, desktopBounds.height-iconHeight);
//
//        // Set the maximized frame as selected.
//        try {
//            f.setSelected(true);
//        } catch (PropertyVetoException e2) {
//        }
    }


    /** This moves the <code>JComponent</code> and repaints the damaged areas. */
    @Override
    public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
        super.setBoundsForFrame(f, newX, newY, newWidth,newHeight );

    }



}
