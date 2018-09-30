package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.domain.repository.StockRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *   获取报价权限详情用例
 *
 * Created by david on 2015/10/7.
 */
public class GetQuoteAuthListUseCase extends UseCase {


    private AuthRepository authRepository;



    public GetQuoteAuthListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,AuthRepository authRepository) {
        super(threadExecutor, postExecutionThread);

        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return authRepository.getQuoteAuthList();
    }
}
