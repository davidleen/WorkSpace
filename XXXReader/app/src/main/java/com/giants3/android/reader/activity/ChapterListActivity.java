package com.giants3.android.reader.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.adapter.BookListRecyclerViewAdapter;
import com.giants3.android.reader.adapter.ChapterListAdapter;
import com.giants3.android.reader.vm.BookListViewModel;
import com.giants3.android.reader.vm.ChapterListViewModel;
import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.Chapter;
import com.github.mzule.activityrouter.annotation.Router;
import com.xxx.reader.book.IChapter;

@Router(value = "chapterList",longParams = "bookId",stringParams = "bookUrl,filePath" )
public class ChapterListActivity extends BaseListViewModelActivity<ChapterListViewModel,IChapter> {




    @Override
    protected AbstractRecyclerAdapter createAdapter() {
        return new ChapterListAdapter(this);
    }



    @Override
    public void onItemClick(AbstractRecyclerAdapter adapter, IChapter item, int position) {







    }
}
