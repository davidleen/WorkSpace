package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.R;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.helper.ImageViewerHelper;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.ErpOrderItem;

import butterknife.Bind;

/**
 * Created by david on 2016/2/14.
 */
public class OrderItemListAdapter
        extends AbstractAdapter<ErpOrderItem> {






    public OrderItemListAdapter(Context context) {
        super(context);


    }

    @Override
    protected Bindable<ErpOrderItem> createViewHolder(int itemViewType) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.list_item_order_item, null, false);
        return new ViewHolder(view);
    }






    public class ViewHolder  extends BaseBindable<ErpOrderItem> {

        @Bind(R.id.photo)
        public ImageView photo;
        @Bind(R.id.os_no)
        public   TextView os_no;
        @Bind(R.id.productName)
        public   TextView productName;
        @Bind(R.id.pVersion)
        public   TextView pVersion;
        @Bind(R.id.batNo)
        public   TextView batNo;
        public ErpOrderItem mItem;

        public ViewHolder(View view) {
            super(view);

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageViewerHelper.view(v.getContext(), (String) v.getTag());
                }
            });

        }

        @Override
        public void bindData(AbstractAdapter<ErpOrderItem> adapter, ErpOrderItem data, int position) {




            String uri = HttpUrl.completeUrl(data.url);
            ImageLoaderFactory.getInstance().displayImage(uri,photo);
            photo.setTag(uri);
            mItem = data;
            os_no.setText(data.os_no);
            productName.setText(data.prd_name);
            pVersion.setText(data.id_no);
            mView.setBackgroundResource(R.drawable.list_item_bg_selector);
            batNo.setText(data.bat_no);
        }



    }
}
