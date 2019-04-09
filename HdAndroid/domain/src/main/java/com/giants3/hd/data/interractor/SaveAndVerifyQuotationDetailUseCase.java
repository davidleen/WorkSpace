package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;
import com.giants3.hd.noEntity.QuotationDetail;

import rx.Observable;

class SaveAndVerifyQuotationDetailUseCase extends DefaultUseCase {
    private final QuotationDetail quotationDetail;
    private final RestApi restApi;

    public SaveAndVerifyQuotationDetailUseCase(QuotationDetail quotationDetail, RestApi restApi) {
        super();

        this.quotationDetail = quotationDetail;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {


        return restApi.saveAndVerifyQuotationDetail(quotationDetail);
    }
}
