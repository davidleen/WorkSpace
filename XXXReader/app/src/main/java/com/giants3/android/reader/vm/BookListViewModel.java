package com.giants3.android.reader.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.giants3.android.reader.HttpUrl;
import com.giants3.reader.entity.Book;

public class BookListViewModel extends BaseListViewModel<Book> {


    public BookListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected String getListUrl() {
        return HttpUrl.getBookList();
    }


}
