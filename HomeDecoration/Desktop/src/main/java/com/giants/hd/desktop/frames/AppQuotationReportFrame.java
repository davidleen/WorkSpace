package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.AppQuotationReportPresenter;
import com.giants.hd.desktop.mvp.presenter.AppQuotationSyncPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationReportViewer;
import com.giants.hd.desktop.mvp.viewer.AppQuotationSyncViewer;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Report;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Sync;
import com.giants3.hd.domain.interractor.UseCase;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;

/**
 * Created by david on 2015/11/23.
 */
public class AppQuotationReportFrame extends BaseMVPFrame<AppQuotationReportViewer> implements AppQuotationReportPresenter {


    public AppQuotationReportFrame() {
        super(ModuleConstant.TITLE_APP_QUOTATION_REPORT);


    }




    @Override
    public void showQuoteCountReport() {



        AppQuotationCountReportFrame frame=new AppQuotationCountReportFrame();
        frame.showInMain();



    }

    @Override
    protected AppQuotationReportViewer createViewer() {
        return new Panel_AppQuotation_Report(this);
    }
}
