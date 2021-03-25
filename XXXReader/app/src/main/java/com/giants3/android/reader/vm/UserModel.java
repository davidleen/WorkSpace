package com.giants3.android.reader.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.giants3.android.reader.entity.User;

public class UserModel extends BaseViewModel {
     private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public UserModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<User> getUser() {
         return userLiveData;
     }



     void doAction() {

         // depending on the action, do necessary business logic calls and update the
         // userLiveData.
         waitMessageLiveData.postValue(true);
     }
 }