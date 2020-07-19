package com.giants3.android.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewBinder;
import com.giants3.android.adapter.AbstractViewHolder;
import com.giants3.android.reader.databinding.ListItemBookBinding;
import com.giants3.reader.entity.Book;


/**
 * Created by davidleen29 on 2018/11/25.
 */

public class BookListAdapter extends AbstractAdapter<Book> {
    public BookListAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<Book> createViewHolder(int itemViewType) {
        return new BookViewHolder(getContext());
    }


    public static class BookViewHolder extends AbstractViewBinder<Book, ListItemBookBinding> {



        public BookViewHolder(Context context) {
            super(context);
        }

        @Override
        protected ListItemBookBinding createViewBinding(LayoutInflater inflater) {
            return ListItemBookBinding.inflate(inflater);
        }

        @Override
        public void bindData(AbstractAdapter<Book> adapter, Book data, int position) {


            getViewBinding().name.setText(data.name);


        }


    }
}
