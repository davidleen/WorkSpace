package com.giants3.android.reader.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.giants3.ClassHelper;
import com.giants3.android.reader.vm.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public  abstract class BaseViewModelActivity<T extends ViewBinding,VM extends BaseViewModel> extends BaseActivity<T> {

    private VM viewModel;

    private ViewModelProvider viewModelProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()));
        viewModel = createViewModel();

        viewModel.getWaitMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {


//                if (value)
//                    getViewWaiting().showWait();
//                else {
//                    getViewWaiting().hideWait();
//                }


            }
        });
        viewModel.handleIntent(getIntent());
    }

    protected VM createViewModel() {

        Class<VM> viewModelClass = getViewModelClass();
        if (viewModelClass == null) {
            viewModelClass = ClassHelper.findParameterizedClass(getClass(), BaseViewModelActivity.class, BaseViewModel.class);
        }
        VM vm = viewModelProvider.get(viewModelClass);


        return vm;
    }



   public ViewModelProvider getViewModelProvider()
   {
       return viewModelProvider;
   }





    protected   Class<VM> getViewModelClass()
    {
        return  null;
    }


    protected final   VM getViewModel()
    {
        return viewModel;
    }
}
