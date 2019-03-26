package com.giants3.hd.android.mvp.workFlow;

import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity_erp.ErpWorkFlowItem;
import com.giants3.hd.noEntity.RemoteData;

public class ErpWorkFlowItemPresenter extends BasePresenter<ErpWorkFlowItemMvp.Viewer, ErpWorkFlowItemMvp.Model> implements ErpWorkFlowItemMvp.Presenter {
    @Override
    public ErpWorkFlowItemMvp.Model createModel() {
        return null;
    }


    @Override
    public void loadWorkFlowItems(String osNo, int itm, String code) {


        UseCaseFactory.getInstance().createFindWorkFlowItemsUseCase(osNo, itm, code).execute(new RemoteDataSubscriber<ErpWorkFlowItem>(this) {
            @Override
            protected void handleRemoteData(RemoteData<ErpWorkFlowItem> data) {


                if (data.isSuccess()) {

                    getView().bindData(data.datas);
                } else {
                    getView().showMessage(data.message);
                }


            }


        });


    }

    @Override
    public void start() {

    }

}
