package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.StockXiaokuIPresenter;
import com.giants.hd.desktop.viewImpl.Panel_StockXiaoku_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
import rx.Subscriber;

import java.awt.*;

/**
 * 销库界面
 * <p/>
 * Created by david on 20161119
 */
public class StockXiaokuFrame extends BaseInternalFrame implements StockXiaokuIPresenter<StockSubmit> {
    Panel_StockXiaoku_List panel_stock_list;

    public StockXiaokuFrame() {
        super(ModuleConstant.TITLE_TRANSPORT_OUT);


    }

    @Override
    protected Container getCustomContentPane() {
        panel_stock_list = new Panel_StockXiaoku_List(this);
        return panel_stock_list.getRoot();
    }


    @Override
    public void close() {

    }

    @Override
    public void search(String key,int pageIndex, int pageSize) {


        UseCaseFactory.getInstance().createStockXiaokuListUseCase(key,pageIndex,pageSize).execute(new Subscriber<RemoteData<StockXiaoku>>() {
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
            public void onNext(RemoteData<StockXiaoku> erpOrderRemoteData) {

                panel_stock_list.setData(erpOrderRemoteData);
            }
        });

        panel_stock_list.showLoadingDialog();

    }

    @Override
    public void onListItemClick(StockXiaoku xiaoku) {

        //点击单行 跳转 销库单。


         StockXiaokuDetailFrame frame =new StockXiaokuDetailFrame( xiaoku);
        frame.setLocationRelativeTo(StockXiaokuFrame.this);
        frame.setVisible(true);

    }
}
