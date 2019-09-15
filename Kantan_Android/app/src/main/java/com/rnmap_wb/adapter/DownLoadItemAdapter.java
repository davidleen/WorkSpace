package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.TileUrlHelper;
import com.rnmap_wb.android.entity.DownloadItem;

import butterknife.Bind;

public class DownLoadItemAdapter extends AbstractAdapter<DownloadItem> {
    public DownLoadItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected Bindable<DownloadItem> createViewHolder(int itemViewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_downloaditem, null));
    }


    public static class ViewHolder extends AbstractViewHolder<DownloadItem> {
        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.url)
        TextView url;

        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(AbstractAdapter<DownloadItem> adapter, DownloadItem data, int position) {


            name.setText(data.getName());
            url.setText(TileUrlHelper.getUrl(data.getTileX(),data.getTileY(),data.getTileZ(),0));

        }
    }
}
