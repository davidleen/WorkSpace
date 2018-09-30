package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.XiankangRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by david on 2015/10/7.
 */
public class GetOrderListUseCase extends UseCase {



    private OrderRepository orderRepository;

    private String key;
    private int pageIndex;
    private final long salesId;
    private int pageSize;
    public GetOrderListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,String key,long salesId,int pageIndex,int pageSize, OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.key = key;
        this.pageIndex = pageIndex;
        this.salesId = salesId;
        this.pageSize = pageSize;

        this.orderRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.getOrderList(key,salesId,pageIndex,pageSize);
    }
}
