package com.giants3.android.reader.domain;

/**
 * Created by davidleen29 on 2018/11/25.
 */

public interface UseCaseHandler<T> {
     void onError(Throwable e);
     void onNext(T object);
     void onCompleted();
}
