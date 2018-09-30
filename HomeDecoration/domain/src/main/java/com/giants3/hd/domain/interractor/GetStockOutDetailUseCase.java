package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 *  获取出库单详情
 *
 * Created by david on 2015/10/7.
 */
public class GetStockOutDetailUseCase extends UseCase {


    private StockRepository stockRepository;

    private String ck_no;


    public GetStockOutDetailUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String ck_no,  StockRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.ck_no = ck_no;


        this.stockRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.getStockOutDetail(ck_no);
    }
}
