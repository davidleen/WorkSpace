package com.giants3.android.reader.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.giants3.ClassHelper;
import com.giants3.android.kit.AbsPopupWindow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public   class ViewBindingHolder  implements AbsPopupWindow.ViewHolder, ViewBinding {


    public ViewBinding viewBinding;
    public ViewBindingHolder(Context context,Class viewBindingClass)
    {

        try {
            Method inflate = viewBindingClass.getMethod("inflate", LayoutInflater.class);
            viewBinding= (ViewBinding) inflate.invoke(null,LayoutInflater.from(context));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    @Override
    public View getRoot() {
        if(viewBinding==null) return null;
        return viewBinding.getRoot();
    }

    public <T>  T getViewBinding()
    {
        return (T) viewBinding;
    }

}
