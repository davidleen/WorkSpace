package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.FileRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.io.File;
import java.util.List;

/**
 * 文件功能
 * Created by david on 2015/10/13.
 */
public class FileRepositoryImpl extends BaseRepositoryImpl implements FileRepository {

    @Inject
    ApiManager apiManager;

    @Override
    public Observable<List<String>> uploadTempFile(final File[] files) {

        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {


                RemoteData<String> result = new RemoteData<String>();

                for (File temp : files) {

                    try {
                        RemoteData<String> remoteData = apiManager.uploadTempPicture(temp);
                        if (remoteData.isSuccess()) {
                            result.datas.addAll(remoteData.datas);
                        } else {

                            subscriber.onError(HdException.create(remoteData.message));
                            return;
                        }


                    } catch (HdException e) {
                        subscriber.onError(e);
                        return;
                    }
                }


                handleResult(subscriber, result);


            }
        });
    }

    @Override
    public Observable<RemoteData<Void>> syncProductPicture(final String remoteResource, final String filterKey) {
        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {


                return apiManager.syncProductPicture(remoteResource,  filterKey);


            }
        });
    }
}
