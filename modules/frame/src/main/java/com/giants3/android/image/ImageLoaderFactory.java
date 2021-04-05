package com.giants3.android.image;

import com.giants3.pools.ObjectFactory;

public class ImageLoaderFactory {

    private static ObjectFactory<ImageLoader> factory;

    public static  void init(ObjectFactory<ImageLoader> factory)
    {

        ImageLoaderFactory.factory = factory;
    }

    private static ImageLoader instance;
    public static  ImageLoader getInstance()
    {

        if(instance==null)
        {
            instance=factory.newObject();
        }

        return instance;
    }
}
