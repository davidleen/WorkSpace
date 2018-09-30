package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.entity.app.AppQuoteAuth;
import rx.Observable;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by davidleen29 on 2018/7/16.
 */
public class SaveAppQuoteAuthUseCase extends DefaultUseCase {
    private final List<AppQuoteAuth> appQuoteAuths;
    private final AuthRepository authRepository;

    public SaveAppQuoteAuthUseCase(List<AppQuoteAuth> appQuoteAuths, AuthRepository authRepository) {
        this.appQuoteAuths = appQuoteAuths;
        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return authRepository.saveAppQuoteAuthList(appQuoteAuths);
    }
}
