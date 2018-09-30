package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.datasource.QuotationDataSourceImpl;
import com.giants3.hd.domain.datasource.QuotationDataStore;
import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.google.inject.Inject;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * Created by david on 2015/9/14.
 */
public class QuotationRepositoryImpl  extends  BaseRepositoryImpl implements QuotationRepository {
    @Inject
    ApiManager apiManager;

    public Observable<List<Quotation>> quotations() {



        return null;
    }

    public Observable<QuotationDetail> detail(long quotationId) {

        QuotationDataStore quotationDataStore=new QuotationDataSourceImpl();

        return quotationDataStore.quotationDetail(quotationId);
    }


    @Override
    public Observable<RemoteData<com.giants3.hd.entity.app.Quotation>> getAppQuotationList(final String key,final String dateStart,final String dateEnd, final long userId,final int pageIndex, final int pageSize) {

        return crateObservable(new ApiCaller<com.giants3.hd.entity.app.Quotation>() {
            @Override
            public RemoteData<com.giants3.hd.entity.app.Quotation> call() throws HdException {
                return apiManager.getAppQuotationList(key,  dateStart,    dateEnd,     userId,pageIndex,pageSize);
            }
        });

    }

    @Override
    public Observable<RemoteData<com.giants3.hd.noEntity.app.QuotationDetail>> getAppQuotationDetail(final long quotationId, final String qNumber) {
        return crateObservable(new ApiCaller<com.giants3.hd.noEntity.app.QuotationDetail>() {
            @Override
            public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> call() throws HdException {
                return apiManager.getAppQuotationDetail(quotationId,qNumber);
            }
        });
    }


    @Override
    public Observable<RemoteData<com.giants3.hd.noEntity.app.QuotationDetail>> newQuotation() {
        return crateObservable(new ApiCaller<com.giants3.hd.noEntity.app.QuotationDetail>() {
            @Override
            public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> call() throws HdException {
                return apiManager.newAppQuotation();
            }
        });
    }


    @Override
    public Observable<RemoteData<com.giants3.hd.noEntity.app.QuotationDetail>> addProductToAppQuotation(final long quotationId, final long productId) {
        return crateObservable(new ApiCaller<com.giants3.hd.noEntity.app.QuotationDetail>() {
            @Override
            public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> call() throws HdException {
                return apiManager.addProductToAppQuotation(quotationId,productId);
            }
        });
    }

    @Override
    public Observable saveAppQuotation(final com.giants3.hd.noEntity.app.QuotationDetail quotationId) {
        return crateObservable(new ApiCaller<com.giants3.hd.noEntity.app.QuotationDetail>() {
            @Override
            public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> call() throws HdException {
                return apiManager.saveAppQuotation(quotationId);
            }
        });
    }

    @Override
    public Observable deleteAppQuotation(final long quotationId) {

        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
                return apiManager.deleteAppQuotation(quotationId);
            }
        });
    }


    @Override
    public Observable printQuotationToFile(final long quotationId, final String filePath) {
        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
                return apiManager.printQuotationToFile(quotationId,filePath);
            }
        });
    }

    @Override
    public Observable syncAppQuotation(final String urlHead, final String startDate, final String endDate) {
        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
                return apiManager.syncAppQuotation(urlHead,startDate,endDate);
            }
        });
    }

    @Override
    public Observable initGjhData() {
        return crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
                return apiManager.initGjhData();
            }
        });
    }

    @Override
    public Observable getAppQuoteCountReport(final String startDate, final String endDate) {
        return crateObservable(new ApiCaller<Map>() {
            @Override
            public RemoteData<Map> call() throws HdException {
                return apiManager.getAppQuoteCountReport(startDate,endDate);
            }
        });
    }
}
