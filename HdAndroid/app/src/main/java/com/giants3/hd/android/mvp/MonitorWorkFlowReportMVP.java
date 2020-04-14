package com.giants3.hd.android.mvp;


import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.noEntity.RemoteData;

/**
 *  生产计划表
 * Created by davidleen29 on 2017/5/23.
 */

public interface MonitorWorkFlowReportMVP {


    interface Model extends NewModel {


    }

    interface Presenter extends NewPresenter<MonitorWorkFlowReportMVP.Viewer> {


        void loadData();


        void loadMore();

        void onKeyChange(String key);
    }

    interface Viewer extends NewViewer {


        void bindData(RemoteData<ErpWorkFlowReport> data);
    }
}
