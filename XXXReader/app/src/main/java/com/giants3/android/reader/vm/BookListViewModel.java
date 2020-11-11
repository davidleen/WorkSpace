package com.giants3.android.reader.vm;

import com.giants3.android.reader.HttpUrl;
import com.giants3.reader.entity.Book;

public class BookListViewModel extends BaseListViewModel<Book> {


    @Override
    protected String getListUrl() {
        return HttpUrl.getComicBookList();
    }


}
