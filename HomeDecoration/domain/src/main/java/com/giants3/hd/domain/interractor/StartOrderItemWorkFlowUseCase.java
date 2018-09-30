package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FactoryRepository;
import com.giants3.hd.entity.OrderItemWorkFlow;
import rx.Observable;
import rx.Scheduler;

/**
 * 启动订单的生产流程
 * Created by davidleen29 on 2017/1/15.
 */
public class StartOrderItemWorkFlowUseCase extends UseCase {
    private final OrderItemWorkFlow orderItemWorkFlow;
    private final FactoryRepository factoryRepository;

    public StartOrderItemWorkFlowUseCase(Scheduler scheduler, Scheduler immediate, OrderItemWorkFlow orderItemWorkFlow, FactoryRepository factoryRepository) {
        super(scheduler,immediate);
        this.orderItemWorkFlow = orderItemWorkFlow;
        this.factoryRepository = factoryRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return factoryRepository.startOrderItemWorkFlow(orderItemWorkFlow);
    }
}
