package com.giants3.android.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



/**
 * startActivityForResult 的一个代理类
 * 方便adapter 等 不在activity类中的对象使用 直接用的startActivityForResult 直接设置回调
 * 避免寻找adapter寻找 所在的activity做监听
 *
 *
 * 原理:在activity上 挂一个代理用的fragment,
 * 在fragment中 启动fragment的startActivityForResult 方法
 * 并在onActivityResult在监听
 *
 *
 */
public  class  FragmentForResult extends Fragment {

    private static String FRAGMENT_TAG ="fragment_Result";

    public    Class aClass = null;
    public int requestCode = 0;
    public ActResult result;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = new Intent(getContext(), aClass);
        it.putExtras(getArguments());
        startActivityForResult(it,requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (result != null) {
            result.onActivityResult(requestCode,resultCode,data);
        }
    }



    public  static <T extends Activity> void startForResult(Context context, Class<T> clazz , int requestCode, Bundle data, ActResult actResult ){
        context = AndroidCompatUtils.findActivityFromContext(context);
        if (context instanceof FragmentActivity){
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (fragmentByTag == null){
                fragmentByTag = new FragmentForResult();
            }
            if (fragmentByTag instanceof FragmentForResult) {
                FragmentForResult forResult = (FragmentForResult) fragmentByTag;
                forResult.aClass = clazz;
                forResult.result = actResult;
                forResult.requestCode = requestCode;
                if(data==null)
                {
                    data=new Bundle();
                }
                forResult.setArguments(data);



                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                if (forResult.isAdded()) {
                    fragmentTransaction.remove(forResult);
                }
                fragmentTransaction.add(forResult, FRAGMENT_TAG);
                fragmentTransaction.commitNowAllowingStateLoss();

            }



        }

    }






    public interface ActResult{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
