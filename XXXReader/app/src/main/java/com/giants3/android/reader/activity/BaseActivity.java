package com.giants3.android.reader.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.giants3.ClassHelper;
import com.giants3.android.widgets.IViewWaiting;
import com.giants3.android.widgets.WaitingDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

        binding= createViewBinding();
        if(binding==null)
            binding =createViewBindAuto();
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



    protected     T createViewBinding()
    {
        return null;
    }

    protected T createViewBindAuto()
    {




        T restult=null;
        try {
                    Class<T> clas = ClassHelper.findParameterizedClass(getClass(),BaseActivity.class,ViewBinding.class);
                    Method inflate = clas.getMethod("inflate", LayoutInflater.class);
                    restult= (T) inflate.invoke(null,LayoutInflater.from(this));


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }   catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return restult;


    }



    protected T getViewBinding() {
        return binding;
    }

}