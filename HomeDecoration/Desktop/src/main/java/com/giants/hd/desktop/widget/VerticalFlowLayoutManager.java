package com.giants.hd.desktop.widget;

import java.awt.*;

/**
 * Created by davidleen29 on 2016/9/8.
 */
public class VerticalFlowLayoutManager implements LayoutManager {
    private int hgap
            ;
    private int vgap;


    public VerticalFlowLayoutManager( int vGap)
    {
        this.hgap=0;
        this.vgap=vGap;
    }
    /**
     * If the layout manager uses a per-component string,
     * adds the component <code>comp</code> to the layout,
     * associating it
     * with the string specified by <code>name</code>.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {

    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param target the container to be laid out
     * @see #minimumLayoutSize
     */
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return getLayoutDimension(target);
    }

    private Dimension getLayoutDimension(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();
            boolean firstVisibleComponent = true;


            for (int i = 0 ; i < nmembers ; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    dim.height +=d.getHeight();
                    dim.height+= vgap;

                      dim.width  = d.width+hgap;

                }
            }

            return dim;
        }
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param target the component to be laid out
     * @see #preferredLayoutSize
     */
    @Override
    public Dimension minimumLayoutSize(Container target) {
        return getLayoutDimension(target);
    }

    /**
     * Lays out the specified container.
     *
     * @param target the container to be laid out
     */
    @Override
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int maxwidth = target.getWidth() - (insets.left + insets.right + hgap*2);
            int nmembers = target.getComponentCount();
            int x = 0, y = insets.top + vgap;

            boolean ltr = target.getComponentOrientation().isLeftToRight();




            for (int i = 0 ; i < nmembers ; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(d.width, d.height);

                    m.setLocation(x , y);
                    y+=d.height+vgap;

                }
            }

        }
    }
}
