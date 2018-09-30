package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 *  获取订单详情
 *
 * Created by david on 2015/10/7.
 */
public class GetOrderDetailUseCase extends UseCase {


    private OrderRepository stockRepository;

    private String os_no;


    public GetOrderDetailUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String os_no, OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.os_no = os_no;


        this.stockRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getOrderOutDetail(os_no);
    }
}
