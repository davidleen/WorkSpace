package com.giants3.hd.domain;

import rx.Subscriber;

/**
 * Created by davidleen29 on 2017/1/8.
 */
public abstract class BaseSubscriber<T>  extends Subscriber<T> {

    /**
     * Notifies the Observer that the {@link Observable} has experienced an error condition.
     * <p/>
     * If the {@link Observable} calls this method, it will not thereafter call {@link #onNext} or
     * {@link #onCompleted}.
     *
     * @param e the exception encountered by the Observable
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

    }
}
