package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.noEntity.app.QuotationDetail;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/7/22.
 */
public class SaveAppQuotationUseCase extends DefaultUseCase {
    private final QuotationDetail quotationDetail;
    private final QuotationRepository quotationRepository;

    public SaveAppQuotationUseCase(QuotationDetail quotationDetail, QuotationRepository quotationRepository) {
        super();
        this.quotationDetail = quotationDetail;
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.saveAppQuotation(quotationDetail);
    }
}
