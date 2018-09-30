package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.AuthRelateDetailIPresenter;
import com.giants.hd.desktop.mvp.viewer.AuthRelateDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_Auth_Relates;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.app.AppQuoteAuth;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.OrderAuth;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.StockOutAuth;
import com.giants3.hd.entity.User;
import com.google.inject.Inject;
import rx.Subscriber;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * n 细分权限业务处理层
 */
public class AuthRelatesDialog extends BaseDialog implements AuthRelateDetailIPresenter {


    @Inject
    ApiManager apiManager;

    List<User> sales;
    int showRow;
    private AuthRelateDetailViewer viewer;


    //当前显示的面板  0  报价 1 订单  2 出库
    private int selectedPane = 0;

    private List<QuoteAuth> quoteAuths;
    private List<AppQuoteAuth> appQuoteAuths;

   private List<StockOutAuth> stockOutAuths;

    private List<OrderAuth> orderAuths;


    public AuthRelatesDialog(Window window) {
        super(window);
        setTitle("细分权限");
        viewer = new Panel_Auth_Relates(this, this);
        setContentPane(viewer.getRoot());
        sales = CacheManager.getInstance().bufferData.salesmans;
        viewer.showAllSales(sales);

        loadQuoteAuth();
        loadStockOutAuth();
        loadOrderAuth();
        loadAppQuoteAuth();
    }

    private void loadQuoteAuth() {

        UseCaseFactory.getInstance().createQuoteAuthListCase().execute(new Subscriber<RemoteData<QuoteAuth>>() {
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
            public void onNext(RemoteData<QuoteAuth> erpOrderRemoteData) {
                if (erpOrderRemoteData.isSuccess()) {
                    setQuoteAuthData(erpOrderRemoteData.datas);
                }


            }


        });
        viewer.showLoadingDialog();
    }



    private void loadStockOutAuth() {

        UseCaseFactory.getInstance().createStockOutAuthListCase().execute(new Subscriber<RemoteData<StockOutAuth>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                 e.printStackTrace();
            }

            @Override
            public void onNext(RemoteData<StockOutAuth> remoteData) {
                if (remoteData.isSuccess()) {
                     setStockOutAuthData(remoteData.datas);
                }


            }


        });

    }



    private void setStockOutAuthData(List<StockOutAuth> datas) {

        stockOutAuths = datas;
        viewer.showStockOutAuthList(datas);


    }

    private void loadOrderAuth() {

        UseCaseFactory.getInstance().createOrderAuthListCase().execute(new Subscriber<RemoteData<OrderAuth>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(RemoteData<OrderAuth> remoteData) {
                if (remoteData.isSuccess()) {
                    setOrderAuthData(remoteData.datas);
                }


            }


        });

    }

    private void loadAppQuoteAuth() {

        UseCaseFactory.getInstance().createGetAppQuoteAuthListCase().execute(new Subscriber<RemoteData<AppQuoteAuth>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(RemoteData<AppQuoteAuth> remoteData) {
                if (remoteData.isSuccess()) {
                    setAppQuoteAuthData(remoteData.datas);
                }


            }


        });

    }

    private void setOrderAuthData(List<OrderAuth> datas) {

        orderAuths = datas;
        viewer.showOrderAuthList(orderAuths);

    }

    private void setAppQuoteAuthData(List<AppQuoteAuth> quoteAuth) {
        appQuoteAuths = quoteAuth;
        viewer.showAppQuoteAuthList(quoteAuth);
    }
 private void setQuoteAuthData(List<QuoteAuth> quoteAuth) {
        quoteAuths = quoteAuth;
        viewer.showQuoteAuthList(quoteAuth);
    }


    /**
     * 显示关联业务员状态
     *
     * @param relatedSales
     */
    private void showSaleRelate(String relatedSales) {


        String[] ids = StringUtils.isEmpty(relatedSales) ? null : relatedSales.split(",|,");


        List<Integer> indexes = new ArrayList<>();
        int size = sales.size();
        if (ids
                != null)
            for (int i = 0; i < size; i++) {

                for (String id : ids) {

                    if (String.valueOf(sales.get(i).id).equals(id)) {
                        indexes.add(i);
                        break;
                    }
                }

            }
        viewer.bindRelateSalesData(indexes);
    }




    @Override
    public void onQuoteAuthRowSelected(int row) {
        showRow=row;
        String  relateSales="";
        switch (selectedPane)
        {

            case 0:
                relateSales= quoteAuths.get(row).relatedSales;

                break;
            case 1:
                relateSales= orderAuths.get(row).relatedSales;
                break;
            case 2:
                relateSales= stockOutAuths.get(row).relatedSales;
                break;
        }
        showSaleRelate(relateSales);
    }

    @Override
    public void setSelectedPane(int selectedIndex) {


        selectedPane = selectedIndex;

        showRow = 0;
        switch (selectedPane) {
            case 0:
                viewer.showQuoteRow( showRow);
                break;
            case 1:
                viewer.showOrderRow( showRow);
                break;
            case 2:
                viewer.showStockOutRow( showRow);
                break;
        }
        onQuoteAuthRowSelected(selectedPane);


    }

    @Override
    public void onRelateUsesSelected(List<Integer> indexes) {


        StringBuilder sb = new StringBuilder();
        for (int i : indexes) {

            sb.append(sales.get(i).id).append(",");


        }


        switch (selectedPane) {
            case 0:
                quoteAuths.get(showRow).relatedSales = sb.toString();
                break;
            case 1:
                orderAuths.get(showRow).relatedSales = sb.toString();
                break;
            case 2:
                stockOutAuths.get(showRow).relatedSales = sb.toString();
                break;
        }


    }

    @Override
    public void save() {

        switch (selectedPane)
        {

            case 0:
                saveQuoteAuth();
                break;
            case 1:
                saveOrderAuth();
                break;
            case 2:
                saveStockOutAuth();
                break;

            case 3:
                saveAppQuoteAuth();
                break;
        }






    }

    private void saveStockOutAuth() {

        if(stockOutAuths==null)
        {
            viewer.showMesssage("数据异常"); return;
        }


        UseCaseFactory.getInstance().createStockOutAuthSaveCase(stockOutAuths).execute(new Subscriber<RemoteData<StockOutAuth>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<StockOutAuth> remoteData) {
                viewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    viewer.showMesssage("保存成功");
                    setStockOutAuthData(remoteData.datas);
                }


            }


        });


        viewer.showLoadingDialog();


    }

    private void saveOrderAuth() {


        if(orderAuths==null)
        {
            viewer.showMesssage("数据异常"); return;
        }


        UseCaseFactory.getInstance().createOrderAuthSaveCase(orderAuths).execute(new Subscriber<RemoteData<OrderAuth>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<OrderAuth> remoteData) {
                viewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    viewer.showMesssage("保存成功");
                    setOrderAuthData(remoteData.datas);
                }


            }


        });


        viewer.showLoadingDialog();
    }

    /**
     * 
     */
    private void saveQuoteAuth() {


        if(quoteAuths==null)
        {
            viewer.showMesssage("数据异常"); return;
        }


        UseCaseFactory.getInstance().createQuoteAuthSaveCase(quoteAuths).execute(new Subscriber<RemoteData<QuoteAuth>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<QuoteAuth> remoteData) {
                viewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    viewer.showMesssage("保存成功");
                    setQuoteAuthData(remoteData.datas);
                }


            }


        });


        viewer.showLoadingDialog();
        
        
        
    }/**
     *
     */
    private void saveAppQuoteAuth() {


        if(appQuoteAuths==null)
        {
            viewer.showMesssage("数据异常"); return;
        }


        UseCaseFactory.getInstance().createSaveAppQuoteAuthCase(appQuoteAuths).execute(new RemoteDataSubscriber< AppQuoteAuth>(viewer) {



            @Override
            protected void handleRemoteData(RemoteData<AppQuoteAuth> data) {

                if (data.isSuccess()) {
                    viewer.showMesssage("保存成功");
                    setAppQuoteAuthData(data.datas);
                }


            }




        });


        viewer.showLoadingDialog();



    }
}
