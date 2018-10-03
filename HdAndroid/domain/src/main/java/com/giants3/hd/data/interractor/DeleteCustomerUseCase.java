package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2018/10/2.
 */

class DeleteCustomerUseCase extends DefaultUseCase {
    private final long customerId;
    private final RestApi restApi;

    public DeleteCustomerUseCase(long customerId, RestApi restApi) {
        super();
        this.customerId = customerId;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.deleteCutomer(customerId);
    }
}
