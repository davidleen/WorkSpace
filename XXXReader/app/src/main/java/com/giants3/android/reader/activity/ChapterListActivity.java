package com.giants3.android.reader.activity;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.adapter.BookListRecyclerViewAdapter;
import com.giants3.android.reader.vm.BookListViewModel;
import com.giants3.android.reader.vm.ChapterListViewModel;
import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.Chapter;
import com.github.mzule.activityrouter.annotation.Router;

@Router(value = "chapterList",longParams = "bookId")
public class ChapterListActivity extends BaseListViewModelActivity<ChapterListViewModel, Chapter> {

//
//    @Override
//    protected Class<BookListViewModel> getViewModelClass() {
//        return BookListViewModel.class;
//    }


    @Override
    protected AbstractRecyclerAdapter createAdapter() {
        return new BookListRecyclerViewAdapter(this);
    }



    @Override
    public void onItemClick(AbstractRecyclerAdapter adapter, Chapter item, int position) {







    }
}
