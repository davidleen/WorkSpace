package com.giants3.android.reader.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.giants3.reader.noEntity.RemoteData;



public abstract class BaseUrlListViewModel<D>  extends BaseListViewModel<D> {


    public BaseUrlListViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadData() {

        String listUrl = getListUrl();
        if(StringUtil.isEmpty(listUrl)) return;

        getWaitMessage().setValue(true);

        UseCaseFactory.getInstance().createUseCase(listUrl, getArgClass()).execute(new UseCaseHandler<RemoteData<D>>() {
            @Override
            public void onCompleted() {

                getWaitMessage().setValue(false);

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                getWaitMessage().setValue(false);
            }

            @Override
            public void onNext(RemoteData<D> remoteData) {

                listResult.setValue(remoteData);

            }
        });


    }



    protected abstract String getListUrl();
}
