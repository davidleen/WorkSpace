package com.giants3.android.reader.vm;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.giants3.android.reader.entity.User;
import com.giants3.android.reader.entity.WaitMessage;

public class BaseViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> waitMessageLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> getWaitMessage() {
        return waitMessageLiveData;
    }

    public   void handleIntent(Intent intent)
    {}
}
