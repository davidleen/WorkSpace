package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by david on 2015/10/7.
 */
public class GetOrderItemListUseCase extends UseCase {



    private OrderRepository orderRepository;

    private String or_no;

    public GetOrderItemListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String or_no , OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.or_no = or_no;


        this.orderRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.getOrderItemList(or_no);
    }
}
