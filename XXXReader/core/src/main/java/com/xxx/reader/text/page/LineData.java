package com.xxx.reader.text.page;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LineData  extends ElementData{


    public List<WordData> wordDataList;


    public long posStart;
    public long posEnd;




    public LineData() {
        wordDataList = new ArrayList<>();
    }

    public void addWord(WordData wordData) {


        wordDataList.add(wordData);
        height=Math.max(height,wordData.height);
        wordData.y=(height-wordData.height   ) ;

    }


    @NonNull
    @Override
    public String toString() {


        StringBuilder stringBuilder=new StringBuilder();
        if(wordDataList==null)
        {
            return "";
        }

        for (WordData wordData:wordDataList)
        {
            stringBuilder.append(wordData.word);
        }


        stringBuilder.append(super.toString());


        return stringBuilder.toString();
    }


}
