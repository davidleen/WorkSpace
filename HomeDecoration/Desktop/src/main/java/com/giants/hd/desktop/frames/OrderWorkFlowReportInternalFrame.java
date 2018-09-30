package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.OrderWorkFlowReportIPresenter;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.mvp.viewer.OrderWorkFlowReportViewer;
import com.giants.hd.desktop.viewImpl.Panel_OrderWorkFlowReport;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrderItem;


import com.giants3.hd.entity.ErpOrderItemProcess;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 订单生产流程报表界面
 * Created by david on 20160925
 */
public class OrderWorkFlowReportInternalFrame extends BaseInternalFrame implements OrderWorkFlowReportIPresenter {
    OrderWorkFlowReportViewer viewer;


    java.util.List<ErpOrderItemProcess> items = null;

    public OrderWorkFlowReportInternalFrame() {
        super(ModuleConstant.TITLE_ORDER_WORK_FLOW_REPORT);

        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                searchUnDoneOrder(  );
            }
        });

    }

    public void init() {

//        setMinimumSize(new Dimension(1024, 768));
//        pack();
    }


    @Override
    protected Container getCustomContentPane() {
        viewer = new Panel_OrderWorkFlowReport(this);
        return viewer.getRoot();
    }

    @Override
    public void searchUnDoneOrder( ) {


        UseCaseFactory.getInstance().createUnCompleteOrderWorkFlowReportUseCase(  ).execute(new Subscriber<RemoteData<ErpOrderItemProcess>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpOrderItemProcess> orderReportItemRemoteData) {

                setUnCompleteRemoteData(orderReportItemRemoteData);
            }
        });

        viewer.showLoadingDialog();


    }

    private void setUnCompleteRemoteData(RemoteData<ErpOrderItemProcess> remoteData) {
        if (remoteData.isSuccess())
            items = remoteData.datas;
        viewer.setUnCompleteData(remoteData);
    }


    /**
     * 导出报表
     */
    @Override
    public void export() {

        if (items == null) {
            viewer.showMesssage("无数据导出");
            return;
        }

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


//        try {
//            new Report_Excel_StockOutPlan().report(items, file.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            viewer.showMesssage(e.getMessage());
//        } catch (HdException e) {
//            e.printStackTrace();
//            viewer.showMesssage(e.getMessage());
//        }


    }


    @Override
    public void search(String key, int pageIndex, int pageSize) {


        UseCaseFactory.getInstance().createOrderWorkFlowReportUseCase(key,pageIndex,pageSize  ).execute(new Subscriber<RemoteData<ErpOrderItem>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpOrderItem> orderReportItemRemoteData) {

                viewer.setData(orderReportItemRemoteData);
            }
        });

        viewer.showLoadingDialog();
    }
}
