package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/7/21.
 */
public class AddProductToAppQuotationUseCase extends DefaultUseCase {


    private final long quotationId;
    private final long productId;
    private final QuotationRepository quotationRepository;

    public AddProductToAppQuotationUseCase(long quotationId, long productId, QuotationRepository quotationRepository)
    {
        this.quotationId = quotationId;
        this.productId = productId;
        this.quotationRepository = quotationRepository;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.addProductToAppQuotation(quotationId,productId);
    }
}
