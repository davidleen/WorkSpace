package com.giants3.android.kit;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.giants3.android.frame.R;
import com.giants3.android.frame.util.Log;
/**
 *  自下往上的弹出对话框
 *
 *  统一处理一些公共方法， popup 自下往上
 *
 */
public abstract class DownUpPopupWindow<VH extends DownUpPopupWindow.ViewHolder> extends AbsPopupWindow<VH> {



    public DownUpPopupWindow(Context context) {
        super(context);


    }


    @Override
    public int getAnimationStyle() {
        return R.style.popupwindow;
    }



    public interface ViewHolder extends AbsPopupWindow.ViewHolder {


    }


    public void show(){
        try {
            showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }catch (Throwable t)
        {
            Log.e(t);
        }
    }


}
