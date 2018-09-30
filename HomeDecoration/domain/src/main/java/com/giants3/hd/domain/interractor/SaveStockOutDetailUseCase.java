package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.StockRepository;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import rx.Observable;
import rx.Scheduler;

/**
 * 保存出库详情用例
 * Created by david on 2015/10/7.
 */
public class SaveStockOutDetailUseCase extends UseCase {


    private StockRepository stockRepository;

    private ErpStockOutDetail stockOutDetail;

    public SaveStockOutDetailUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, ErpStockOutDetail stockOutDetail, StockRepository stockRepository) {
        super(threadExecutor, postExecutionThread);
        this.stockOutDetail = stockOutDetail;


        this.stockRepository = stockRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return stockRepository.saveStockOutDetail(stockOutDetail);
    }
}
