package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.BaseMVPDialog;
import com.giants.hd.desktop.mvp.DialogViewer;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.ProductValueHistoryPresenter;
import com.giants.hd.desktop.mvp.viewer.ProductValueHistoryViewer;
import com.giants.hd.desktop.viewImpl.Panel_Product_Value_History;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.ProductValueHistory;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;

import javax.swing.*;
import java.awt.*;

/**
 * Created by david on 2015/11/23.
 */
public class ProductValueHistoryDialog extends BaseMVPDialog<ProductValueHistory, ProductValueHistoryViewer> implements ProductValueHistoryPresenter {


    private long productId;
    public ProductValueHistoryDialog(Window window)
    {
        this(window,0);
    }
    public ProductValueHistoryDialog(Window window, final long productId) {
        super(window, "产品价格变动历史");
          this.productId = productId;
        setSize(900, 600);
        setModal(false);
        getViewer().setSearchable(productId<=0);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                searchData( );
            }
        });


    }


    @Override
    protected ProductValueHistoryViewer createViewer() {
        return new Panel_Product_Value_History(this);
    }


    public void searchData() {



        if(productId>0) {

            UseCaseFactory.getInstance().createGetUseCase(HttpUrl.findProductValueHistory(productId), ProductValueHistory.class).execute(new RemoteDataSubscriber<ProductValueHistory>(getViewer()) {
                @Override
                protected void handleRemoteData(RemoteData<ProductValueHistory> data) {

                    if (data.isSuccess()) {
                        getViewer().bindData(data);
                    }


                }


            });
            getViewer().showLoadingDialog();
        }



    }

    @Override
    public void findHistoryData(ProductValueHistory item) {
        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.findProductDetailByValueHistory(item.id), ProductDetail.class).execute(new RemoteDataSubscriber<ProductDetail>(new DialogViewer(ProductValueHistoryDialog.this)) {


            @Override
            protected void handleRemoteData(RemoteData<ProductDetail> data) {
                ProductDetail productDetail = data.datas.get(0);


                ProductDetailFrame productDetailFrame = new ProductDetailFrame(productDetail, null, ProductDetailFrame.VIEW_TYPE_HISTORY);

                JFrame frame = productDetailFrame;
                frame.setLocationRelativeTo(ProductValueHistoryDialog.this);
                frame.setVisible(true);


            }


        });


    }

    @Override
    public void search(String keyText, boolean isAccurate, int pageIndex, int pageSize) {
        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.searchProductValueHistory( keyText,pageIndex,pageSize,!isAccurate), ProductValueHistory.class).execute(new RemoteDataSubscriber<ProductValueHistory>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<ProductValueHistory> data) {

                if (data.isSuccess()) {
                    getViewer().bindData(data);
                }


            }


        });
        getViewer().showLoadingDialog();
    }
}
