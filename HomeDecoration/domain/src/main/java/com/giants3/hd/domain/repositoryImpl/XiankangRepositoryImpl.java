package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.XiankangRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import com.google.inject.Guice;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 2015/10/13.
 */
public class XiankangRepositoryImpl implements XiankangRepository {
    @Override
    public Observable<Void> updateXiankang() {



        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {



                ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
                try {
                    RemoteData<Void> remoteData= apiManager.updateXiankang();

                    if(remoteData.isSuccess())
                    {


                        subscriber.onNext(   (Void)null );
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
