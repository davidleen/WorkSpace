package com.giants3.android.reader.activity;

import com.xxx.reader.book.BookFactory;
import com.xxx.reader.book.TextBook;
import com.xxx.reader.book.TextChapter;

public class TextBookFactory implements BookFactory<TextBook> {
    @Override
    public TextBook create(String path) {
        TextBook  textBook = new TextBook() ;
        TextChapter textChapter=new TextChapter(path);
        textBook.addChapter(textChapter);
       return  textBook;
    }
}
