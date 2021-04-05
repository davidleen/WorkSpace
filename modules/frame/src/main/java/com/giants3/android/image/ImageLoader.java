package com.giants3.android.image;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public interface ImageLoader {

    public void displayImage(String url, ImageView imageView);
    public void displayImage(String url, @DrawableRes int res, ImageView imageView);
}
