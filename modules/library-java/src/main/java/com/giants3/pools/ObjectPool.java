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
    private int createCount;
    /**
     * 新建对象超过最大缓存数时候，是否新建对象
     */
    private boolean disableOverSizeCreate;

    public ObjectPool(ObjectFactory<T> factory, int maxPoolSize)
    {
        this(factory,maxPoolSize,false);

    }
    public ObjectPool(ObjectFactory<T> factory, int maxCount,boolean disableOverSizeCreate) {
        super();
        this.factory = factory;
        this.maxCount = maxCount;
        objPool = new ArrayList<T>(maxCount);
        this.disableOverSizeCreate = disableOverSizeCreate;
    }

    /**
     * 获取新对豄1�7 if pool has unused obj reuse it or recreate it
     *
     * @return
     */
    @Override
    public T newObject() {
        synchronized (objPool) {
            if (objPool.size() > 0) {
                return objPool.remove(objPool.size() - 1);

            } else {
                //是否超出最大缓存数且不能新建。
                if(disableOverSizeCreate&&createCount>=maxCount)
                {
                    return null;
                }
                else
                {
                    T t = factory.newObject();
                    createCount++;
                    return t;
                }
            }

        }
    }

    /**
     * release an obj make it unuse if no bigger than the maxCount add to pool<list> or simply drop it , let gc help him.
     *
     * @param data
     */
    public void release(T data) {
        if(data==null) return;
        synchronized (objPool) {
            if (objPool.size() < maxCount) {
                objPool.add(data);
            }
            if (objPool.size() > maxCount) {
                objPool.remove(objPool.size() - 1);

            }
            System.out.println(data.getClass()+"free object in pool:"+objPool.size()+",createCount:"+createCount);
        }

    }


    /**
     * release an obj  list make it unuse if no bigger than the maxCount add to pool<list> or simply drop it , let gc help him.
     *
     * @param collection
     */
    public void release(Collection<T> collection) {
        synchronized (objPool) {
            if (objPool.size() < maxCount) {
                objPool.addAll(collection);
            }
        }

    }

    public void clear() {
        synchronized (objPool) {
            objPool.clear();

        }
    }
}

