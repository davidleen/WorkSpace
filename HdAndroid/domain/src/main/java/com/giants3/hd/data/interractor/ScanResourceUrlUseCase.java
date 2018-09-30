package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2018/9/25.
 */

class ScanResourceUrlUseCase extends DefaultUseCase {
    private final String resourceUrl;
    private final RestApi restApi;

    public ScanResourceUrlUseCase(String resourceUrl, RestApi restApi) {
        super();
        this.resourceUrl = resourceUrl;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.scanResourceUrl(resourceUrl);
    }
}
