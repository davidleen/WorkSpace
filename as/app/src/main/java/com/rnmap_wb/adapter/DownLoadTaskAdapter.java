package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.giants3.android.frame.util.Utils;
import com.rnmap_wb.R;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.widget.RingBar;

import butterknife.Bind;

import static com.rnmap_wb.android.entity.DownloadTask.STATE_COMPLETE;
import static com.rnmap_wb.android.entity.DownloadTask.STATE_DOWNLOADING;

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


        boolean update = false;
        for (DownloadTask task : getDatas())

        {

            if (task.getId() == taskId) {
                task.setCount(totalCount);
                task.setDownloadedCount(downloadCount);
                task.setPercent(percent);
                if (downloadCount >= totalCount) {
                    task.setState(DownloadTask.STATE_COMPLETE);
                }
                update = true;

            }
        }
        if (update)
            notifyDataSetChanged();


    }

    public void setOnTaskStateChangeListener(TaskStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {


        DownloadTask task = (DownloadTask) v.getTag();

        if (listener != null) listener.onTaskStateChange(task);


    }


    public static class ViewHolder extends AbstractViewHolder<DownloadTask> {
        @Bind(R.id.name)
        TextView name;


        @Bind(R.id.panel_download)
        View panel_download;


        @Bind(R.id.msg_download)
        TextView msg_download;


        @Bind(R.id.totalCount)
        TextView totalCount;


        @Bind(R.id.txt_complete)
        View txt_complete;


        @Bind(R.id.switchState)
        ImageView switchState;

        @Bind(R.id.zoomlevel)
        TextView zoomlevel;

        @Bind(R.id.downloadCount)
        TextView downloadCount;

        @Bind(R.id.progress)
        RingBar progress;

        public ViewHolder(View v) {
            super(v);

            progress.setRingWidth(Utils.dp2px(2));
            progress.setRingColor(v.getResources().getColor(R.color.uniform_1));
        }

        @Override
        public void bindData(AbstractAdapter<DownloadTask> adapter, DownloadTask data, int position) {


            name.setText(data.getName());

            downloadCount.setText(String.valueOf(data.downloadedCount));
            totalCount.setText(String.valueOf(data.count));
            zoomlevel.setText(data.getFromZoom() + "~" + data.getToZoom());
//            state.setText(data.getState() == 0 ? "正在下载" : data.getState() == 1 ? "已经暂停" : "已完成");
//            state.setTag(data);
            // state.setOnClickListener((View.OnClickListener) adapter);

            txt_complete.setVisibility(data.getState() == STATE_COMPLETE ? View.VISIBLE : View.GONE);
            panel_download.setVisibility(data.getState() != STATE_COMPLETE ? View.VISIBLE : View.GONE);

            boolean downloading = data.getState() == STATE_DOWNLOADING;
            msg_download.setText(downloading ? "正在下载" : "下载暂停");

            panel_download.setSelected(downloading);
            switchState.setImageResource(downloading ? R.drawable.icon_state_pause : R.drawable.icon_state_download);

            panel_download.setOnClickListener((View.OnClickListener) adapter);
            panel_download.setTag(data);
            progress.setProgress(data.percent);

        }
    }

    public static interface TaskStateChangeListener {
        void onTaskStateChange(DownloadTask downloadTask);
    }
}
