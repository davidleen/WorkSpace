package com.giants3.hd.domain.datasource;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/9/14.
 */
public class QuotationDataSourceImpl implements  QuotationDataStore{
    @Inject
    public Observable<List<Quotation>> userEntityList() {
        return null;
    }

    public Observable<QuotationDetail> quotationDetail(final long quotationId) {
        return Observable.create(new Observable.OnSubscribe<QuotationDetail>() {
            @Override
            public void call(Subscriber<? super QuotationDetail> subscriber) {



                  ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
                try {
                    RemoteData<QuotationDetail> remoteData= apiManager.loadQuotationDetail(quotationId);

                    if(remoteData.isSuccess())
                    {


                        subscriber.onNext(remoteData.datas.get(0));
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
    public Observable<com.giants3.hd.entity.app.Quotation> getAppQuotationList(String key, int pageIndex, int pageSize) {
        return null;
    }
}
