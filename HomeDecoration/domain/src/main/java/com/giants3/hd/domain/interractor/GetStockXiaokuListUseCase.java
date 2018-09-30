package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 * 销库单列表
 *
 * Created by david on 2015/10/7.
 */
public class GetStockXiaokuListUseCase extends UseCase {


    private StockRepository stockRepository;


    private final String key;
    private int pageIndex;
    private int pageSize;

    public GetStockXiaokuListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,String key, int pageIndex, int pageSize, StockRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.key = key;

        this.pageIndex = pageIndex;
        this.pageSize = pageSize;

        this.stockRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getStockXiaokuList(  key, pageIndex, pageSize);
    }
}
