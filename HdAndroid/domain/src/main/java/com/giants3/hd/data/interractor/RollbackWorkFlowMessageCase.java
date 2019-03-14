package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import java.io.File;

import rx.Observable;


/**
 * 审核流程传递
 *
 *
 */
public class RollbackWorkFlowMessageCase extends DefaultUseCase {


    private final long workFlowMessageId;
    private final String memo;

    RestApi restApi;
    public RollbackWorkFlowMessageCase(long messageId,   String memo, RestApi restApi) {

        this.workFlowMessageId = messageId;
        this.memo = memo;

        this.restApi=restApi;


    }

    @Override
    protected Observable buildUseCaseObservable() {



       return restApi.rollbackWorkFlowMessage ( workFlowMessageId,     memo);


    }
}
