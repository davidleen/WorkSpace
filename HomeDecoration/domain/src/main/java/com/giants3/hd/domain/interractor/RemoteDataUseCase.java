package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

public abstract class RemoteDataUseCase< T> extends DefaultUseCase {
    private String url;
    public Class<T> tClass;
    @Inject
    ApiManager apiManager;
    public RemoteDataUseCase(String url,Class<T> tClass)
    {
        this.url = url;
        this.tClass=tClass;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<T>>() {
            @Override
            public void call(Subscriber<? super RemoteData<T>> subscriber) {




                try {
                    RemoteData<T> remoteData= handleRequest(url,tClass);
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    protected abstract RemoteData<T> handleRequest(String url,Class<T> tClass) throws HdException  ;
}
