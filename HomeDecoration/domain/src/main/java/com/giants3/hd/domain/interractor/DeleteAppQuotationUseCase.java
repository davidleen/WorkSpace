package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.noEntity.app.QuotationDetail;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/7/22.
 */
public class DeleteAppQuotationUseCase extends DefaultUseCase {
    private final long quotationId;
    private final QuotationRepository quotationRepository;

    public DeleteAppQuotationUseCase(long quotationId, QuotationRepository quotationRepository) {
        super();
        this.quotationId = quotationId;
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.deleteAppQuotation(quotationId);
    }
}
