package com.giants3.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;

import androidx.viewbinding.ViewBinding;



public abstract class AbstractViewBinder<D,V extends ViewBinding> implements AbstractAdapter.Bindable<D> {


    private Context context;

    private  V binding;
    public AbstractViewBinder(Context context)
    {

        this.context = context;
        binding=createViewBinding(LayoutInflater.from(context));

    }
    protected abstract V createViewBinding(LayoutInflater inflater);


    public V getViewBinding()
    {
        return binding;
    }


    @Override
    public View getContentView() {

        return binding.getRoot();
    }
}
