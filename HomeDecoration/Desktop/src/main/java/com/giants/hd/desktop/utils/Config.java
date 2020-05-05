package com.giants.hd.desktop.utils;

/**
 * Created by davidleen29 on 2017/4/4.
 */
public class Config {
    public static boolean DEBUG=false;

    public static void log(String s) {
        if(DEBUG)

        System.out.println(s);

    }
}
