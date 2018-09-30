package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.FactoryRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.OrderItemWorkFlow;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 *
 *  获取外厂数据
 * Created by davidleen29 on 2017/1/15.
 */
public class FactoryRepositoryImpl  extends  BaseRepositoryImpl implements FactoryRepository {

    @Inject
    ApiManager apiManager;
    @Override
    public Observable<RemoteData<OutFactory>> getOutFactories() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<OutFactory>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OutFactory>> subscriber) {




                try {
                    RemoteData<OutFactory> remoteData= apiManager.getOutFactories();
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
    public Observable<RemoteData<OutFactory>> saveOutFactories(final List<OutFactory> datas) {





            return Observable.create(new Observable.OnSubscribe<RemoteData<OutFactory>>() {
                @Override
                public void call(Subscriber<? super RemoteData<OutFactory>> subscriber) {




                    try {
                        RemoteData<OutFactory> remoteData= apiManager.saveOutFactories(datas);
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
    public Observable<RemoteData<OrderItemWorkFlow>> startOrderItemWorkFlow(final OrderItemWorkFlow orderItemWorkFlow) {



        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderItemWorkFlow>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderItemWorkFlow>> subscriber) {




                try {
                    RemoteData<OrderItemWorkFlow> remoteData= apiManager.startOrderItemWorkFlow(orderItemWorkFlow);
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
