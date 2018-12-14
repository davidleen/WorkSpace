package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2018/2/25.
 */

class GetCustomerListUseCase extends DefaultUseCase {
    private String key;
    private final int pageIndex;
    private final int pageSize;
    private RestApi restApi;

    public GetCustomerListUseCase(String key,int pageIndex,int pageSize,RestApi restApi) {
        super();
        this.key = key;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.getCustomerList(key,  pageIndex,  pageSize);
    }
}
