package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FactoryRepository;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/1/20.
 */
public class GetOrderItemWorkFlowState extends UseCase {
    private final long orderItemId;
    private final WorkFlowRepository workFlowRepository;

    public GetOrderItemWorkFlowState(Scheduler scheduler, Scheduler immediate, long orderItemId, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.orderItemId = orderItemId;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getOrderItemWorkFlowState(orderItemId);
    }
}
