package com.giants3.hd.android.adapter;

import android.content.Context;

import java.util.Map;

/**
 * Map类型数据下发， 适用于用来展示报表类数据，避免建立各种一次性数据结构。
 * Created by davidleen29 on 2018/9/16.
 */
public class MapListAdapter extends ItemListAdapter<Map<String, Object>> {
    public MapListAdapter(Context context) {
        super(context);
    }

    @Override
    public Object getData(String fieldName, Map<String, Object> object) {

        return object.get(fieldName);
    }
}
