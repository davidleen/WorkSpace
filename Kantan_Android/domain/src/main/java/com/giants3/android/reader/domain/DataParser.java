package com.giants3.android.reader.domain;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by davidleen29 on 2018/11/23.
 */
public class DataParser<T> implements ResultParser<T> {

    private Class<?> tClass;

    public DataParser(Class<?> tClass) {
        this.tClass = tClass;
    }

    @Override
    public T parser(String result) {
        return GsonUtils.fromJson(result, getResolveType(tClass));

    }

    @Override
    public T parser(InputStream inputStream) {
        return GsonUtils.fromInputStream(inputStream, getResolveType(tClass));
    }


    protected Type getResolveType(Class<?> paraClass) {
        return paraClass;
    }
}
