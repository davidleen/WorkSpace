package com.giants3.hd.data.interractor;

import com.giants3.hd.data.net.RestApi;

import java.io.File;

import rx.Observable;

/**
 * Created by davidleen29 on 2018/9/22.
 */

class ScanNameCardUseCase extends DefaultUseCase {
    private final File newPath;
    private final RestApi restApi;

    public ScanNameCardUseCase(File newPath, RestApi restApi) {
        super();
        this.newPath = newPath;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApi.scanNameCard(newPath);
    }
}
