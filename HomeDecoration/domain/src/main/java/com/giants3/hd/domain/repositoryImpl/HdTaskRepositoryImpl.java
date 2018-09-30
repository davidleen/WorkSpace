package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.HdTask;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import javafx.geometry.VPos;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/12/12.
 */
public class HdTaskRepositoryImpl extends BaseRepositoryImpl implements HdTaskRepository {

    @Inject
    ApiManager apiManager;
    @Override
    public Observable<List<HdTask>> getTaskList() {


        return Observable.create(new Observable.OnSubscribe<List<HdTask>>() {
            @Override
            public void call(Subscriber<? super List<HdTask>> subscriber) {




                try {
                    RemoteData<HdTask> remoteData= apiManager.loadTaskList();
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });






    }

    @Override
    public Observable<List<HdTask>> addTask(final HdTask task) {

        return Observable.create(new Observable.OnSubscribe<List<HdTask>>() {
            @Override
            public void call(Subscriber<? super List<HdTask>> subscriber) {




                try {
                    RemoteData<HdTask> remoteData= apiManager.addHdTask(task);
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<List<HdTask>> delete(final long taskId) {
        return Observable.create(new Observable.OnSubscribe<List<HdTask>>() {
            @Override
            public void call(Subscriber<? super List<HdTask>> subscriber) {




                try {
                    RemoteData<HdTask> remoteData= apiManager.deleteHdTask(taskId);
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }


    @Override
    public Observable<RemoteData<HdTask>> updateTaskState(final long taskId, final int taskState) {
        return crateObservable(new ApiCaller<HdTask>() {
            @Override
            public RemoteData<HdTask> call() throws HdException {
                RemoteData<HdTask>  result= apiManager.updateTaskState(taskId,taskState);
                return result;
            }
        });


    }@Override
    public Observable<RemoteData<Void>> executeHdTask(  final int taskType) {
        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
                RemoteData<Void>  result= apiManager.executeHdTask(taskType);
                return result;
            }
        });


    }
}
