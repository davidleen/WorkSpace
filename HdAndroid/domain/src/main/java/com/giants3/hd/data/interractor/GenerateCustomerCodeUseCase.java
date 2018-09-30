package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2018/9/1.
 */

class GenerateCustomerCodeUseCase extends DefaultUseCase {
    private RestApi restApi;

    public GenerateCustomerCodeUseCase(RestApi restApi) {
        super();
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.GenerateCustomerCodeUseCase();
    }
}
