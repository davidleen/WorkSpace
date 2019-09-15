package com.giants3.hd.data.utils;

import com.giants3.hd.exception.HdException;

import java.lang.reflect.Type;

/**
 * 辅助类
 * Created by davidleen29 on 2015/7/6.
 */
public class GsonUtils {


    public static String toJson(Object o) {

        return com.giants3.hd.utils.GsonUtils.toJson(o);


    }


    public static <T> T fromJson(String json, Type typeOfT)   {

        return com.giants3.hd.utils.GsonUtils.fromJson(json, typeOfT);

    }


}
