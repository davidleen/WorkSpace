package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * 撤销订单排厂
 * Created by davidleen29 on 2017/4/30.
 */
public class CancelOrderWorkFlowUseCase extends DefaultUseCase {
    private final long orderItemWorkFlowId;
    private final OrderRepository orderRepository;

    public CancelOrderWorkFlowUseCase(long orderItemWorkFlowId, OrderRepository orderRepository) {

        this.orderItemWorkFlowId = orderItemWorkFlowId;
        this.orderRepository = orderRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {

        return orderRepository.cancelOrderWorkFlow(orderItemWorkFlowId);
    }
}
