package com.giants3.hd.android.mvp.workFlow;

import com.giants3.hd.android.mvp.NewModel;
import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.entity.OrderItemWorkFlowState;
import com.giants3.hd.entity_erp.ErpWorkFlowItem;
import com.giants3.hd.noEntity.RemoteData;

import java.util.List;

import rx.Subscriber;

/**
 * Created by davidleen29 on 2016/10/10.
 */

public interface ErpWorkFlowItemMvp {

    interface Model extends NewModel {


    }

    interface Presenter extends NewPresenter<Viewer> {
        void loadWorkFlowItems(String osNo,int itm,String code);


    }

    interface Viewer extends NewViewer {
        void bindData(List<ErpWorkFlowItem> datas);


    }

}
