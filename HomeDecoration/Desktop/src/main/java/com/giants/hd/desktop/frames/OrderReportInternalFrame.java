package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.OrderReportIPresenter;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockOutPlan;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.mvp.viewer.OrderReportViewer;
import com.giants.hd.desktop.viewImpl.Panel_Order_Report;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.OrderReportItem;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** 订单报表业务逻辑层
 * Created by david on 2015/11/23.
 */
public class OrderReportInternalFrame extends BaseInternalFrame implements OrderReportIPresenter {
    OrderReportViewer orderReportViewer;


    java.util.List<OrderReportItem> items=null;
    public OrderReportInternalFrame( ) {
        super(ModuleConstant.TITLE_ORDER_REPORT);

        init();

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                search("",,0,20);
//            }
//        });

    }
    public void init() {

//        setMinimumSize(new Dimension(1024, 768));
//        pack();
    }


    @Override
    protected Container getCustomContentPane() {
        orderReportViewer=new Panel_Order_Report( this);
        return orderReportViewer.getRoot();
    }
    @Override
    public void search(long userId, String dateStart,String dateEnd, int pageIndex, int pageSize) {





        UseCaseFactory.getInstance().createOrderReportUseCase(  userId,dateStart,dateEnd,pageIndex,pageSize).execute(new Subscriber<RemoteData<OrderReportItem>>() {
            @Override
            public void onCompleted() {
                orderReportViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderReportViewer.hideLoadingDialog();
                orderReportViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<OrderReportItem> orderReportItemRemoteData) {

                setRemoteData(orderReportItemRemoteData);
            }
        });

        orderReportViewer.showLoadingDialog();


    }

    private void setRemoteData(RemoteData<OrderReportItem> remoteData)
    {
        if(remoteData.isSuccess())
           items=remoteData.datas;
        orderReportViewer.setData(remoteData);
    }

    /**
     * 订单详情读取
     * @param erpOrder
     */
    @Override
    public void loadOrderDetail(ErpOrder erpOrder) {


        Frame frame=new OrderDetailFrame(erpOrder);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);






    }


    /**
     * 导出报表
     */
    @Override
    public void export() {

            if(items==null) {
                orderReportViewer.showMesssage("无数据导出");
                return;
            }

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;



        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                new Report_Excel_StockOutPlan().report(items,file.getAbsolutePath());
                return null;
            }

            @Override
            protected void done() {
                super.done();
                orderReportViewer.hideLoadingDialog();
                try{
                    get();
                    orderReportViewer.showMesssage("成功导出文件到："+file.getAbsolutePath());
                }catch (Throwable t)
                {
                    orderReportViewer.showMesssage("导出出错："+t.getMessage());
                }



            }

        }.execute();


        orderReportViewer.showLoadingDialog();
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        } catch (HdException e) {
//            e.printStackTrace();
//            orderReportViewer.showMesssage(e.getMessage());
//        }


    }
}
