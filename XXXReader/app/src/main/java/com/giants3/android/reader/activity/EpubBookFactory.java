package com.giants3.android.reader.activity;

import com.giants3.reader.book.EpubBook;
import com.xxx.reader.book.BookFactory;

public class EpubBookFactory implements BookFactory<EpubBook> {

    @Override
    public EpubBook create(String path) {


        return new EpubBook(path);
    }
}
