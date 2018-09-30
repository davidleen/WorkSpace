package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 *
 * Created by david on 2015/10/7.
 */
public class GetStockOutListUseCase extends UseCase {


    private StockRepository stockRepository;

    private String key;
    private final long salesId;
    private int pageIndex;
    private int pageSize;

    public GetStockOutListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String key,long salesId, int pageIndex, int pageSize, StockRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.key = key;
        this.salesId = salesId;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;

        this.stockRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getStockOutList(key, salesId,pageIndex, pageSize);
    }
}
