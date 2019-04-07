package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

class UnVerifyQuotationUseCase extends DefaultUseCase {
    private long quotationId;
    private RestApi restApi;

    public UnVerifyQuotationUseCase(long quotationId, RestApi restApi) {
        super();
        this.quotationId = quotationId;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.unVerifyQuotation(quotationId);
    }
}
