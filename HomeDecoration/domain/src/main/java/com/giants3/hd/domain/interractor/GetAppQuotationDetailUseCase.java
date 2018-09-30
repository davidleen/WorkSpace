package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/4/6.
 */
public class GetAppQuotationDetailUseCase extends DefaultUseCase {
    private final long quotationId;
    private final String qNumber;
    private final QuotationRepository quotationRepository;

    public GetAppQuotationDetailUseCase(long quotationId, String qNumber, QuotationRepository quotationRepository) {
        super();
        this.quotationId = quotationId;
        this.qNumber = qNumber;
        this.quotationRepository = quotationRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.getAppQuotationDetail(quotationId,qNumber);
    }
}
