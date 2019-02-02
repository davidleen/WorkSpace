package com.giants3.yourreader.text;

import android.view.MotionEvent;

import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageBitmap;
import  android.content.Context;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageBitmap extends PageBitmap<TextPageInfo,DrawParam> {


    public TextPageBitmap(Context context,int screenWidth, int screenHeight, IDrawable iDrawable) {
        super(screenWidth, screenHeight, iDrawable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
