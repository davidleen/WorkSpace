package com.giants3.hd.domain.repository;

import com.giants3.hd.entity.Quotation;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;
import rx.Observable;

import java.util.List;

/**
 * Created by david on 2015/9/14.
 */
public interface QuotationRepository {



    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Quotation}.
     */
    Observable<List<Quotation>> quotations();

    /**
     * Get an {@link rx.Observable} which will emit a {@link QuotationDetail}.
     *
     * @param quotationId The user id used to retrieve user data.
     */
     Observable<QuotationDetail> detail(final long quotationId);

    Observable<RemoteData<com.giants3.hd.entity.app.Quotation>> getAppQuotationList(String key,String dateStart, String dateEnd, long userId, int pageIndex, int pageSize);

    Observable<RemoteData<com.giants3.hd.noEntity.app.QuotationDetail>> getAppQuotationDetail(long quotationId, String qNumber);

    Observable<RemoteData<com.giants3.hd.noEntity.app.QuotationDetail>> newQuotation();
    Observable<RemoteData<com.giants3.hd.noEntity.app.QuotationDetail>> addProductToAppQuotation(long quotationId,long productId);

    Observable saveAppQuotation(com.giants3.hd.noEntity.app.QuotationDetail quotationDetail);

    Observable deleteAppQuotation(long quotationId);

    Observable printQuotationToFile(long quotationId, String filePath);

    Observable syncAppQuotation(String urlHead, String startDate, String endDate);

    Observable initGjhData();

    Observable getAppQuoteCountReport(String startDate, String endDate);
}
