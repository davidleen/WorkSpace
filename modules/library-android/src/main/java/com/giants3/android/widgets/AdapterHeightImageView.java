package com.giants3.android.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


/**
 * 自适应根据图片比例，调整控件高度。
 *
 */
public class AdapterHeightImageView extends androidx.appcompat.widget.AppCompatImageView {


    public AdapterHeightImageView(Context context) {
        super(context);
    }

    public AdapterHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int lastMeasureWidthAtMost;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        Drawable drawable = getDrawable();
        if (drawable == null||!(drawable instanceof BitmapDrawable)) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }


//
//        //根据宽度，按比例扩展
        final int specMode = MeasureSpec.getMode(widthMeasureSpec);
        final int specSize = MeasureSpec.getSize(widthMeasureSpec);
        int measuredWidth = -1;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                measuredWidth =specSize;
                break;
        }
        if(measuredWidth==-1)
        {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            return;
        }

        int measuredHeight = measuredWidth * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
//        if (BuildConfig.DEBUG)
//            Log.e("mode："+specMode+"measuredWidth:" + measuredWidth + ",measuredHeight:" + measuredHeight);

        setMeasuredDimension(measuredWidth, measuredHeight);


    }
}
