package com.xxx.reader.text.page;

import com.xxx.reader.file.Charset;

import java.util.List;

public class ChapterTitleParaData extends ParaData {


    public List<WordData> wordData;

    /**
     * 字符串编码
     */
    public int code= Charset.UNKOWN;


    public String content;

    public ChapterTitleParaData() {

    }






    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();


        for (WordData lineData : wordData) {
            stringBuilder.append(lineData.toString());
            stringBuilder.append("\n\r");

        }


        return stringBuilder.toString();


    }






}
