package com.giants3.hd.data.interractor;


import com.giants3.hd.data.net.ApiManager;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

public class RemoteDataPostUseCase< T> extends DefaultUseCase {
    private String url;
    private T data;
    public Class<T> tClass;
    @Inject
    ApiManager apiManager;
    public RemoteDataPostUseCase(String url,T data,Class<T> tClass)
    {
        this.url = url;
        this.data = data;
        this.tClass=tClass;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<T>>() {
            @Override
            public void call(Subscriber<? super RemoteData<T>> subscriber) {




                try {
                    RemoteData<T> remoteData= apiManager.postData(url, data, tClass);
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
}
