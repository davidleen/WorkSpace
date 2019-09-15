package com.rnmap_wb.activity.mapwork;

import java.util.Random;

public class TileUrlHelper {


    static boolean abroad=false;
    static  Random random=new Random();
    public static  String getUrl(int x,int y ,int z)
    {




        return getUrl(x,y,z,random.nextInt(4));
    }


    public static  String getUrl(int x,int y ,int z,int mtIndex)
    {



        if(abroad)
        {
            return  String.format("https://mts%d.googleapis.com/vt?lyrs=s&x=%d&y=%d&z=%d", mtIndex, x, y, z);
        }else
            return String.format("https://mt%d.google.cn/vt?hl=zh-CN&gl=CN&lyrs=y&x=%d&y=%d&z=%d",mtIndex, x, y, z);

    }


    public static final int MAX_OFFLINE_ZOOM=21;
    public static final int MIN_OFFLINE_ZOOM=1;
    public static final int MAX_MT_COUNT=4;
}
