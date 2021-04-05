package com.giants3.android.reader.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbsRecycleViewHolder;
import com.giants3.android.image.ImageLoaderFactory;
import com.giants3.android.reader.R;
import com.xxx.reader.TextScheme;
import com.xxx.reader.TextSchemeContent;

public class TextSchemeViewHolder extends AbsRecycleViewHolder<TextScheme> {
    ImageView theme;
    TextView title;
    View select;

    public TextSchemeViewHolder(View v) {
        super(v);
        theme = v.findViewById(R.id.theme);
        title = v.findViewById(R.id.title);
        select = v.findViewById(R.id.select);
    }

    @Override
    public void bindData(TextScheme data, int position) {
        title.setText(data.getTitle());
        theme.setBackgroundColor(data.getBackgroundColor());

        select.setVisibility(data.getName().equals(TextSchemeContent.getName())?View.VISIBLE:View.GONE);

        if(data.getBackgroundType()==TextScheme.BACKGROUND_TYPE_COLOR)
        {
            theme.setBackgroundColor(data.getBackgroundColor());
        }else
        {
            ImageLoaderFactory.getInstance().displayImage(data.getBackgroundThumbPath(),theme);
        }

    }

}