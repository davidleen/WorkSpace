package com.giants3.android.reader.vm;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.giants3.android.reader.entity.User;
import com.giants3.android.reader.entity.WaitMessage;

public class BaseViewModel extends AndroidViewModel {
    protected final MutableLiveData<Boolean> waitMessageLiveData = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getWaitMessage() {
        return waitMessageLiveData;
    }

    public   void handleIntent(Intent intent)
    {}
}
