package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/8/31.
 */
public class InitGjhDataUseCase extends DefaultUseCase {
    private QuotationRepository quotationRepository;

    public InitGjhDataUseCase(QuotationRepository quotationRepository) {
        super();
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.initGjhData();
    }
}
