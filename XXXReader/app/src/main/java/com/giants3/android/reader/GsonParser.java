package com.giants3.android.reader;

import com.giants3.android.convert.JsonParser;
import com.giants3.android.reader.domain.GsonUtils;

public class GsonParser implements JsonParser {
    @Override
    public <T> String toJson(T object) {

       return GsonUtils.toJson(object);
    }

    @Override
    public <T> T fromJson(String json, Class<T> tClass) {

        return GsonUtils.fromJson(json,tClass);
    }
}
