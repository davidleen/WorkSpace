package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2017/11/22.
 */

class AdjustWorkFlowItemUseCase extends DefaultUseCase {
    private final String os_no;
    private final String prd_no;
    private final int  itm;
    private final RestApi restApi;

    public AdjustWorkFlowItemUseCase(String os_no, String prd_no, int itm,RestApi restApi) {
        super();
        this.os_no = os_no;
        this.prd_no = prd_no;
        this.itm = itm;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return restApi.adjustWorkFlowItem(os_no,prd_no,itm);
    }
}
