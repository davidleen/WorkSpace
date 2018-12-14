package com.giants3.android.reader.domain;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by david on 2015/9/14.
 */
public abstract class UseCase<T> {

    private final Scheduler threadExecutor;
    private final Scheduler postExecutionThread;

    private Subscription subscription = Subscriptions.empty();


    protected UseCase(Scheduler threadExecutor,
                      Scheduler postExecutionThread) {


        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;

    }

    public final void execute(final UseCaseHandler<T> caseHandler) {


        Subscriber useCaseSubscriber = new Subscriber<T>() {
            @Override
            public void onCompleted() {
                caseHandler.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                caseHandler.onError(e);
            }

            @Override
            public void onNext(T t) {
                caseHandler.onNext(t);

            }
        };
        this.subscription = this.buildUseCaseObservable().subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(useCaseSubscriber);

    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable<T> buildUseCaseObservable();

}