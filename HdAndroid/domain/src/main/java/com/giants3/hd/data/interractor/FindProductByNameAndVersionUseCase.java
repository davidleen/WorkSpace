package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**
 * Created by davidleen29 on 2018/10/30.
 */

public class FindProductByNameAndVersionUseCase extends DefaultUseCase {
    private final String pName;
    private final String pVersion;
    private final RestApi restApi;

    public FindProductByNameAndVersionUseCase(String pName, String pVersion, RestApi restApi) {
        super();
        this.pName = pName;
        this.pVersion = pVersion;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.findProductByNameAndVersion(pName,pVersion);
    }
}
