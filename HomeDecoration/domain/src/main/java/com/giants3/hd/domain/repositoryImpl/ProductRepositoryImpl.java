package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Product;
import com.giants3.hd.exception.HdException;

import com.giants3.hd.noEntity.ProductDetail;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public class ProductRepositoryImpl extends BaseRepositoryImpl implements ProductRepository {



    @Inject
    ApiManager apiManager;
    @Override
    public Observable<List<Product>> loadByProductNameBetween(final String startName, final String endName, final boolean withCopy) {



        return Observable.create(new Observable.OnSubscribe<List<Product>>() {
            @Override
            public void call(Subscriber<? super List<Product>> subscriber) {




                try {
                    RemoteData<Product> remoteData= apiManager.loadProductListByNameBetween(startName, endName,withCopy);

                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData.datas );
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
    public Observable<RemoteData<Product>> loadByProductNameRandom(final String productNames, final boolean withCopy) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Product>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Product>> subscriber) {




                try {
                    RemoteData<Product> remoteData= apiManager.loadProductListByNameRandom(productNames,withCopy);

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
    public Observable<ProductDetail> loadByProductNo(final String prdNo) {
        return Observable.create(new Observable.OnSubscribe<ProductDetail>() {
            @Override
            public void call(Subscriber<? super ProductDetail> subscriber) {




                try {
                    RemoteData<ProductDetail> remoteData= apiManager.loadProductDetailByPrdNo(prdNo);

                    if(remoteData.isSuccess()&&remoteData.datas.size()>0)
                    {
                        subscriber.onNext(remoteData.datas.get(0) );
                        subscriber.onCompleted();

                    }else
                    {
                        if(remoteData.isSuccess())
                        {
                            subscriber.onError(   HdException.create("未找到"+prdNo+" 的产品详细数据"));

                        }else
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    /**
     * 公式改变时候， 进行产品表的数据同步。
     *
     * @return
     */
    @Override
    public Observable<RemoteData<Void>> synchronizeProductOnEquationUpdate() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {




                try {
                    RemoteData<Void> remoteData= apiManager.synchronizeProductOnEquationUpdate( );


                    subscriber.onNext(remoteData  );
                    subscriber.onCompleted();


                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<RemoteData<Void>> syncProductInfo(final String remoteResource, final String filterKey, final boolean shouldOverride) {
        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {


                return apiManager.syncProductInfo(remoteResource,  filterKey,  shouldOverride);


            }
        });
    }
    @Override
    public Observable<RemoteData<Void>> correctThumbnail(final long productId) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {




                try {
                    RemoteData<Void> remoteData= apiManager.correctProductThumbnail(productId);


                        subscriber.onNext(remoteData  );
                        subscriber.onCompleted();


                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });



    }


    @Override
    public Observable<RemoteData<Void>> syncRelateProductPicture() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {




                try {
                    RemoteData<Void> remoteData= apiManager.syncRelateProductPicture( );


                    subscriber.onNext(remoteData  );
                    subscriber.onCompleted();


                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<RemoteData<Product>> loadById(final long[] productIds) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Product>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Product>> subscriber) {




                try {
                    RemoteData<Product> remoteData= apiManager.loadProductById( productIds);
                    subscriber.onNext(remoteData  );
                    subscriber.onCompleted();


                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<RemoteData<ProductDetail>> loadDetailById(final long productId) {



        return crateObservable(new ApiCaller<ProductDetail>() {
            @Override
            public RemoteData<ProductDetail> call() throws HdException {

             return    apiManager.loadProductDetail(productId);


            }
        });
    }
}
