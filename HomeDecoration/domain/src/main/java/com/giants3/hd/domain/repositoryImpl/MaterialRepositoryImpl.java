package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.MaterialRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.MaterialClass;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class MaterialRepositoryImpl implements MaterialRepository {
    @Inject
    ApiManager apiManager;
    /**
     * 更新材料分类
     *
     * @param materialClass
     * @return
     */
    @Override
    public Observable<RemoteData<MaterialClass>> updateMaterialClass(final MaterialClass materialClass) {




        return Observable.create(new Observable.OnSubscribe<RemoteData<MaterialClass>>() {
            @Override
            public void call(Subscriber<? super RemoteData<MaterialClass>> subscriber) {




                try {
                    RemoteData<MaterialClass> remoteData= apiManager.updateMaterialClass(materialClass);

                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData );
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


    @Override
    public Observable<RemoteData<Void>> deleteMaterialClass(final long materialClassId) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {




                try {
                    RemoteData<Void> remoteData= apiManager.deleteMaterialClass(materialClassId);

                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData );
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
