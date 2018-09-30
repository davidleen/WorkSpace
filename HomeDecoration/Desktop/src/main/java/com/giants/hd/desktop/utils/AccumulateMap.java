package com.giants.hd.desktop.utils;

import java.util.HashMap;

/**
 * Created by davidleen29 on 2015/8/18.
 */
public class AccumulateMap extends HashMap<String, Integer> {



    public Integer get(String key) {
        Integer integer= super.get(key);
        return integer==null?0:integer.intValue();
    }


    public void accumulate(String key)
    {
     int value=   get(key);

        put(key,value+1);

    }
}
