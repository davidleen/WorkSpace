package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import com.google.inject.Guice;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/12/12.
 */
public class BaseRepositoryImpl  {


    public BaseRepositoryImpl()
    {

        Guice.createInjector().injectMembers(this);
    }


    /**
     * 统一处理结果
     * @param subscriber
     * @param remoteData
     * @param <T>
     * @return
     */
    public static <T> void handleResult(Subscriber<? super List<T>> subscriber,RemoteData<T> remoteData)
    {




            if(remoteData.isSuccess())
            {
                subscriber.onNext(remoteData.datas );
                subscriber.onCompleted();

            }else
            {
                subscriber.onError(   HdException.create(remoteData.message));

            }



    }


    /**
     * 执行 Observable 统一构建方法
     * @param caller
     * @param <T>
     * @return
     */
    public <T> Observable<RemoteData<T>>   crateObservable(final ApiCaller<T> caller)
    {

        return Observable.create(new Observable.OnSubscribe<RemoteData<T>>() {
            @Override
            public void call(Subscriber<? super RemoteData<T>> subscriber) {


                try {
                    RemoteData<T> remoteData = caller.call();
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }



    interface  ApiCaller<T>
    {
        RemoteData<T> call()throws  HdException;
    }
}
