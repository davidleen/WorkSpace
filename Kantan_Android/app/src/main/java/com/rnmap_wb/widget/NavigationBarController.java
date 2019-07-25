package com.rnmap_wb.widget;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rnmap_wb.R;
import com.rnmap_wb.immersive.SmartBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationBarController {

    @Bind(R.id.leftView)
    public ImageView leftView;
    @Bind(R.id.rightView)
    public ImageView rightView;
    @Bind(R.id.titleView)
    public TextView titleView;
    @Bind(R.id.navigation)
    public View navigation;

    public NavigationBarController(Activity activity) {
        try {
            ButterKnife.bind(this, activity);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        navigation.setPadding(0,SmartBarUtils.getNavigationBarPaddingTop(activity),0,0);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setLeftView(@DrawableRes int res, View.OnClickListener onClickListener) {
        leftView.setImageResource(res);
        leftView.setVisibility(res==0?View.INVISIBLE:View.VISIBLE);
        leftView.setOnClickListener(onClickListener);
    }

    public void setRightView(@DrawableRes int res) {
        rightView.setImageResource(res);
    }


    public void setVisible(boolean visible)
    {

        navigation.setVisibility(visible?View.VISIBLE:View.GONE);

    }
}
