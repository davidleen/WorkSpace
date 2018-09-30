package com.giants.hd.desktop.mvp;


import com.giants3.hd.noEntity.RemoteData;
import rx.Subscriber;

/**
 * subscriber    网络数据额统一处理。   未登录会自动跳转登录
 * <p>
 * <p>
 * Created by davidleen29 on 2017/6/4.
 */

public abstract class RemoteDataSubscriber<T> extends Subscriber<RemoteData<T>> {


    private IViewer viewer;

    public RemoteDataSubscriber(IViewer viewer) {


        this.viewer = viewer;
    }

    @Override
    public void onCompleted() {

        if (viewer== null) return;
        viewer.hideLoadingDialog();
    }

    @Override
    public void onError(Throwable e) {
        if (viewer== null) return;
        viewer.hideLoadingDialog();
        e.printStackTrace();
        viewer.showMesssage(e.getMessage());

    }

    @Override
    public void onNext(RemoteData<T> tRemoteData) {

        if (tRemoteData.isSuccess()) {
            handleRemoteData(tRemoteData);
        } else {

            if (viewer!= null)  ;

            viewer.showMesssage(tRemoteData.message);

            if (tRemoteData.code == RemoteData.CODE_UNLOGIN) {
                viewer.showMesssage("登录超时");
            }
        }

    }



    protected abstract void handleRemoteData(RemoteData<T> data);
}
