package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.AppQuotationListPresenter;
import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerIPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationListViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerViewer;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_List;
import com.giants.hd.desktop.viewImpl.Panel_Quotation;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowWorker;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * Created by david on 2015/11/23.
 */
public class AppQuotationInternalFrame  extends BaseMVPFrame<AppQuotationListViewer> implements AppQuotationListPresenter {

    private FrameResultListener frameResultListener=   new FrameResultListener() {
        @Override
        public void onFrameResult(int requestCode, int resultCode, Object o) {
            if(resultCode==RESULT_OK)
            {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        getViewer().performSearch();
                    }
                });
            }

        }
    };;

    public AppQuotationInternalFrame() {
        super(ModuleConstant.TITLE_APP_QUOTATION);

        setSize(SCREEN_WIDTH*2/3,SCREEN_HEIGHT*2/3);





    }





    @Override
    protected AppQuotationListViewer createViewer() {
        return new Panel_AppQuotation_List(this);
    }

    @Override
    public void addOne() {

        AppQuotationDetailFrame frame=new AppQuotationDetailFrame( );
        frame.setFrameResultListener(frameResultListener);
        frame.showInMain();




    }

    @Override
    public void showOne(Quotation quotation) {

        AppQuotationDetailFrame frame=new AppQuotationDetailFrame(quotation );
        frame.setFrameResultListener(frameResultListener);
        frame.showInMain();




    }

    @Override
    public void search(String key, String dateStart, String dateEnd, long userId, int pageIndex, int pageSize) {




        UseCaseFactory.getInstance().createGetAppQuotationListUseCase(key,  dateStart,   dateEnd,   userId,pageIndex,pageSize).execute(new Subscriber<RemoteData<Quotation>>() {
            //            @Override
            public void onCompleted() {
                getViewer().hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<Quotation> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {


                    getViewer().bindData(workFlowRemoteData);

                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();





    }


}
