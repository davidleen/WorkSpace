package com.giants3.android.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.giants3.ClassHelper;

public     class  RecycleViewBindingHolder   extends RecyclerView.ViewHolder {
    public  ViewBinding viewBinding;

    public RecycleViewBindingHolder(ViewBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }
}
