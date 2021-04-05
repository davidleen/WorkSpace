package com.giants3.android.reader.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.reader.R;
import com.xxx.reader.TextScheme;

public class TextSchemeAdapter extends AbstractRecyclerAdapter<TextSchemeViewHolder,TextScheme> {


    public TextSchemeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public TextSchemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextSchemeViewHolder(inflater.inflate(R.layout.item_text_theme,null));
    }


}
