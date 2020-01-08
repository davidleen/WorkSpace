package com.giants3.hd.data.interractor;

import com.google.inject.Guice;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/** 默认用例实现。
 * Created by david on 2015/9/14.
 */
public abstract class DefaultUseCase  extends  UseCase{




    protected DefaultUseCase( ) {
        super(  Schedulers.newThread(), AndroidSchedulers.mainThread());

        Guice.createInjector().injectMembers(this);
    }



    protected abstract Observable buildUseCaseObservable();

}
