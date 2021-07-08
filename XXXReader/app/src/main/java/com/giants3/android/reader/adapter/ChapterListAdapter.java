package com.giants3.android.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.giants3.android.adapter.AbsRecycleViewHolder;
import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.databinding.ListItemBookBinding;


import com.giants3.android.reader.databinding.ListItemChapterBinding;
import com.xxx.reader.book.IChapter;

/**
 * 章节列表
 */
public class ChapterListAdapter extends AbstractRecyclerAdapter<ChapterListAdapter.ViewHolder, IChapter> {


    public ChapterListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemChapterBinding inflate = ListItemChapterBinding.inflate(LayoutInflater.from(context));
        return new ViewHolder(inflate);
    }


    public static class ViewHolder extends AbsRecycleViewHolder<IChapter> {

        private ListItemChapterBinding viewBinding;

        public ViewHolder(@NonNull ListItemChapterBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        @Override
        public void bindData(IChapter data, int position) {


            viewBinding.name.setText(data.getName());

        }


    }
}
