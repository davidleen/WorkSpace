package com.giants3.hd.noEntity;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/** RemoteData 泛型应用情况下，抓取实际泛型类型类型
 *
 * Created by davidleen29 on 2018/8/13.
 */
public class RemoteDateParameterizedType implements ParameterizedType {

    private Type [] tClasses;

    /**
     *
     * @param tClasses 泛型参数对应的实际类型列表   比如泛型<T,K,J>    T,K,J 实际类型class
     */
    public RemoteDateParameterizedType(Type... tClasses) {
        super();
        for (int i = 0; i < tClasses.length; i++) {
            if( tClasses[i] == Map.class)
            {
                tClasses[i]= new TypeToken<Map<String, String>>() {}.getType();
            }
        }


        this.tClasses = tClasses;
    }


    @Override
    public Type[] getActualTypeArguments() {



        return tClasses;
    }

    @Override
    public Type getRawType() {
        return RemoteData.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
