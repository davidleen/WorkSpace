package com.giants3.android.reader.vm;

import android.content.Intent;

import com.giants3.android.reader.HttpUrl;
import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.Chapter;

public class ChapterListViewModel extends BaseListViewModel<Chapter> {


    long bookId;
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
