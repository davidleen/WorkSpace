package com.giants3.android.reader.domain;

import com.giants3.android.network.ApiConnection;
import com.giants3.net.ResApi;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** 默认用例实现。
 * Created by david on 2015/9/14.
 */
public abstract class DefaultUseCase<T>  extends  UseCase<T>{




    protected DefaultUseCase( ) {
        super(  Schedulers.newThread(), AndroidSchedulers.mainThread());

    }



    protected abstract Observable<T> buildUseCaseObservable();

}