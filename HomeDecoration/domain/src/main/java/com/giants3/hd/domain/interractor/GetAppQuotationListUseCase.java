package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/4/5.
 */
public class GetAppQuotationListUseCase extends DefaultUseCase {
    private final String key;
    private final String dateStart;
    private final String dateEnd;
    private final long userId;
    private final int pageIndex;
    private final int pageSize;
    private final QuotationRepository quotationRepository;

    public GetAppQuotationListUseCase(String key,String dateStart, String dateEnd, long userId, int pageIndex, int pageSize, QuotationRepository quotationRepository) {
        super();
        this.key = key;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userId = userId;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.quotationRepository = quotationRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.getAppQuotationList(key,  dateStart,   dateEnd,   userId,pageIndex,pageSize);
    }
}
