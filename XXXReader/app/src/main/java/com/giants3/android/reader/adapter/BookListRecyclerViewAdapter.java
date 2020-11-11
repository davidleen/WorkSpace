package com.giants3.android.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.giants3.android.adapter.AbsRecycleViewHolder;
import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.databinding.ListItemBookBinding;
import com.giants3.reader.entity.Book;

public class BookListRecyclerViewAdapter extends AbstractRecyclerAdapter<BookListRecyclerViewAdapter.ViewHolder, Book> {


    public BookListRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemBookBinding inflate = ListItemBookBinding.inflate(LayoutInflater.from(context));
        return new ViewHolder(inflate);
    }


    public static class ViewHolder  extends AbsRecycleViewHolder<Book>
    {

        private com.giants3.android.reader.databinding.ListItemBookBinding viewBinding;

        public ViewHolder(@NonNull ListItemBookBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        @Override
        public void bindData(Book data, int position) {



            viewBinding.name.setText(data.name);

        }


    }
}
