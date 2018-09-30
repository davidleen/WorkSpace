package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.StockInAndSubmitIPresenter;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockInAndSubmitList;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockInAndSubmitList2;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.viewImpl.Panel_StockInAndSubmit_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.exception.HdException;
import rx.Subscriber;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 进库与缴库
 * <p/>
 * Created by david on 20161119
 */
public class StockInAndSubmitFrame extends BaseInternalFrame implements StockInAndSubmitIPresenter<StockSubmit> {
    Panel_StockInAndSubmit_List panel_stockInAndSubmit_list;


    RemoteData<StockSubmit> remoteData;
    private String dateStart;
    private String dateEnd;

    public StockInAndSubmitFrame() {
        super(ModuleConstant.TITLE_STOCK_IN_AND_SUBMIT);

    }

    @Override
    protected Container getCustomContentPane() {
        panel_stockInAndSubmit_list = new Panel_StockInAndSubmit_List(this);
        return panel_stockInAndSubmit_list.getRoot();
    }

    @Override
    public void search(String key, final String startDate, final String endDate) {


        this.remoteData=null;
        this.dateStart="";
        this.dateEnd="";
        UseCaseFactory.getInstance().createStockInAndSubmitListUseCase(key, startDate, endDate).execute(new Subscriber<RemoteData<StockSubmit>>() {
            @Override
            public void onCompleted() {
                panel_stockInAndSubmit_list.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panel_stockInAndSubmit_list.hideLoadingDialog();
                panel_stockInAndSubmit_list.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<StockSubmit> erpOrderRemoteData) {

                if(erpOrderRemoteData.isSuccess())
                {
                    remoteData=erpOrderRemoteData;
                    dateStart=startDate;
                    dateEnd=endDate;
                }

                panel_stockInAndSubmit_list.setData(erpOrderRemoteData);
            }
        });

        panel_stockInAndSubmit_list.showLoadingDialog();


    }

    @Override
    public void exportExcel() {


        if(StringUtils.isEmpty(dateEnd)|| remoteData==null)
            return ;

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockInAndSubmitList(dateStart,dateEnd).report(remoteData, file.getAbsolutePath());
            panel_stockInAndSubmit_list.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            panel_stockInAndSubmit_list.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            panel_stockInAndSubmit_list.showMesssage(e1.getMessage());

        }

    }

    /**
     * 格式2
     */
    @Override
    public void exportExcel2() {
        if(StringUtils.isEmpty(dateEnd)|| remoteData==null)
            return ;

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockInAndSubmitList2(dateStart,dateEnd).report(remoteData, file.getAbsolutePath());
            panel_stockInAndSubmit_list.showMesssage("格式2导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            panel_stockInAndSubmit_list.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            panel_stockInAndSubmit_list.showMesssage(e1.getMessage());

        }

    }


    @Override
    public void close() {

    }
}
