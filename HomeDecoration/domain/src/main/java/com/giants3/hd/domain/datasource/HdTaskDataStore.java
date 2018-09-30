package com.giants3.hd.domain.datasource;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.noEntity.QuotationDetail;
import rx.Observable;

import java.util.List;

/**
 *
 */
public interface HdTaskDataStore {

    /**
     * Get an {@link Observable} which will emit a List of {@link Quotation}.
     */
    Observable<List<HdTask>> taskList();

    /**
     * Get an {@link Observable} which will emit a {@link QuotationDetail} by its id.
     *
     * @param quotationId The id to retrieve user data.
     */
    Observable<QuotationDetail> quotationDetail(final long quotationId);
}
