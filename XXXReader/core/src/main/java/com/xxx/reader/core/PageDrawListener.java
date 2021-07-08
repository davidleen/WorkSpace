package com.xxx.reader.core;

import android.graphics.Canvas;
import android.graphics.Path;

/**
 * 页面切换时绘制回调接口
 */
public interface PageDrawListener {


       /**
        * 绘制在上面一页
        * @param canvas
        * @param turnNext
        */
       void drawTop(Canvas canvas, boolean turnNext);

       /**
        * 绘制在下面一页
        * @param canvas
        * @param turnNext
        */
       void drawBottom(Canvas canvas, boolean turnNext);
}
