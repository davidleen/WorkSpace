package com.giants3.hd.domain.interractor;

import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;

public class RemoteDataDeleteUseCase<T> extends RemoteDataUseCase<T> {
    public RemoteDataDeleteUseCase(String url, Class<T> tClass) {
        super(url, tClass);
    }

    @Override
    protected RemoteData<T> handleRequest(String url, Class<T> tClass) throws HdException {
        return apiManager.deleteData(url, tClass);
    }
}
