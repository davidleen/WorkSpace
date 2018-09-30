package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2017/6/25.
 */

class GetMyWorkFlowMessageCase extends DefaultUseCase {
    private String key;
    private RestApi restApi;
    private final int pageIndex;
    private final int pageSize;

    public GetMyWorkFlowMessageCase(String key,RestApi restApi,int pageIndex,int pageSize) {
        super();
        this.key = key;
        this.restApi = restApi;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.getMyWorkFlowMessage(key ,  pageIndex, pageSize);
    }
}
