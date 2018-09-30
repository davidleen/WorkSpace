package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/9/17.
 */
public class AppQuoteCountReportUseCase extends DefaultUseCase {
    private final String startDate;
    private final String endDate;
    private final QuotationRepository quotationRepository;

    public AppQuoteCountReportUseCase(String startDate, String endDate, QuotationRepository quotationRepository) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.getAppQuoteCountReport(startDate,endDate);
    }
}
