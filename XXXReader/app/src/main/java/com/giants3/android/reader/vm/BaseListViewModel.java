package com.giants3.android.reader.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.giants3.reader.noEntity.RemoteData;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseListViewModel<D> extends BaseViewModel {

    private final MutableLiveData<RemoteData<D>> listResult = new MutableLiveData<>();

    public BaseListViewModel(@NonNull Application application) {
        super(application);
    }


    public void loadData() {


        getWaitMessage().setValue(true);
        UseCaseFactory.getInstance().createUseCase(getListUrl(), getArgClass()).execute(new UseCaseHandler<RemoteData<D>>() {
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

    protected Class<D> getArgClass() {


        Type genericSuperclass = getClass().getGenericSuperclass();
        try {
            if (genericSuperclass instanceof ParameterizedType) {
                Type actualTypeArgument = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];

                if (actualTypeArgument instanceof Class) {
                    Class<D> clas = (Class<D>) actualTypeArgument;
                    return clas;

                }


            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public LiveData<RemoteData<D>> getListResult() {
        return listResult;
    }

    protected abstract String getListUrl();
}
