package com.giants3.hd.android;

import android.view.LayoutInflater;

//import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ViewBindHelper {

//    protected <T extends ViewBinding> T createViewBindAuto(Object o,LayoutInflater layoutInflater) {
//
//        T restult = null;
//        Type genericSuperclass = o.getClass().getGenericSuperclass();
//        try {
//            if (genericSuperclass instanceof ParameterizedType) {
//                Type actualTypeArgument = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
//
//                if (actualTypeArgument instanceof Class) {
//                    Class<T> clas = (Class<T>) actualTypeArgument;
//                    Method inflate = clas.getMethod("inflate", LayoutInflater.class);
//                    restult = (T) inflate.invoke(null, layoutInflater);
//
//                }
//
//
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return restult;
//
//
//    }
//
//
//    protected <T extends ViewBinding> T createViewBindAuto(Class<T> viewBindingClass,LayoutInflater layoutInflater) {
//
//        T result=null;
//
//        try {
//            Method inflate = viewBindingClass.getMethod("inflate", LayoutInflater.class);
//            result = (T) inflate.invoke(null, layoutInflater);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }

}