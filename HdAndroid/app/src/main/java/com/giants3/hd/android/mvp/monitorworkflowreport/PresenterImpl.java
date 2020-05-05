package com.giants3.hd.android.mvp.monitorworkflowreport;

import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.MonitorWorkFlowReportMVP;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.noEntity.RemoteData;

/**
 * Created by davidleen29 on 2017/6/3.
 */

public class PresenterImpl extends BasePresenter<MonitorWorkFlowReportMVP.Viewer, MonitorWorkFlowReportMVP.Model> implements MonitorWorkFlowReportMVP.Presenter {

    private String key="";
    int defaultPageSize=30;
    private RemoteData<ErpWorkFlowReport> remoteData;

    @Override
    public void start() {
        loadData();

    }

    @Override
    public MonitorWorkFlowReportMVP.Model createModel() {
        return new ModelImpl();
    }

    @Override
    public void loadData( ) {


        loadData(0, defaultPageSize);


    }

    @Override
    public void loadMore() {
        int pageIndex=remoteData==null?0:remoteData.pageIndex+1;
        int pageSize=remoteData==null?0:remoteData.pageSize;

        loadData(pageIndex, pageSize);

    }

    private void loadData(int pageIndex, int pageSize) {
        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.getMonitorWorkFlowReports(key, pageIndex, pageSize), ErpWorkFlowReport.class).execute(new RemoteDataSubscriber<ErpWorkFlowReport>(this) {
            @Override
            protected void handleRemoteData(RemoteData<ErpWorkFlowReport> data) {


                                    if (remoteData == null) remoteData = data;
                                    else {

                                        if (data.pageIndex == 0) {
                                            remoteData.datas.clear();
                                        }
                                        remoteData.datas.addAll(data.datas);
                                        remoteData.pageIndex = data.pageIndex;
                                        remoteData.pageSize = data.pageSize;
                                    }

                getView().bindData(remoteData);


            }
        });


        getView().showWaiting();
    }

    @Override
    public void onKeyChange(String key) {

        this.key = key;
    }
}
