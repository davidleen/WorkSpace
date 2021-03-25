package com.giants3.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;

public class MKVImpl  implements  KV{

    public static final String HAS_IMPORT = "HAS_IMPORT";

    private Context context;
    MMKV mmkv;
    private String originSharedPreferenceName;


    public MKVImpl(Context  context,String fileName) {
        this.context = context;

        mmkv = MMKV.mmkvWithID(fileName);
        this.originSharedPreferenceName = fileName;
        if (!mmkv.getBoolean(HAS_IMPORT, false)) {
            //执行迁移
            SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            mmkv.importFromSharedPreferences(sharedPreferences);
            sharedPreferences.edit().clear().apply();
            mmkv.putBoolean(HAS_IMPORT, true);
        }

    }
    @Override
    public boolean getBoolean(String key, boolean defaultValue) {


        return mmkv.getBoolean(key, defaultValue);

    }


    @Override
    public String getString(String key, String defaultValue) {


        return mmkv.getString(key, defaultValue);

    }


    @Override
    public float getFloat(String key, float defaultValue) {


        return mmkv.getFloat(key, defaultValue);

    }

    @Override
    public void putFloat(String key, float value) {
          mmkv.putFloat(key, value);
    }

    @Override
    public int getInt(String key, int defaultValue) {


        return mmkv.getInt(key, defaultValue);

    }


    @Override
    public long getLong(String key, long defaultValue) {


        return mmkv.getLong(key, defaultValue);

    }

    @Override
    public void putBoolean(String key, boolean value) {

        mmkv.putBoolean(key, value);
    }

    @Override
    public void putInt(String key, int value) {

        mmkv.putInt(key, value);
    }

    @Override
    public void putString(String key, String value) {
        mmkv.putString(key, value);
    }

    @Override
    public boolean contains(String key) {
        return mmkv.contains(key);
    }

    @Override
    public void putLong(String key, long value) {


        mmkv.putLong(key, value);
        //lastcliptime 在本地lib库中有使用，需保留
        if("lastcliptime".equalsIgnoreCase(key))
        {
            SharedPreferences.Editor edit = context.getSharedPreferences(originSharedPreferenceName, Context.MODE_PRIVATE).edit();
            edit.putLong(key,value);
            edit.apply();
        }
    }
}
