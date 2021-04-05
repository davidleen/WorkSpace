package com.giants3.android.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

public class AndroidCompatUtils {

    public static Activity findActivityFromView(View v)
    {
        if(v== null) return null;
        return findActivityFromContext(v.getContext());
    }




    public   static  Activity findActivityFromContext(Context context)
    {
        if(context instanceof Activity) return (Activity) context;
        if(context instanceof ContextWrapper)
        {
            return findActivityFromContext(((ContextWrapper)context).getBaseContext());
        }
        return null;

    }
}
