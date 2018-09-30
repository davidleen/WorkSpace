package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/8/15.
 */
public class SyncAppQuotationUseCase extends DefaultUseCase {
    private final String urlHead;
    private final String startDate;
    private final String endDate;
    private final QuotationRepository quotationRepository;

    public SyncAppQuotationUseCase(String urlHead, String startDate, String endDate, QuotationRepository quotationRepository) {
        super();

        this.urlHead = urlHead;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.syncAppQuotation(urlHead,startDate,endDate);
    }
}
