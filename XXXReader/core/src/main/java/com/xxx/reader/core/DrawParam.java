package com.xxx.reader.core;

import java.util.Arrays;

/**
 * 绘制参数
 * 宽高四边padding
 * Created by davidleen29 on 2017/8/28.
 */

public class DrawParam {

    public int width;
    public int height;
    public float[] padding;

    public DrawParam()
    {}

    public DrawParam(DrawParam temp)
    {
        width=temp.width;
        height=temp.height;
        padding= temp.padding==null?null:Arrays.copyOf(temp.padding,temp.padding.length);
    }
}
