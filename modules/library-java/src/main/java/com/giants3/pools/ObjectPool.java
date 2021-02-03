package com.giants3.pools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象池
 */


public class ObjectPool<T> implements ObjectFactory<T> {

    private ObjectFactory<T> factory;
    private int maxCount;
    private List<T> objPool;

    public ObjectPool(ObjectFactory<T> factory, int maxCount) {
        super();
        this.factory = factory;
        this.maxCount = maxCount;
        objPool = new ArrayList<T>(maxCount);
    }

    /**
     * 获取新对豄1�7 if pool has unused obj reuse it or recreate it
     *
     * @return
     */
    @Override
    public T newObject() {

        if (objPool.size() > 0) {
            return objPool.remove(objPool.size() - 1);

        } else {
            if (factory != null)
                return factory.newObject();
            else
                return null;
        }

    }

    /**
     * release an obj make it unuse if no bigger than the maxCount add to pool<list> or simply drop it , let gc help him.
     *
     * @param data
     */
    public void release(T data) {
        if (objPool.size() < maxCount) {
            objPool.add(data);
        }

    }


    /**
     * release an obj  list make it unuse if no bigger than the maxCount add to pool<list> or simply drop it , let gc help him.
     *
     * @param collection
     */
    public void release(Collection<T> collection) {
        if (objPool.size() < maxCount) {
            objPool.addAll(collection);
        }

    }

    public void clear() {
        objPool.clear();

    }
}

