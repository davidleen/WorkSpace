package com.giants3.hd.utils.pools;

/**
 * 对象工厂接口
 */

public interface ObjectFactory<T> {
    public T newObject();
}