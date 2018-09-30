package com.giants3.report.jasper;

import java.util.HashMap;

public class ReportData<T> extends HashMap<String, Object> {


    T data;
    private Class<T> tClass;

    public ReportData(T data, Class<T> tClass) {
        this.data = data;
        this.tClass = tClass;
    }

    @Override
    public Object get(Object key) {

        try {
            return tClass.getField(key.toString()).get(data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return super.get(key);

    }

    @Override
    public boolean containsKey(Object key) {

        try {
            return tClass.getField(key.toString()) != null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return super.containsKey(key);
    }
}