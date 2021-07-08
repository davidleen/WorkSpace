package com.giants3.android.reader.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.giants3.reader.noEntity.RemoteData;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseListViewModel<D> extends BaseViewModel {

    protected final MutableLiveData<RemoteData<D>> listResult = new MutableLiveData<>();

    public BaseListViewModel(@NonNull Application application) {
        super(application);
    }


    public abstract void loadData()  ;

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

}
