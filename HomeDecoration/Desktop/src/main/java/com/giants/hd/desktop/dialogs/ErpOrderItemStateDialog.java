package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.ErpOrderItemStateMVP;
import com.giants.hd.desktop.mvp.model.ErpOrderItemProcessModel;
import com.giants.hd.desktop.viewImpl.Panel_ErpOrderItemStatesList;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlowReport;
import rx.Subscriber;

import java.awt.*;

/**
 * 订单排产明细列表
 */
public class ErpOrderItemStateDialog extends MVPDialog<ErpOrderItemProcess, ErpOrderItemStateMVP.Viewer, ErpOrderItemStateMVP.Model> implements ErpOrderItemStateMVP.Presenter {


    public ErpOrderItemStateDialog(Window window, String osNo, String prdNo) {
        super(window, "订单款项排厂明细");
        setMinimumSize(new Dimension(800, 600));

        loadData(osNo, prdNo);


    }


    private void loadData(final String osNo, final String prdNo) {


        UseCaseFactory.getInstance().createGetErpOrderItemProcessUseCase(osNo, prdNo).execute(new Subscriber<RemoteData<ErpOrderItemProcess>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<ErpOrderItemProcess> data) {

                getViewer().hideLoadingDialog();
                if (data.isSuccess()) {
                    getViewer().setData(data);

                    loadReportSilence(osNo, prdNo);
                }
            }

        });
        getViewer().showLoadingDialog();


    }


    private void loadReportSilence(String os_no, String prd_no) {

        UseCaseFactory.getInstance().createGetErpOrderItemReportUseCase(os_no, prd_no).execute(new Subscriber<RemoteData<ErpWorkFlowReport>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {


            }


            @Override
            public void onNext(RemoteData<ErpWorkFlowReport> data) {


                if (data.isSuccess()) {
                    getViewer().setReportData(data);

                }
            }

        });


    }

    @Override
    protected ErpOrderItemStateMVP.Viewer createViewer() {
        return new Panel_ErpOrderItemStatesList(this);
    }

    @Override
    protected ErpOrderItemStateMVP.Model createModel() {
        return new ErpOrderItemProcessModel();
    }

    @Override
    public void onItemClick(ErpOrderItemProcess data) {


    }
}
