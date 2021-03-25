package com.giants3.android.storage;

/**
 * 键对值接口。
 */
public interface KV {
    boolean getBoolean(String key, boolean defaultValue);

    String getString(String key, String defaultValue);

    float getFloat(String key, float defaultValue);

    int getInt(String key, int defaultValue);

    long getLong(String key, long defaultValue);

    void putBoolean(String key, boolean value);

    void putInt(String key, int value);

    void putString(String key, String value);

    boolean contains(String key);

    void putLong(String key, long value);

    void putFloat(String key, float value);
}
