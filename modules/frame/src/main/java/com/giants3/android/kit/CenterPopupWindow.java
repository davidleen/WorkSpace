package com.giants3.android.kit;

import android.content.Context;

import com.giants3.android.frame.R;


public abstract class CenterPopupWindow<V extends AbsPopupWindow.ViewHolder> extends AbsPopupWindow<V> {


    public CenterPopupWindow(Context context) {
        super(context);
    }

    @Override
    public int getAnimationStyle() {
        return R.anim.popup_enter;
    }


}
