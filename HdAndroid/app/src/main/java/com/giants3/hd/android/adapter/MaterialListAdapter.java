package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.hd.android.R;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.helper.ImageViewerHelper;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.Material;

import butterknife.BindView;

/**
 * 材料列表适配
 * Created by david on 2016/2/14.
 */
public class MaterialListAdapter
        extends AbstractAdapter<Material> {


//    private View.OnClickListener itemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            ViewHolder viewHolder = (ViewHolder) v.getTag();
//
//            Context context = v.getContext();
//            if (context instanceof MaterialListFragment.OnFragmentInteractionListener) {
//                ((MaterialListFragment.OnFragmentInteractionListener) context).onFragmentInteraction(viewHolder.mItem);
//            }
//
//
//        }
//    };


    public MaterialListAdapter(Context context) {
        super(context);


    }

    @Override
    protected Bindable<Material> createViewHolder(int itemViewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_material, null));
        viewHolder.picture.setOnClickListener(imageClickListener);
        return viewHolder;
    }


    public class ViewHolder extends AbstractViewHolder<Material> {
        public Material mItem;


        @BindView(R.id.materialCode)
        public TextView materialCode;
        @BindView(R.id.picture)
        public ImageView picture;
        @BindView(R.id.materialName)
        public TextView materialName;
        @BindView(R.id.materialType)
        public TextView materialType;
        @BindView(R.id.unit)
        public TextView unit;

        public ViewHolder(View view) {
            super(view);

        }


        @Override
        public void bindData(AbstractAdapter<Material> adapter, Material data, int position) {

            mItem = data;
            materialCode.setText(data.code);
            materialName.setText(data.name);
            materialType.setText(data.className);
            unit.setText(data.unitName);
            ImageLoaderFactory.getInstance().displayImage(HttpUrl.completeUrl(data.url), picture);
            picture.setTag(data.url);
        }

        @Override
        public View getContentView() {
            return v;
        }
    }


    private View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String url = (String) v.getTag();
            if(StringUtil.isEmpty(url)) return;
            ImageViewerHelper.view(v.getContext(), url);

        }
    };
}
