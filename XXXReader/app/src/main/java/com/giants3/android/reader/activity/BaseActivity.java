package com.giants3.android.reader.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.giants3.android.widgets.IViewWaiting;
import com.giants3.android.widgets.WaitingDialog;

/**
 * Created by davidleen29 on 2018/11/25.
 */

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    @Override
    public void onContentChanged() {
        super.onContentChanged();

    }


   private IViewWaiting viewWaiting;

    private T binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = createViewBinding();
        if (binding != null)
            setContentView(binding.getRoot());
        viewWaiting=createViewWaiting();



    }

    protected IViewWaiting createViewWaiting()
    {
        return new  WaitingDialog(this);
    }



    protected IViewWaiting getViewWaiting()
    {
        return viewWaiting;
    }



    protected abstract T createViewBinding();


    protected T getViewBinding() {
        return binding;
    }

}