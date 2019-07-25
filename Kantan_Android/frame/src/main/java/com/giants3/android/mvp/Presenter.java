package com.giants3.android.mvp;

/**
 * Created by davidleen29 on 2016/10/10.
 */

public interface Presenter<T extends Viewer>   {
    void attachView(T view);

    void start();

    void detachView();


}
