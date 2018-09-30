package com.giants3.report.jasper;

import com.giants3.hd.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

public class ReportData<T> extends HashMap<String, Object> {


    T data;
    private Class<T> tClass;
    Field[] fields;

    String[] fieldNames;



    public ReportData(T data, Class<T> tClass) {
        this.data = data;
        this.tClass = tClass;
        fields = tClass.getFields();

        final int length = fields.length;
        fieldNames = new String[length];

        for (int i = 0; i < length; i++) {
            fieldNames[i] = fields[i].getName();
        }


    }

    @Override
    public Object get(Object key) {
        Object result = super.get(key);
        if (result == null) {


            final int length = fieldNames.length;
            for (int i = 0; i < length; i++) {
                    if (fieldNames[i].equals(key)) {
                        try {
                            result= fields[i].get(data);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

            }

    return result;


    }

    @Override
    public boolean containsKey(Object key) {

        boolean result = super.containsKey(key);
        if (!result) {

            for (String temp : fieldNames) {
                if (temp.equals(key)) {
                    result = true;
                    break;
                }
            }

        }
        return result;
    }


}