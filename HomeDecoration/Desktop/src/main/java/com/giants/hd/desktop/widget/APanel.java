package com.giants.hd.desktop.widget;

import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 *  a panel  if its layout is formLayout  draw gridLine
 */

public class APanel extends JPanel {



    public Color DEFAULT_GRID_LINE_COLOR=Color.GRAY
            ;

    @Override
    public void paint(Graphics g) {


         super.paint(g);


    }


    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
     //   paintGrid(g);
    }

    @Override
    public void paintComponent(Graphics g)
    {


    //   Rectangle rectangle=   g.getClipBounds();
//        g.setColor(Color.GREEN);
//        g.drawRect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);


        super.paintComponent(g);
    //    g.setColor(Color.GRAY);
      //  g.drawRect(rectangle.x, rectangle.y, rectangle.width - 2, rectangle.height-2);
    }


    /**
     * Paints the form's grid lines and diagonals.
     *
     * @param g    the Graphics object used to paint
     */
    private void paintGrid(Graphics g) {
        if (!(getLayout() instanceof FormLayout)) {
            return;
        }
        FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
        int left   = layoutInfo.getX();
        int top    = layoutInfo.getY();
        int width  = layoutInfo.getWidth();
        int height = layoutInfo.getHeight();

        g.setColor(DEFAULT_GRID_LINE_COLOR);
      //  g.drawLine(left, top,width,height);
        // Paint the column bounds.
        for (int col = 0; col < layoutInfo.columnOrigins.length; col++) {
            g.fillRect(layoutInfo.columnOrigins[col], top, 1, height);
        }



        // Paint the row bounds.
        for (int row = 0; row < layoutInfo.rowOrigins.length; row++) {
            g.fillRect(left, layoutInfo.rowOrigins[row], width, 1);
        }


    }
}
