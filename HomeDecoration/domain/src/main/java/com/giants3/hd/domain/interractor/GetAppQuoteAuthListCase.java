package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2018/7/15.
 */
public class GetAppQuoteAuthListCase extends DefaultUseCase {
    private AuthRepository authRepository;

    public GetAppQuoteAuthListCase(AuthRepository authRepository) {
        super();
        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return authRepository.getAppQuoteAuthList();
    }
}
