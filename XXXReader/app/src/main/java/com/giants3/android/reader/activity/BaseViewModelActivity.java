package com.giants3.android.reader.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.giants3.android.reader.vm.BaseViewModel;

public  abstract class BaseViewModelActivity<T extends ViewBinding,VM extends BaseViewModel> extends BaseActivity<T> {

    private VM viewModel;

    private ViewModelProvider viewModelProvider;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()));
        viewModel=createViewModel();

        viewModel.getWaitMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {


                if (value)
                    getViewWaiting().showWait();
                else {
                    getViewWaiting().hideWait();
                }






            }
        });
        viewModel.handleIntent(getIntent());
    }

    protected   VM createViewModel( ) {


        VM vm = viewModelProvider.get(getViewModelClass());
        return vm;
    }

    protected abstract Class<VM> getViewModelClass();


    protected final   VM getViewModel()
    {
        return viewModel;
    }
}
