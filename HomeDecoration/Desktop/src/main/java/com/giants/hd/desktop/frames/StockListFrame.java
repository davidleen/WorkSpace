package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.StockListIPresenter;
import com.giants.hd.desktop.viewImpl.Panel_Stock_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;

import com.giants3.hd.entity_erp.ErpStockOut;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;

/**
 * 库存 列表
 *
 * Created by david on 2015/11/23.
 */
public class StockListFrame extends BaseInternalFrame implements StockListIPresenter<ErpStockOut> {
    Panel_Stock_List panel_stock_list;
    public StockListFrame( ) {
        super(ModuleConstant.TITLE_STOCK_OUT);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                search("",-1,0,20);
            }
        });

    }

    @Override
    protected Container getCustomContentPane() {
        panel_stock_list =new Panel_Stock_List(this);
        return panel_stock_list.getRoot();
    }

    @Override
    public void search(String key,long salesId, int pageIndex, int pageSize) {


        UseCaseFactory.getInstance().createStockOutListUseCase(key,salesId,pageIndex,pageSize).execute(new Subscriber<RemoteData<ErpStockOut>>() {
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
            public void onNext(RemoteData<ErpStockOut> erpOrderRemoteData) {

                panel_stock_list.setData(erpOrderRemoteData);
            }
        });

        panel_stock_list.showLoadingDialog();


    }

    @Override
    public void onListItemClick(ErpStockOut data) {
        Frame frame=new StockOutDetailFrame( data);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);



    }



    @Override
    public void close() {

    }
}
