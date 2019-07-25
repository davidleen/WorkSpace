package com.giants3.android.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * 所有viewholder 的父类
 * 执行控件绑定
 * @param <D>
 */
public abstract class AbstractViewHolder<D> implements AbstractAdapter.Bindable<D> {

    protected View v;

    public AbstractViewHolder(View v) {
        this.v = v;
        try {
            ButterKnife.bind(this, v);
        }catch (Throwable t)
        {
            t.printStackTrace();
        }
    }


    @Override
    public View getContentView() {
        return v;
    }
}