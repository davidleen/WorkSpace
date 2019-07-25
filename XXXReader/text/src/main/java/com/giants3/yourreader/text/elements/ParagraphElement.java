package com.giants3.yourreader.text.elements;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by davidleen29 on 2018/12/30.
 */

public class ParagraphElement extends Element {



    public int firstLine=-1;
    public int lastLine=-1;
    TextParam para;
     List<LineElement> lineElements;


    @Override
    public void draw(Canvas canvas, Paint paint) {


    }

    class TextParam
    {
        public String text;
        public float[] xPositions;
        public int lineCount;
        int[] lineHead;
    }
}
