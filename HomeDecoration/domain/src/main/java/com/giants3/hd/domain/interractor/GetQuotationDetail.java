package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by david on 2015/9/14.
 */
public class GetQuotationDetail extends UseCase {


    long quotationId;
    QuotationRepository repository;

    public GetQuotationDetail(Scheduler threadExecutor, Scheduler postExecutionThread, long quotationId, QuotationRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.quotationId = quotationId;
        this.repository = repository;

    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.detail(quotationId);
    }
}
