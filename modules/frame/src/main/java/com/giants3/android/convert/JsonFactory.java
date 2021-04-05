package com.giants3.android.convert;

import com.giants3.pools.ObjectFactory;

public class JsonFactory {


    private static ObjectFactory<JsonParser> factory;

    public static  void init(ObjectFactory<JsonParser> factory)
    {

        JsonFactory.factory = factory;
    }



    private static JsonParser instance;
    public static  JsonParser getInstance()
    {

        if(instance==null)
        {
            instance=factory.newObject();
        }

        return instance;
    }

}
