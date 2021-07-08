package com.giants3.android.reader.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.kit.DownUpPopupWindow;
import com.giants3.android.reader.activity.BaseActivity;
import com.giants3.android.reader.adapter.BookListRecyclerViewAdapter;
import com.giants3.android.reader.databinding.PopBookListBinding;
import com.giants3.android.reader.vm.BookListViewModel;
import com.giants3.android.reader.vm.ViewModelHelper;
import com.giants3.reader.entity.Book;
import com.giants3.reader.noEntity.RemoteData;

public class BooksPopupWindow extends DownUpPopupWindow {

    PopBookListBinding binding;
    BookListViewModel bookListViewModel;
    BookListRecyclerViewAdapter adapter;

    public BooksPopupWindow(BaseActivity activity, final BookSelectListener listener) {
        super(activity);

        adapter = new BookListRecyclerViewAdapter(activity);

        binding.books.setAdapter(adapter);
        binding.books.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        bookListViewModel = ViewModelHelper.createViewModel(activity, BookListViewModel.class);
        adapter.setOnItemClickListener(new AbstractRecyclerAdapter.OnItemClickListener<Book>() {
            @Override
            public void onItemClick(Book item, int position) {

                if(listener!=null)
                {
                    listener.onBookPick(item);
                }
                dismiss();

            }
        });

        if (bookListViewModel != null) {
            bookListViewModel.getListResult().observe(activity, new Observer<RemoteData<Book>>() {
                @Override
                public void onChanged(RemoteData<Book> bookRemoteData) {


                    adapter.setDataArray(bookRemoteData.datas);

                }
            });
            bookListViewModel.loadData();
        }




    }


    @Override
    protected View createContent(Context context) {
        binding = PopBookListBinding.inflate(LayoutInflater.from(context));
        return binding.getRoot();
    }



   public  interface  BookSelectListener
    {




        public void onBookPick(Book book);
    }

}
