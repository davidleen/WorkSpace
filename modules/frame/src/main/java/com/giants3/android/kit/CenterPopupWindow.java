package com.giants3.android.kit;

import android.app.Activity;
import android.content.Context;

import com.giants3.android.frame.R;


public abstract class CenterPopupWindow  extends AbsPopupWindow  {


    public CenterPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getAnimationStyle() {
        return R.anim.popup_enter;
    }


}
