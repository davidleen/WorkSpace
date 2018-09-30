package com.giants3.hd.utils.pools;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象池控制中心
 */
public class PoolCenter {


    private static final Map<Class<?>, ObjectPool<?>> pools = new HashMap<Class<?>, ObjectPool<?>>();


    public synchronized static final ObjectPool getObjectPool(final Class<?> c) {
       return getObjectPool(c,100);

    }

    public synchronized static final ObjectPool getObjectPool(final Class<?> c,int size) {
        ObjectPool result = null;
        if (pools.containsKey(c)) {
            result = (ObjectPool) pools.get(c);
        }

        if (result == null) {
            result = new ObjectPool(getObjectFactory(c), size);
            pools.put(c, result);
        }

        return result;

    }

    private static final Map<Class<?>, ObjectFactory<?>> factorys = new HashMap<Class<?>, ObjectFactory<?>>();

    public synchronized static final ObjectFactory getObjectFactory(
            final Class<?> c) {
        ObjectFactory<?> result = null;
        if (factorys.containsKey(c)) {
            result = factorys.get(c);
        }

        if (result == null) {

            result = new ObjectFactory() {

                @Override
                public Object newObject() {
                    // TODO Auto-generated method stub
                    try {
                        return c.newInstance();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    throw new RuntimeException(new StringBuilder(
                            "error on create new entity class  :").append(c)
                            .append(",check your code, it must have an empty constructor").toString());
                }
            };

            factorys.put(c, result);
        }

        return result;

    }
}
