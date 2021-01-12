package com.giants3.android.kit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import android.util.StateSet;


/**
 * 代码构建drable  避免创建大量的drawable.xml 文件。
 */
public class GradientDrawableFactory {


    public static final GradientDrawable create(Context context, int backGroundColor, int cornerRadius) {
        return create(context, backGroundColor, 0, 0, cornerRadius);
    }

    public static final GradientDrawable create(Context context, int backGroundColor, int strokeColor, int strokeWidth, int cornerRadius) {


        // GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.bg_rectangle_blank_solid);
        // GradientDrawable mutate = (GradientDrawable) drawable.mutate();

        GradientDrawable mutate = new GradientDrawable();
        mutate.setCornerRadius(cornerRadius);
        mutate.setColor(backGroundColor);
        mutate.setStroke(strokeWidth, strokeColor);
        return mutate;


    }


    public static final GradientDrawable create(Context context, int[] backGroundColor, GradientDrawable.Orientation orientation, float gradientCenterX, float gradientCenterY, int strokeColor, int strokeWidth, int cornerRadius) {

        GradientDrawable mutate = new GradientDrawable();
        setCornerRadius(mutate, cornerRadius);
        setGradient(mutate, backGroundColor, orientation, gradientCenterX, gradientCenterY);
        mutate.setStroke(strokeWidth, strokeColor);
        return mutate;

    }


    public static final GradientDrawable create(Context context, int[] backGroundColor, GradientDrawable.Orientation orientation, int strokeColor, int strokeWidth, int cornerRadius) {
        return create(context, backGroundColor, orientation, 0.5f, 0.5f, strokeColor, strokeWidth, cornerRadius);


    }


    public static final GradientDrawable create(Context context, int backGroundColor, int strokeColor, int strokeWidth, float[] cornerRadius) {

        // GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.bg_rectangle_blank_solid);
        // GradientDrawable mutate = (GradientDrawable) drawable.mutate();

        GradientDrawable mutate = new GradientDrawable();
        setCornerRadii(mutate, cornerRadius);
        mutate.setColor(backGroundColor);
        mutate.setStroke(strokeWidth, strokeColor);
        return mutate;


    }


    public static void setCornerRadius(GradientDrawable gradientDrawable, int cornerRadius) {
        gradientDrawable.setCornerRadius(cornerRadius);
    }


    public static void setCornerRadii(GradientDrawable gradientDrawable, float[] cornerRadius) {
        gradientDrawable.setCornerRadii(cornerRadius);
    }


    public static void setGradient(GradientDrawable gradientDrawable, int[] backGroundColor, GradientDrawable.Orientation orientation) {

        setGradient(gradientDrawable, backGroundColor, orientation, 0.5f, 0.5f);
    }


    public static void setGradient(GradientDrawable gradientDrawable, int[] backGroundColor, GradientDrawable.Orientation orientation, float gradientCenterX, float gradientCenterY) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            gradientDrawable.setColors(backGroundColor);
            gradientDrawable.setOrientation(orientation);

        } else {
            gradientDrawable.setColor(backGroundColor[0]);
        }
        gradientDrawable.setGradientCenter(gradientCenterX, gradientCenterY);
    }


    public static final Drawable createClicDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = context.getResources().getDrawable( drawableId);

        Drawable normal = drawable.mutate();
        normal.setAlpha(127);
        return createClickDrawable(normal, drawable);
    }

    public static final Drawable createClickDrawable(Drawable normal, Drawable drawablePressed) {


        return createStateDrawable(new int[][]{ new int[]{android.R.attr.state_pressed
        },StateSet.WILD_CARD}, new Drawable[]{ drawablePressed,normal});


    }


    public static final Drawable createStateDrawable(int[][] stats, Drawable[] drawables) {

        StateListDrawable stateListDrawable = new StateListDrawable();
        for (int i = 0; i < stats.length; i++) {
            stateListDrawable.addState(stats[i], drawables[i]);
        }

        return stateListDrawable;


    }


    public static final Drawable createEnableDrawable(Drawable normal, Drawable drawableEnable) {


        return createStateDrawable(new int[][]{ new int[]{android.R.attr.state_enabled
        },StateSet.WILD_CARD}, new Drawable[]{drawableEnable,normal});


    }

    public static final Drawable createSelectDrawable(Drawable normal, Drawable drawableselect) {


        return createStateDrawable(new int[][]{ new int[]{android.R.attr.state_selected
        },StateSet.WILD_CARD}, new Drawable[]{drawableselect,normal});


    }

















}
