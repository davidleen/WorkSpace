package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 *
 * Created by david on 2015/10/7.
 */
public class StockInAndSubmitListUseCase extends UseCase {


    private final String startDate;
    private final String endDate;
    private StockRepository stockRepository;

    private String key;



    public StockInAndSubmitListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String key,  String startDate,String endDate, StockRepository stockRepository) {
        super(threadExecutor, postExecutionThread);
        this.key = key;
        this.startDate = startDate;
        this.endDate = endDate;


        this.stockRepository = stockRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getStockInAndSubmitList(key, startDate ,endDate);
    }
}
