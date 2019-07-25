package com.giants3.yourreader.text.elements;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class PageElement extends Element {


    List<WordElement> wordElements = new ArrayList<>();


    @Override
    public void draw(Canvas canvas, Paint paint) {

        for (WordElement wordElement : wordElements) {
            wordElement.draw(canvas, paint);
        }
    }
}
