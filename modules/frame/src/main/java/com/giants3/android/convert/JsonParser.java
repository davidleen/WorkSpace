package com.giants3.android.convert;

public interface JsonParser {


    public <T> String toJson(T object);
    public <T> T fromJson(String json,Class<T>  tClass);
}
