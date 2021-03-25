package com.giants3.android.reader.vm;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.giants3.android.reader.HttpUrl;
import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.Chapter;

public class ChapterListViewModel extends BaseListViewModel<Chapter> {


    long bookId;

    public ChapterListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);


        bookId=intent.getLongExtra("booId",0);
    }

    @Override
    protected String getListUrl() {
        return HttpUrl.getChapterList(bookId);
    }


}
