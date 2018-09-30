package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.StockRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

/**
 * 库存处理
 * Created by david on 2015/10/13.
 */
public class StockRepositoryImpl extends BaseRepositoryImpl implements StockRepository {
    @Inject
    ApiManager apiManager;

    @Override
    public Observable<RemoteData<ErpStockOut>> getStockOutList(final String key, final long salesId, final int pageIndex, final int pageSize) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpStockOut>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpStockOut>> subscriber) {


                try {
                    RemoteData<ErpStockOut> remoteData = apiManager.getStockOutList(key, salesId, pageIndex, pageSize);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<ErpStockOutDetail>> getStockOutDetail(final String ck_no) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpStockOutDetail>> subscriber) {
                try {
                    RemoteData<ErpStockOutDetail> remoteData = apiManager.getStockOutDetail(ck_no);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<ErpStockOutDetail>> saveStockOutDetail(final ErpStockOutDetail stockOutDetail) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpStockOutDetail>> subscriber) {
                try {
                    RemoteData<ErpStockOutDetail> remoteData = apiManager.saveStockOutDetail(stockOutDetail);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<StockSubmit>> getStockInAndSubmitList(final String key, final String startDate, final String endDate) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<StockSubmit>>() {
            @Override
            public void call(Subscriber<? super RemoteData<StockSubmit>> subscriber) {
                try {
                    RemoteData<StockSubmit> remoteData = apiManager.getStockInAndSubmitList(key, startDate, endDate);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<StockSubmit>> getStockXiaokuItemList(final String ps_no) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<StockSubmit>>() {
            @Override
            public void call(Subscriber<? super RemoteData<StockSubmit>> subscriber) {
                try {
                    RemoteData<StockSubmit> remoteData = apiManager.getStockXiaokuItemList(ps_no);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }


    @Override
    public Observable<RemoteData<StockSubmit>> getStockXiaokuItemList(final String key, final String dateStart, final String dateEnd) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<StockSubmit>>() {
            @Override
            public void call(Subscriber<? super RemoteData<StockSubmit>> subscriber) {
                try {
                    RemoteData<StockSubmit> remoteData = apiManager.getStockXiaokuItemList(key,dateStart,dateEnd);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<StockXiaoku>> getStockXiaokuList(final String  key, final int pageIndex, final int pageSize) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<StockXiaoku>>() {
            @Override
            public void call(Subscriber<? super RemoteData<StockXiaoku>> subscriber) {
                try {
                    RemoteData<StockXiaoku> remoteData = apiManager.getStockXiaokuList(key,pageIndex, pageSize);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }
}
