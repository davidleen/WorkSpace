package com.giants3.android.kit;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.giants3.android.frame.util.AndroidCompatUtils;
import com.giants3.android.frame.util.Log;

public  abstract class AbsPopupWindow <VH extends AbsPopupWindow.ViewHolder> extends PopupWindow {
    private VH wh;
    protected Context mContext;


    public AbsPopupWindow(Context context) {
        super(context);
        this.mContext = context;

        wh = createViewHolder(context);

        View contentView =wh.getRoot();
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        if(canAutoDismiss()) {
            setFocusable(true);
            contentView.setFocusable(false);
			setOutsideTouchable(true);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }else
        {
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(false);// 这个很重要
            setOutsideTouchable(false);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;

                }
            });


        }
        setAnimationStyle(getAnimationStyle());
        setBackgroundDrawable(new BitmapDrawable());
        // 处理输入框焦点问题。 获取焦点，弹出软键盘，重新调整popupwindow的大小。
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);





//    设置并保存背景透明度
        setToppestAlpha(128);



        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 恢复背景透明度
                setToppestAlpha(0);
                AbsPopupWindow.this.onDismiss();

            }
        });

    }


    public  abstract int getAnimationStyle();


    protected void setToppestAlpha(int alpha)
    {

        if(mContext instanceof Activity)
        {
            Activity toppest= ((Activity) mContext);

            while (toppest.getParent()!=null)
            {
                toppest=toppest.getParent();
            }

            if(toppest instanceof ActivityAlphaable)
            {
                ((ActivityAlphaable) toppest).setAlpha(alpha);
            }


        }

    }



    /**
     * dismiss 回调。
     */
    protected void onDismiss()
    {}



    /**
     * 创建界面绑定对象
     * @return
     */
    protected abstract VH createViewHolder(Context context);
    /**
     * 获取界面绑定对象
     * @return
     */
    protected  VH getViewHolder()
    {
        return wh;
    }







    public interface ViewHolder {


        View getRoot();
    }


    public class   SimpleViewHolder  implements  ViewHolder{

        private View view;

        public SimpleViewHolder(View view)
        {

            this.view = view;
        }

        @Override
        public View getRoot()
        {
            return view;
        }
    }



    public void show(){
        try {
            showAtLocation(((Activity) mContext).getWindow().getDecorView(),getLayoutGravity(), 0, 0);
        }catch (Throwable t)
        {
            Log.e(t);
        }
    }




    protected  boolean canAutoDismiss()
    {
        return true;
    }


    protected  int getLayoutGravity()
    {
        return Gravity.CENTER;
    }




}
