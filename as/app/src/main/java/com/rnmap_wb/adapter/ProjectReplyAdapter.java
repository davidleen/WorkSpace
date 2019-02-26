package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.View;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.rnmap_wb.R;
import com.rnmap_wb.android.data.ProjectReply;

public class ProjectReplyAdapter extends AbstractAdapter<ProjectReply> {
    public ProjectReplyAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<ProjectReply> createViewHolder(int itemViewType) {
        return new ViewHolder(inflater.inflate(R.layout.project_reply_item,null));
    }

    private class ViewHolder extends AbstractViewHolder<ProjectReply>
    {
        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(AbstractAdapter<ProjectReply> adapter, ProjectReply data, int position) {

        }
    }
}
