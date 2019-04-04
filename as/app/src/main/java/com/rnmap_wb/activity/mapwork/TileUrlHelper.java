package com.rnmap_wb.activity.mapwork;

import java.util.Random;

public class TileUrlHelper {


    static boolean abroad=false;
    static  Random random=new Random();
    public static  String getUrl(int x,int y ,int z)
    {



        if(abroad)
        {
            return  String.format("http://mts%d.googleapis.com/vt?lyrs=s&x=%d&y=%d&z=%d", random.nextInt(4), x, y, z);
        }else
            return String.format("http://mt%d.google.cn/vt?hl=zh-CN&gl=CN&s=Gali&lyrs=s&x=%d&y=%d&z=%d", random.nextInt(4), x, y, z);

    }


    public static final int MAX_OFFLINE_ZOOM=21;
    public static final int MIN_OFFLINE_ZOOM=1;
}
