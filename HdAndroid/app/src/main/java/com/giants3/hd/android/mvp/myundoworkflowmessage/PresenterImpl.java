package com.giants3.hd.android.mvp.myundoworkflowmessage;

import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.MyUndoWorkFlowMessageMVP;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.noEntity.RemoteData;

/**
 * Created by davidleen29 on 2017/6/3.
 */

public class PresenterImpl extends BasePresenter<MyUndoWorkFlowMessageMVP.Viewer, MyUndoWorkFlowMessageMVP.Model> implements MyUndoWorkFlowMessageMVP.Presenter {

    @Override
    public void start() {

    }

    @Override
    public void setKey(String s) {

        getModel().setKey(s);
    }

    @Override
    public MyUndoWorkFlowMessageMVP.Model createModel() {
        return new ModelImpl();
    }

    @Override
    public void loadData() {


        if (getView() == null) return;

        String key = getModel().getKey();
        UseCaseFactory.getInstance().createGetUnHandleWorkFlowMessageCase(key).execute(new RemoteDataSubscriber<WorkFlowMessage>(this) {

            @Override
            protected void handleRemoteData(RemoteData<WorkFlowMessage> data) {
                if (getView() == null) return;

                if (data.isSuccess())
                    getView().setData(data);
            }
        });


        getView().showWaiting();


    }

}
