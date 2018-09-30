package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import rx.Observable;
import rx.Scheduler;

/** 生产流程查询用例
 * Created by david on 2015/10/7.
 */
public class OrderWorkFlowReportUseCase extends UseCase {


    private final String key;
    private final int pageIndex;
    private final int pageSize;
    private OrderRepository orderRepository;


    public OrderWorkFlowReportUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,String key,int pageIndex,int pageSize, OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.key = key;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;


        this.orderRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.getOrderWorkFlowReport( key,pageIndex,pageSize );
    }
}
