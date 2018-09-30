package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by david on 2015/9/14.
 */
public class GetQuotationList extends UseCase {



    QuotationRepository repository;

    public GetQuotationList(Scheduler threadExecutor, Scheduler postExecutionThread,
                            QuotationRepository repository) {
        super(threadExecutor, postExecutionThread);

        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.quotations( );
    }
}
