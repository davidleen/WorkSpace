package com.giants3.android.reader.domain;

import com.giants3.reader.noEntity.RemoteData;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by davidleen29 on 2018/11/23.
 */

public class RemoteDataParser<T> extends DataParser<RemoteData<T>> {



    public RemoteDataParser(Class<?> paraClass)
    {
        super(paraClass);

    }

    @Override
    protected Type getResolveType(Class<?> paraClass) {

        return new RemoteDateParameterizedType(paraClass);

    }
}
