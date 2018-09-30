package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.HdTaskLogRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/12/12.
 */
public class HdTaskLogRepositoryImpl extends BaseRepositoryImpl implements HdTaskLogRepository {

    @Inject
    ApiManager apiManager;
    @Override
    public Observable<List<HdTaskLog>> getTaskLogList(final long taskId) {


        return Observable.create(new Observable.OnSubscribe<List<HdTaskLog>>() {
            @Override
            public void call(Subscriber<? super List<HdTaskLog>> subscriber) {




                try {
                    RemoteData<HdTaskLog> remoteData= apiManager.loadTaskLogList(taskId);
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });






    }

}
