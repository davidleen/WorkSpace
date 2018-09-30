package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.UserRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.User;
import com.giants3.hd.exception.HdException;
import com.google.inject.Guice;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public class UserRepositoryImpl implements UserRepository {
    @Override
    public Observable<User> login(final String name, final String password) {

        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {



                ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
                try {
                    RemoteData<User> remoteData= apiManager.login(name, password);

                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData.datas.get(0) );
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

    /**
     * 获取用户列表
     */
    @Override
    public Observable<List<User>> getUserList() {
        return Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {



                ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
                try {
                    RemoteData<User> remoteData= apiManager.readUsers();

                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData.datas );
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
