package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.noEntity.ErpOrderDetail;
import rx.Observable;
import rx.Scheduler;

/**
 * 保存出库详情用例
 * Created by david on 2015/10/7.
 */
public class SaveOrderDetailUseCase extends UseCase {


    private OrderRepository orderRepository;

    private ErpOrderDetail orderDetail;

    public SaveOrderDetailUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, ErpOrderDetail orderDetail, OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.orderDetail = orderDetail;


        this.orderRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.saveOrderDetail(orderDetail);
    }
}
