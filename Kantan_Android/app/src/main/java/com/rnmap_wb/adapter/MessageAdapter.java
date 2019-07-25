package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.rnmap_wb.R;
import com.rnmap_wb.android.data.TaskMessage;

import butterknife.Bind;

public class MessageAdapter extends AbstractAdapter<TaskMessage> {
    private View.OnClickListener listener;

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<TaskMessage> createViewHolder(int itemViewType) {
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.list_item_task_message, null));
        if(listener!=null)
            viewHolder.view.setOnClickListener(listener);
        return viewHolder;
    }


    public class ViewHolder extends AbstractViewHolder<TaskMessage> {

        @Bind(R.id.receiveTime)
        TextView receiveTime;
        @Bind(R.id.task_name)
        TextView task_name;
        @Bind(R.id.readPoint)
        View readPoint;
        @Bind(R.id.view)
        View view;

        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(AbstractAdapter<TaskMessage> adapter, TaskMessage data, int position) {

            task_name.setText(data.task.name);
            readPoint.setVisibility(data.read ? View.GONE : View.VISIBLE);
            receiveTime.setText(data.created);
            view.setTag(data);
        }
    }

    public void setOnViewClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }
}
