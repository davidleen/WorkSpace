package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.rnmap_wb.R;
import com.rnmap_wb.android.data.Task;

import butterknife.Bind;

public class HomeTaskAdapter extends AbstractAdapter<Task> {
    public HomeTaskAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<Task> createViewHolder(int itemViewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_task, null));
    }


    public static class ViewHolder extends AbstractViewHolder<Task> {
        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.dir_name)
        TextView dir_name;

        @Bind(R.id.createTime)
        TextView createTime;

//        @Bind(R.id.kml)
//        TextView kml;

        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(AbstractAdapter<Task> adapter, Task data, int position) {


            name.setText(data.name);
            dir_name.setText(String.valueOf(data.dir_name));

            createTime.setText(String.valueOf(data.created));

            Task last = position == 0 ? null : adapter.getItem(position - 1);
            if (last == null || !last.dir_id.equals(data.dir_id)) {
                dir_name.setVisibility(View.VISIBLE);
            } else
                dir_name.setVisibility(View.GONE);

        }
    }
}
