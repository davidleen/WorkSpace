package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;

import javax.swing.*;

/**
 * 展示图片的控件
 * Created by david on 2016/5/11.
 */
public class ImageView extends JLabel {




    public ImageView() {
    }



    public void setImageUrl(String imageUrl) {



        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                 setText("");
                ImageView.this.setIcon(icon);

            }

            @Override
            public void onError(String message) {
                ImageView.this.setIcon(null);
                setText("");

            }
        }, imageUrl, 100,100);
        setText("loading...");
    }


}
