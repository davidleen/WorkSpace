package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Created by davidleen29 on 2016/7/31.
 */
public class ImageLabel extends JLabel {

    private Image backgroundImage;

    // Some code to initialize the background image.
    // Here, we use the constructor to load the image. This
    // can vary depending on the use case of the panel.
    public ImageLabel(String url)  {

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                backgroundImage =icon.getImage();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        revalidate();
                        repaint();
                    }
                });



            }

            @Override
            public void onError(String message) {
                setText(message);
            }
        }, url, 80, 80);

        setText(url);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(backgroundImage!=null)
        // Draw the background image.
            g.drawImage(backgroundImage, 0, 0, this);
    }
}
