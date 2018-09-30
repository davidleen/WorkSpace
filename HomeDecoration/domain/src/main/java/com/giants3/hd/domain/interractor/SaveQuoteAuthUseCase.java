package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.entity.QuoteAuth;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**
 * 保存报价权限用例
 * Created by david on 2015/10/7.
 */
public class SaveQuoteAuthUseCase extends UseCase {


    private AuthRepository authRepository;

    private List<QuoteAuth> quoteAuths;

    public SaveQuoteAuthUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, List<QuoteAuth> quoteAuths, AuthRepository authRepository) {
        super(threadExecutor, postExecutionThread);
        this.quoteAuths = quoteAuths;


        this.authRepository = authRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return authRepository.saveQuoteAuthList(quoteAuths);
    }
}
