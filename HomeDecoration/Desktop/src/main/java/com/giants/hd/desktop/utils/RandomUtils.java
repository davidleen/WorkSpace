package com.giants.hd.desktop.utils;

import java.util.Random;

/**
 * Created by davidleen29 on 2015/7/6.
 */
public class RandomUtils {


    public static Random random = new Random();

    public static  int nextInt(int max)
    {
        return random.nextInt(max);
    }
    public static int nextInt( )
    {
        return random.nextInt( );
    }
}
