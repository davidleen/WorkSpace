package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

class FindWorkFlowItemsUseCase extends DefaultUseCase {
    private final String osNo;
    private final int itm;
    private final String code;
    private final RestApi restApi;

    public FindWorkFlowItemsUseCase(String osNo, int itm, String code, RestApi restApi) {
        super();
        this.osNo = osNo;
        this.itm = itm;
        this.code = code;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.findWorkFlowItemsUseCase(osNo,itm,code);
    }
}
