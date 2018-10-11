package com.giants3.hd.domain.interractor;

import com.giants3.hd.noEntity.RemoteData;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2018/10/5.
 */
public class RunnableWorkReportUseCase extends DefaultUseCase {
    private Runnable runnable;

    public RunnableWorkReportUseCase(Runnable runnable) {
        super();
        this.runnable = runnable;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {


                try {
                    runnable.run();
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }


            }
        });
    }
}
