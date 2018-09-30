package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 *
 * Created by david on 2015/10/7.
 */
public class StockXiaokuItemUseCase extends UseCase {



    private StockRepository stockRepository;

    private String ps_no;



    public StockXiaokuItemUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String ps_no,  StockRepository stockRepository) {
        super(threadExecutor, postExecutionThread);
        this.ps_no = ps_no;


        this.stockRepository = stockRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getStockXiaokuItemList(ps_no);
    }
}
