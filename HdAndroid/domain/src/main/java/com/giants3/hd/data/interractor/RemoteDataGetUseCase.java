package com.giants3.hd.data.interractor;

import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;

public class RemoteDataGetUseCase<T> extends  RemoteDataUseCase<T> {
    public RemoteDataGetUseCase(String url, Class<T> tClass) {
        super(url, tClass);
    }

    @Override
    protected   RemoteData<T> handleRequest(String url,Class<T> tClass) throws HdException {
        return apiManager.getData(url, tClass);
    }
}
