package com.giants3.android.reader.activity;



import android.net.Uri;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.adapter.BookListRecyclerViewAdapter;
import com.giants3.android.reader.vm.BookListViewModel;
import com.giants3.reader.entity.Book;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

@Router(value = "bookList")
public class BookListActivity extends BaseListViewModelActivity<BookListViewModel, Book> {

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(AbstractRecyclerAdapter adapter, Book item, int position) {


//        Intent intent=new Intent(this,ChapterListActivity.class)
//                ;
//
//        intent.putExtra("bookid",item.id);
//        startActivity(intent);

//        Router.build("ChapterList?bokId="+item.id).go(this);


        Routers.openForResult(this, Uri.parse("XXX://chapterList?bookId="+item.id),111);


    }
}
