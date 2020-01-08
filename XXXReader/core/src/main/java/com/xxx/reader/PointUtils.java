package com.xxx.reader;

import android.graphics.PointF;

import com.giants3.android.frame.util.Log;

public class PointUtils {

    public static void middle(PointF outPut, PointF... points)
    {

        outPut.set(0,0);
        for (PointF pointF:points)
        {
            outPut.x+=pointF.x;
            outPut.y+=pointF.y;

        }
        outPut.x/=points.length;
        outPut.y/=points.length;


    }


    /**
     * 求解直线touch,control和直线bezierHorizontalStart,bezierVerticalStart的交点坐标
     */
    public static void calculateCross(PointF outPut, PointF p1, PointF p2, PointF s1, PointF s2) {
        // 二元函数通式： y=ax+b
        float a1 = (p2.y - p1.y) / (p2.x - p1.x);
        float b1 = ((p1.x * p2.y) - (p2.x * p1.y)) / (p1.x - p2.x);

        if ((p2.x - p1.x) == 0.0f) {
            Log.e("illegal point");
        }

        float a2 = (s2.y - s1.y) / (s2.x - s1.x);
        float b2 = ((s1.x * s2.y) - (s2.x * s1.y)) / (s1.x - s2.x);

        outPut.x = (b2 - b1) / (a1 - a2);
        outPut.y = a1 * outPut.x + b1;
    }



}
