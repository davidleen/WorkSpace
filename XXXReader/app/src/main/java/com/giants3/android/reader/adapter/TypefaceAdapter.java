package com.giants3.android.reader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.giants3.android.adapter.AbsRecycleViewHolder;
import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.image.ImageLoaderFactory;
import com.giants3.android.reader.databinding.ListItemTypefaceBinding;
import com.giants3.android.reader.scheme.TypefaceEntity;
import com.xxx.reader.turnner.sim.SettingContent;

public class TypefaceAdapter  extends AbstractRecyclerAdapter< TypefaceAdapter.ViewHolder,TypefaceEntity> {


    public TypefaceAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public TypefaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListItemTypefaceBinding.inflate(inflater,parent,false),this);
    }




    public static class ViewHolder extends AbsRecycleViewHolder<TypefaceEntity>
    {

        private com.giants3.android.reader.databinding.ListItemTypefaceBinding viewBinding;
        private AbstractRecyclerAdapter adapter;

        public ViewHolder(ListItemTypefaceBinding viewBinding,AbstractRecyclerAdapter adapter) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.adapter = adapter;
        }

        @Override
        public void bindData(TypefaceEntity data, int position) {

            viewBinding.name.setText(data.getTitle());
            ImageLoaderFactory.getInstance().displayImage(data.getThumb(),viewBinding.thumb);

            viewBinding.select.setVisibility(adapter.isSelected(data)? View.VISIBLE:View.GONE);
        }
    }
}
