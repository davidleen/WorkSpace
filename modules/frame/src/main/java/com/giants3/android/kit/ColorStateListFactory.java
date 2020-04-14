package com.giants3.android.kit;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.StateSet;

public class ColorStateListFactory {



    public static final ColorStateList createPressColor(int  colorNormal, int colorPressed) {


        return createStateColorList(new int[][]{ new int[]{android.R.attr.state_pressed
        },StateSet.WILD_CARD}, new int[]{colorPressed,colorNormal});


    }



    public static final ColorStateList createEnableColor(int  colorNormal, int colorEnable) {


        return createStateColorList(new int[][]{ new int[]{android.R.attr.state_enabled
        },StateSet.WILD_CARD}, new int[]{ colorEnable,colorNormal});


    }
    public static final ColorStateList createSelectColor(int  colorNormal, int colorSelect) {


        return createStateColorList(new int[][]{ new int[]{android.R.attr.state_selected
        },StateSet.WILD_CARD}, new int[]{ colorSelect,colorNormal});


    }

    public static final ColorStateList createStateColorList(int[][] stats, int[] colors) {

        ColorStateList stateListDrawable = new ColorStateList(  stats,colors);

        return stateListDrawable;


    }


    /**
     *   两种颜色中切换（取插值）
     * @param factor   分布点 [0-1]
     * @param color1
     * @param color2
     * @return
     */
    public static int transformColor(float factor,int color1,int color2)
    {
        int r = (int) (Color.red(color1) * (1 - factor) + Color.red(color2) * factor);
        int g = (int) (Color.green(color1) * (1 - factor) + Color.green(color2) * factor);
        int b = (int) (Color.blue(color1) * (1 - factor) + Color.blue(color2) * factor);

        return Color.rgb(r, g, b);

    }

    /**
     * 获取指定透明度的颜色值
     * @param color
     * @param opaque  不透明度[0-1]
     * @return
     */
    public static int alphaColor(int color,float opaque)
    {


        int opaqueInt= (int) (opaque*255);
        int newColor = (((opaqueInt) << 24) & 0xff000000) // 透明度通道
                + (color & 0x00ffffff);// 色值通道

        return newColor;
    }




}
