package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 *
 * Created by david on 2015/10/7.
 */
public class StockXiaokuItemSearchUseCase extends UseCase {



    private StockRepository stockRepository;

    private String key;
    private String dateStart;
    private String dateEnd;


    public StockXiaokuItemSearchUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String key,String dateStart,String dateEnd, StockRepository stockRepository) {
        super(threadExecutor, postExecutionThread);
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.key = key;


        this.stockRepository = stockRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getStockXiaokuItemList(key,dateStart,dateEnd);
    }
}
