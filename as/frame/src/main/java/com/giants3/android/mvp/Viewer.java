package com.giants3.android.mvp;

/**
 * Created by davidleen29 on 2016/10/10.
 */

public interface Viewer extends AndroidRouter {
    void hideWaiting();
    void showMessage(String message);
    void showWaiting();
    public void showWaiting(String message);


}
