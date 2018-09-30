package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.entity.StockOutAuth;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**
 * 保存出库权限用例
 * Created by david on 2015/10/7.
 */
public class SaveStockOutAuthUseCase extends UseCase {


    private AuthRepository authRepository;

    private List<StockOutAuth> stockOutAuths;

    public SaveStockOutAuthUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,  List<StockOutAuth> stockOutAuths, AuthRepository authRepository) {
        super(threadExecutor, postExecutionThread);
        this.stockOutAuths = stockOutAuths;


        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return authRepository.saveStockOutAuthList(stockOutAuths);
    }
}
