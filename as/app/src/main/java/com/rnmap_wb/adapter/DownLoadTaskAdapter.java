package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.rnmap_wb.R;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.android.entity.DownloadTask;

import butterknife.Bind;

public class DownLoadTaskAdapter extends AbstractAdapter<DownloadTask> implements View.OnClickListener {
    private TaskStateChangeListener listener;

    public DownLoadTaskAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<DownloadTask> createViewHolder(int itemViewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_downloadtask, null));
    }

    public void updateTaskState(long taskId, float percent, int downloadCount, int totalCount) {


        for (DownloadTask task : getDatas())

        {

            if (task.getId() == taskId) {
                task.setCount(totalCount);
                task.setDownloadedCount(downloadCount);
                task.setPercent(percent);
            }
        }
        notifyDataSetChanged();


    }

    public  void setOnTaskStateChangeListener(TaskStateChangeListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {

        DownloadTask task= (DownloadTask) v.getTag();

        if(listener!=null) listener.onTaskStateChange(task);


    }


    public static class ViewHolder extends AbstractViewHolder<DownloadTask> {
        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.percent)
        TextView percent;

        @Bind(R.id.totalCount)
        TextView totalCount;

        @Bind(R.id.downloadCount)
        TextView downloadCount;

        @Bind(R.id.state)
        TextView state;

        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(AbstractAdapter<DownloadTask> adapter, DownloadTask data, int position) {


            name.setText(data.getName());
            percent.setText(String.valueOf((int)(data.getPercent()*100)+"%"));
            downloadCount.setText(String.valueOf(data.downloadedCount));
            totalCount.setText(String.valueOf(data.count));

            state.setText(data.getState() == 0 ? "正在下载" : data.getState() == 1 ? "已经暂停" : "已完成");
            state.setTag(data);
            state.setOnClickListener((View.OnClickListener) adapter);


        }
    }
    public static interface  TaskStateChangeListener
    {
         void  onTaskStateChange(DownloadTask downloadTask);
    }
}
