package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.giants3.hd.android.R;

import butterknife.BindView;


/**
 * Created by davidleen29 on 2017/6/23.
 */

public class WorkFLowMainMenuAdapter extends AbstractAdapter<WorkFLowMainMenuAdapter.MenuItem> {


    public WorkFLowMainMenuAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<MenuItem> createViewHolder(int itemViewType) {

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.list_item_main_menu, null, false);
        return new WorkFLowMainMenuAdapter.ViewHolder(view);

    }




    public static class ViewHolder extends AbstractViewHolder<MenuItem> {

         @BindView(R.id.text)
        public TextView text;
         @BindView(R.id.countText)
        public TextView countText;

        public String mItem;

        public ViewHolder(View view) {
            super(view);
        }


        @Override
        public void bindData(AbstractAdapter<MenuItem> adapter, MenuItem data, int position) {
            text.setText(data.title);

            countText.setVisibility(data.msgCount>0?View.VISIBLE:View.GONE);
            countText.setText(String.valueOf(data.msgCount));

        }


        public void setMessageCount(int count) {


            countText.setText(count>99?"99+":String.valueOf(count));
            countText.setVisibility(count>0?View.VISIBLE:View.GONE);
        }
    }

    public static class MenuItem
    {

        public String title;
        public int msgCount;
        public String fragmentClass;
    }
}

