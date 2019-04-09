package com.giants3.hd.android.mvp.quotation;

import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.QuotationXKItem;
import com.giants3.hd.logic.QuotationAnalytics;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;

/**
 * 广交会报价 P层接口
 * Created by davidleen29 on 2016/9/23.
 */

public class QuotationDetailPresenterImpl extends BasePresenter<QuotationDetailMVP.Viewer, QuotationDetailMVP.Model> implements QuotationDetailMVP.Presenter {


   private Quotation quotation;
    private QuotationDetail quotationDetail;

    @Override
    public QuotationDetailMVP.Model createModel() {
        return new QuotationDetailModelImpl();
    }

    @Override
    public void start() {

    }


    @Override
    public void init(Quotation quotation) {

        this.quotation = quotation;
    }

    @Override
    public void loadData() {
        if (quotation == null) return;
        getView().showWaiting();
        UseCaseFactory.getInstance().createGetQuotationDetail(quotation.id).execute(new RemoteDataSubscriber<QuotationDetail>(this) {


            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> remoteData) {
                if (remoteData.isSuccess()) {
                    quotationDetail = remoteData.datas.get(0);
                    getView().bindData(quotationDetail);
                }
            }
        });

    }

    @Override
    public void loadProductAndCalculatePrice(final QuotationXKItem quotationItem, final float newRatio, ItemListAdapter<QuotationXKItem> quotationItemItemListAdapter) {

        UseCaseFactory.getInstance().createGetProductByIdUseCase(new long[]{quotationItem.productId, quotationItem.productId2}).execute(new RemoteDataSubscriber<Product>(this) {
            @Override
            protected void handleRemoteData(RemoteData<Product> data) {


                if (data.isSuccess() && data.datas.size() > 0) {


                    quotationItem.cost_price_ratio = newRatio;
                    for (Product product : data.datas) {

                        if (quotationItem.productId == product.id)
                            quotationItem.price = QuotationAnalytics.getPriceBaseOnCostRatio(product, newRatio, SharedPreferencesHelper.getInitData().globalData);
                        if (quotationItem.productId2 == product.id)
                            quotationItem.price2 = QuotationAnalytics.getPriceBaseOnCostRatio(product, newRatio, SharedPreferencesHelper.getInitData().globalData);


                    }
                    getView().updateListData();
                }


            }

        });

        getView().showWaiting();
    }

    @Override
    public void loadProductAndCalculatePrice(final QuotationItem quotationItem, final float newRatio, ItemListAdapter<QuotationItem> quotationItemItemListAdapter) {
        //  读取产品数据  重新计算fob值
        UseCaseFactory.getInstance().createGetProductByIdUseCase(new long[]{quotationItem.productId}).execute(new RemoteDataSubscriber<Product>(this) {
            @Override
            protected void handleRemoteData(RemoteData<Product> data) {


                if (data.isSuccess() && data.datas.size() > 0) {
                    Product product = data.datas.get(0);
                    quotationItem.cost_price_ratio = newRatio;
                    quotationItem.price = QuotationAnalytics.getPriceBaseOnCostRatio(product, newRatio, SharedPreferencesHelper.getInitData().globalData);
                    getView().updateListData();
                }


            }
        });
        getView().showWaiting();
    }

    @Override
    public void verify() {


        if (quotationDetail == null) {
            getView().showMessage("数据异常。。。");
            return;
        }


        UseCaseFactory.getInstance().createSaveAndVerifyQuotationDetailUseCase(quotationDetail).execute(new RemoteDataSubscriber<QuotationDetail>(this) {


            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> remoteData) {
                if (remoteData.isSuccess()) {

                    getView().showMessage("审核成功");

                    quotationDetail = remoteData.datas.get(0);
                    getView().bindData(quotationDetail);

                }
            }
        });


        getView().showWaiting("正在保存审核");

    }

    @Override
    public boolean isVerifiedQuotation() {

        return  quotationDetail!=null&&quotationDetail.quotation!=null&&quotationDetail.quotation.isVerified;
    }

    @Override
    public void unVerify() {

        if (quotationDetail == null) {
            getView().showMessage("数据异常。。。");
            return;
        }
        UseCaseFactory.getInstance().createUnVerifyQuotationUseCase(quotationDetail.quotation.id).execute(new RemoteDataSubscriber<QuotationDetail>(this) {


            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> remoteData) {
                if (remoteData.isSuccess()) {

                    getView().showMessage("该报价单审核已经撤销");

                    quotationDetail = remoteData.datas.get(0);
                    getView().bindData(quotationDetail);
                }
            }
        });


        getView().showWaiting("正在撤销审核");
    }
}
