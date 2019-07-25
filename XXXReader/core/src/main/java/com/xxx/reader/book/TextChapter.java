package com.xxx.reader.book;

public class TextChapter extends AbstractChapter {

    public TextChapter(  String filePath)
    {
        this("",filePath,"","",0);
    }
    public TextChapter(String url, String filePath, String id, String name, int index) {
        super(url, filePath, id, name, index);
    }
}
