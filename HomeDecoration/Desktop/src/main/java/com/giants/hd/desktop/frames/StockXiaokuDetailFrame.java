package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.StockXiaokuDetailIPresenter;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockXiaoku;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.viewImpl.Panel_StockXiaokuDetail;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
import com.giants3.hd.exception.HdException;
import rx.Subscriber;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 销库单详情界面
 * <p/>
 * Created by david on 20161119
 */
public class StockXiaokuDetailFrame extends BaseFrame implements StockXiaokuDetailIPresenter {
    Panel_StockXiaokuDetail panel_stock_list;
    private StockXiaoku stockXiaoku;
    private java.util.List<StockSubmit> xiaokuItems;

    public StockXiaokuDetailFrame(StockXiaoku stockXiaoku) {
        super(ModuleConstant.TITLE_TRANSPORT_OUT);


        this.stockXiaoku = stockXiaoku;

        initView();
    }


    protected void initView() {
        panel_stock_list = new Panel_StockXiaokuDetail(this);
        setContentPane(panel_stock_list.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();

        panel_stock_list.bindData(stockXiaoku);
        loadXiaokuItemList(stockXiaoku.ps_no);

    }


    @Override
    public void close() {

    }


    public void loadXiaokuItemList(String ps_no) {


        UseCaseFactory.getInstance().createStockXiaokuItemListUseCase(ps_no).execute(new Subscriber<RemoteData<StockSubmit>>() {
            @Override
            public void onCompleted() {
                panel_stock_list.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panel_stock_list.hideLoadingDialog();
                panel_stock_list.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<StockSubmit> erpOrderRemoteData) {

                xiaokuItems=erpOrderRemoteData.datas;
                panel_stock_list.setItemData(erpOrderRemoteData);
            }
        });

        panel_stock_list.showLoadingDialog();

    }


    @Override
    public void exportExcel() {


        if (xiaokuItems == null)
            return;

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockXiaoku(stockXiaoku, xiaokuItems).report(file.getAbsolutePath());
            panel_stock_list.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            panel_stock_list.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            panel_stock_list.showMesssage(e1.getMessage());

        }

    }


}
