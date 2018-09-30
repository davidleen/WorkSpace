package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/1/5.
 */
public class GetWorkFlowOfProduct  extends UseCase {
    private final long productId;
    private final WorkFlowRepository workFlowRepository;

    public GetWorkFlowOfProduct(Scheduler scheduler, Scheduler immediate, long productId, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.productId = productId;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getWorkFlowOfProduct(productId);
    }
}
