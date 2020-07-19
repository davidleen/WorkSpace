package com.giants3.android.reader.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.giants3.android.reader.entity.User;

public class UserModel extends BaseViewModel {
     private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

     public LiveData<User> getUser() {
         return userLiveData;
     }

     public UserModel() {
         // trigger user load.
     }

     void doAction() {

         // depending on the action, do necessary business logic calls and update the
         // userLiveData.
         waitMessageLiveData.postValue(true);
     }
 }