package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *   获取订单权限用例
 *
 * Created by david on 2015/10/7.
 */
public class GetOrderAuthListUseCase extends UseCase {


    private AuthRepository authRepository;



    public GetOrderAuthListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, AuthRepository authRepository) {
        super(threadExecutor, postExecutionThread);

        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return authRepository.getOrderAuthList();
    }
}
