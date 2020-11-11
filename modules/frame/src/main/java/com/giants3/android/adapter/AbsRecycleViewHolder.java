package com.giants3.android.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;

public abstract class AbsRecycleViewHolder<D > extends RecyclerView.ViewHolder
    {


        public AbsRecycleViewHolder(View itemView) {
            super(itemView);


        }


        public  abstract void bindData( D data,int position);


    }