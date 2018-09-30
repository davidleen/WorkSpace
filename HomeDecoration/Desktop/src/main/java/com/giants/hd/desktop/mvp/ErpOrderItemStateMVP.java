package com.giants.hd.desktop.mvp;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlowReport;

/**
 * Created by davidleen29 on 2017/5/13.
 */
public class ErpOrderItemStateMVP {

    public interface Model extends IModel
    {

    }

    public interface Viewer extends IViewer
    {

        void setData(RemoteData<ErpOrderItemProcess> remoteData);
        void setReportData(RemoteData<ErpWorkFlowReport> workFlowReportRemoteData);
    }

    public interface Presenter extends IPresenter
    {


          void onItemClick(ErpOrderItemProcess data);
    }
}
