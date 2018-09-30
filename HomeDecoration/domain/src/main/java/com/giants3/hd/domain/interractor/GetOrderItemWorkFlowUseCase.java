package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/3/6.
 */
public class GetOrderItemWorkFlowUseCase extends UseCase {
    private final long orderItemId;
    private final OrderRepository orderRepository;

    public GetOrderItemWorkFlowUseCase(Scheduler scheduler, Scheduler immediate, long orderItemId, OrderRepository orderRepository) {
        super(scheduler,immediate);
        this.orderItemId = orderItemId;
        this.orderRepository = orderRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.getOrderItemWorkFlow(orderItemId);
    }
}
