package com.giants3.android.reader;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.giants3.reader.entity.Book;

import butterknife.Bind;

/**
 * Created by davidleen29 on 2018/11/25.
 */

public class BookListAdapter extends AbstractAdapter<Book> {
    public BookListAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<Book> createViewHolder(int itemViewType) {
        return new BookViewHolder(inflater.inflate(R.layout.list_item_book,null));
    }


    public static class BookViewHolder extends AbstractViewHolder<Book> {

        @Bind(R.id.name)
        TextView name;

        public BookViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(AbstractAdapter<Book> adapter, Book data, int position) {


            name.setText(data.name);


        }
    }
}
