package com.rnmap_wb.activity.mapwork.map;

import android.view.View;

import butterknife.ButterKnife;

public abstract class AbsViewHolder<D> {

    private final View v;

    public AbsViewHolder(View v) {
        this.v = v;
        try {
            ButterKnife.bind(this, v);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public abstract void bindData(D data);


    public View getRoot() {
        return v;
    }
}
