package com.giants3.yourreader.text.composite;

import com.giants3.yourreader.text.elements.Element;
import com.giants3.yourreader.text.elements.PageElement;
import com.giants3.yourreader.text.elements.WordElement;
import com.xxx.reader.file.LineReader;
import com.xxx.reader.file.ParaReader;

import java.util.ArrayList;
import java.util.List;

public class PageCompositor  {


    public static void composite(ParaReader reader, int filePos, int width, int height,CompositeConfig compositeConfig)
    {


        TextMeasure textMeasure = new TextMeasure() {
            @Override
            public float getTextWidth(char c, int orientation) {
                return 0;
            }
        };



        PageElement lastPage=null;

        while (reader.hasNext())
        {
            String text=reader.readNext();

            ParaCompositor.composite(text, width, compositeConfig.wordGap, compositeConfig.indentCount, compositeConfig.orientation, textMeasure);













        }






    }



    String nextParagraph()
    {

        return "";
    }
    String preParagraph()
    {

        return "";
    }




}
