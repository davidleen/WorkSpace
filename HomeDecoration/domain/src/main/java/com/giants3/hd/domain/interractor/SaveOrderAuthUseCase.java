package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.entity.OrderAuth;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**
 * 保存订单权限用例
 * Created by david on 2015/10/7.
 */
public class SaveOrderAuthUseCase extends UseCase {


    private AuthRepository authRepository;

    private List<OrderAuth> orderAuthes;

    public SaveOrderAuthUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, List<OrderAuth> orderAuths, AuthRepository authRepository) {
        super(threadExecutor, postExecutionThread);
        this.orderAuthes = orderAuths;


        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return authRepository.saveOrderAuthList(orderAuthes);
    }
}
