package com.giants3.yourreader.text.elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by davidleen29 on 2018/12/30.
 */

public class WordElement extends Element {


    
    int color;
    float size;
    boolean bold;
    //[0,-0.25]
    float skewX;
    //drawPos
    float baseLine;
  public   String word;

  public int x;
  public int y;
    int paraIndex;
    int lineIndex;








    int startIndex;
    int endIndex;

    @Override
    public void draw(Canvas canvas, Paint paint) {



        canvas.drawText(word,x,y,paint);
    }
}
