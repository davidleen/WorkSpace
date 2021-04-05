package com.giants3.android.reader.activity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;


import android.app.Activity;
import android.os.Bundle;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.convert.JsonFactory;

import com.giants3.android.reader.adapter.TypefaceAdapter;
import com.giants3.android.reader.databinding.ActivityTypefaceBinding;
import com.giants3.android.reader.scheme.TypefaceEntity;
import com.giants3.android.reader.vm.TypefaceViewModel;
import com.xxx.reader.turnner.sim.SettingContent;



public class TypefaceActivity extends BaseViewModelActivity<ActivityTypefaceBinding, TypefaceViewModel> {




    TypefaceAdapter typefaceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("字体选择");

        getViewModel().typefaces.observe(this, new Observer<TypefaceEntity[]>() {
            @Override
            public void onChanged(TypefaceEntity[] typefaces) {

                typefaceAdapter.setDataArray(typefaces);

            }
        } );

        typefaceAdapter=new TypefaceAdapter(this);
        typefaceAdapter.setOnItemClickListener(new AbstractRecyclerAdapter.OnItemClickListener<TypefaceEntity>() {
            @Override
            public void onItemClick(TypefaceEntity item, int position) {

                SettingContent.getInstance().setTypeface (JsonFactory.getInstance().toJson(item));
                typefaceAdapter.setSelectItem(item);
                typefaceAdapter.notifyDataSetChanged();
                setResult(Activity.RESULT_OK);




            }
        });
        getViewBinding().typeface.setAdapter(typefaceAdapter);
        getViewBinding().typeface.setLayoutManager(new GridLayoutManager(this,2)
       );

        getViewModel().loadTypefaces();
    }


}