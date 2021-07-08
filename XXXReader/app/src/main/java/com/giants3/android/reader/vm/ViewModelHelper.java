package com.giants3.android.reader.vm;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.giants3.android.reader.activity.BaseViewModelActivity;

public class ViewModelHelper {



    public static <T extends ViewModel> T createViewModel(Activity activity,Class<T> modelClass) {

        if(activity instanceof BaseViewModelActivity)
        return ((BaseViewModelActivity) activity).getViewModelProvider().get(modelClass);

        return null;


    }
}
