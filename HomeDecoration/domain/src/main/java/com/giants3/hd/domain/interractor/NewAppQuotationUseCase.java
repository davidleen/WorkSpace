package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/7/19.
 */
public class NewAppQuotationUseCase extends DefaultUseCase {
    private QuotationRepository quotationRepository;

    public NewAppQuotationUseCase(QuotationRepository quotationRepository) {
        super();
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.newQuotation();
    }
}
