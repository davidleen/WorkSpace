package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.StockOutSubmitIPresenter;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockOutSubmitList;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.viewImpl.Panel_StockOutSubmit_List;
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
 * 销库单明细列表
 * <p/>
 * Created by david on 20161119
 */
public class StockOutSumitFrame extends BaseInternalFrame implements StockOutSubmitIPresenter<StockSubmit> {
    Panel_StockOutSubmit_List panel_stockOutSubmit_list;


    RemoteData<StockSubmit> remoteData;
    private String dateStart;
    private String dateEnd;

    public StockOutSumitFrame() {
        super(ModuleConstant.TITLE_TRANSPORT_OUT_SEARCH);

    }

    @Override
    protected Container getCustomContentPane() {
        panel_stockOutSubmit_list = new Panel_StockOutSubmit_List(this);
        return panel_stockOutSubmit_list.getRoot();
    }

    @Override
    public void search(String key, final String startDate, final String endDate) {


        this.remoteData=null;
        this.dateStart="";
        this.dateEnd="";
        UseCaseFactory.getInstance().createStockXiaokuItemListUseCase(key, startDate, endDate).execute(new Subscriber<RemoteData<StockSubmit>>() {
            @Override
            public void onCompleted() {
                panel_stockOutSubmit_list.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panel_stockOutSubmit_list.hideLoadingDialog();
                panel_stockOutSubmit_list.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<StockSubmit> erpOrderRemoteData) {

                if(erpOrderRemoteData.isSuccess())
                {
                    remoteData=erpOrderRemoteData;
                    dateStart=startDate;
                    dateEnd=endDate;
                }

                panel_stockOutSubmit_list.setData(erpOrderRemoteData);
            }
        });

        panel_stockOutSubmit_list.showLoadingDialog();


    }

    @Override
    public void exportExcel() {


        if(StringUtils.isEmpty(dateEnd)|| remoteData==null)
            return ;

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockOutSubmitList(dateStart,dateEnd).report(remoteData, file.getAbsolutePath());
            panel_stockOutSubmit_list.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            panel_stockOutSubmit_list.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            panel_stockOutSubmit_list.showMesssage(e1.getMessage());

        }

    }




    @Override
    public void close() {

    }
}
