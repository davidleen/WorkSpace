package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.interf.ComonSearch;
import com.giants.hd.desktop.local.DownloadFileManager;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.AppQuotationDetailPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationDetailViewer;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Detail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.logic.AppQuotationAnalytics;
import com.giants3.hd.logic.QuotationAnalytics;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.report.jasper.quotation.AppQuotationPreviewReport;
import com.google.inject.Inject;

import java.awt.*;
import java.io.InputStream;

/**
 * 订单详情界面
 * Created by davidleen29 on 2015/8/24.
 */
public class AppQuotationDetailFrame extends BaseMVPFrame<AppQuotationDetailViewer> implements AppQuotationDetailPresenter {


    @Inject
    ApiManager apiManager;
    private QuotationDetail quotationDetail;

    private String originData;

    public AppQuotationDetailFrame(Quotation quotation) {
        super("广交会报价单:" + quotation.qNumber);


        initPanel(quotation);

    }


    public AppQuotationDetailFrame() {
        super("新增广交会报价单:");


//        quotationDetail= new    QuotationDetail();
//        quotationDetail.quotation=new Quotation();
//        quotationDetail.items=new ArrayList<>();
//        getViewer().bindDetail(quotationDetail);
        createNew();

    }


    @Override
    protected AppQuotationDetailViewer createViewer() {
        return new Panel_AppQuotation_Detail(this);
    }


    private void initPanel(Quotation quotation) {

        setMinimumSize(new Dimension(1080, 800));
        getViewer().bindData(quotation);
        loadDetail(quotation);
    }


    @Override
    public boolean hasModifyData() {

        if (getViewer() == null || quotationDetail == null || originData == null) {
            return false;
        }

        if (GsonUtils.toJson(quotationDetail).equals(originData)) {
            return false;
        }

        return true;
    }

    public void loadDetail(Quotation quotation) {


        UseCaseFactory.getInstance().createGetAppQuotationDetailUseCase(quotation.id, quotation.qNumber).execute(new RemoteDataSubscriber<QuotationDetail>(getViewer()) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                final QuotationDetail quotationDetail = data.datas.get(0);
                setDetail(quotationDetail);
            }
        });

        // getViewer().showLoadingDialog();
    }

    private void setDetail(QuotationDetail quotationDetail) {


        originData = GsonUtils.toJson(quotationDetail);
        AppQuotationDetailFrame.this.quotationDetail = quotationDetail;

        getViewer().bindDetail(quotationDetail);
    }


    public void createNew() {


        UseCaseFactory.getInstance().createNewAppQuotationDetailUseCase().execute(new RemoteDataSubscriber<QuotationDetail>(getViewer()) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                final QuotationDetail quotationDetail = data.datas.get(0);
                setDetail(quotationDetail);
            }
        });

        // getViewer().showLoadingDialog();
    }


    @Override
    public void discountItem(int[] selectRow, float v) {


        AppQuotationAnalytics.discountItem(quotationDetail, selectRow, v);
        getViewer().bindDetail(quotationDetail);

    }

    @Override
    public void addItem(int itemIndex) {

        SearchDialog<Product> dialog = new SearchDialog.Builder().setWindow(getWindow()).setTableModel(new ProductTableModel()).setComonSearch(new ComonSearch<Product>() {
            @Override
            public RemoteData<Product> search(String value, int pageIndex, int pageCount) throws HdException {
                return apiManager.readProductList(value, pageIndex, pageCount);
            }
        }).setValue("").setRemoteData(null).setWindow(getWindow()).createSearchDialog();
        dialog.setMinimumSize(new Dimension(800, 600));
        dialog.pack();

        dialog.setVisible(true);
        Product product = dialog.getResult();
        if (product == null) return;


        AppQuotationAnalytics.addItem(quotationDetail, itemIndex, product);
        getViewer().bindDetail(quotationDetail);
//        UseCaseFactory.getInstance().createAddProductToAppQuotationUseCase(quotationDetail.quotation.id,product.id).execute(new RemoteDataSubscriber<QuotationDetail>(getViewer()) {
//
//            @Override
//            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
//                final QuotationDetail quotationDetail = data.datas.get(0);
//                AppQuotationDetailFrame.this.quotationDetail = quotationDetail;
//                getViewer().bindDetail(quotationDetail);
//            }
//        });


    }

    @Override
    public void deleteItem(int[] itemIndexs) {


        AppQuotationAnalytics.removeItem(quotationDetail, itemIndexs);
        getViewer().bindDetail(quotationDetail);

    }

    @Override
    public void moveUpItem(int itemIndex) {
        AppQuotationAnalytics.moveUpItem(quotationDetail, itemIndex);
        getViewer().bindDetail(quotationDetail);

    }

    @Override
    public void moveDownItem(int itemIndex) {


        AppQuotationAnalytics.moveDownItem(quotationDetail, itemIndex);

        getViewer().bindDetail(quotationDetail);


    }


    @Override
    public void save() {
        if (!hasModifyData()) {
            getViewer().showMesssage("数据无改动");
            return;
        }


        UseCaseFactory.getInstance().createSaveAppQuotationUseCase(quotationDetail).execute(new RemoteDataSubscriber<QuotationDetail>(getViewer()) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                final QuotationDetail quotationDetail = data.datas.get(0);
                setDetail(quotationDetail);
                getViewer().showMesssage("保存成功");
                setFrameResult(FrameResultListener.RESULT_OK);

            }
        });

        getViewer().showLoadingDialog();


    }

    @Override
    public void setCustomer(Customer customer) {


        AppQuotationAnalytics.setCustomerToQuotation(quotationDetail.quotation, customer);
    }

    @Override
    public void setSaleman(User user) {
        AppQuotationAnalytics.setSaleManToQuotation(quotationDetail.quotation, user);
    }

    @Override
    public void updateQNumber(String text) {


        quotationDetail.quotation.qNumber = text;
    }

    @Override
    public void updateQDate(String date) {

        quotationDetail.quotation.qDate = date;

    }

    @Override
    public void updateVDate(String date) {

        quotationDetail.quotation.vDate = date;
    }

    @Override
    public void updateMemo(String text) {

        quotationDetail.quotation.memo = text;
    }

    @Override
    public void updateItemPrice(int itemIndex, float newValue) {

        AppQuotationAnalytics.updateQuotationItemPrice(quotationDetail, itemIndex, newValue);
        getViewer().bindDetail(quotationDetail);

    }

    @Override
    public void updateItemQty(int itemIndex, int newQty) {
        AppQuotationAnalytics.updateQuotationItemQty(quotationDetail, itemIndex, newQty);
        getViewer().bindDetail(quotationDetail);
    }

    @Override
    public void updateItemMemo(int itemIndex, String newValue) {
        AppQuotationAnalytics.updateQuotationItemMemo(quotationDetail, itemIndex, newValue);
        getViewer().bindDetail(quotationDetail);
    }

    @Override
    public void deleteQuotation() {
        UseCaseFactory.getInstance().createDeleteAppQuotationUseCase(quotationDetail.quotation.id).execute(new RemoteDataSubscriber<Void>(getViewer()) {

            @Override
            protected void handleRemoteData(RemoteData<Void> data) {
                if (data.isSuccess()) {
                    getViewer().showMesssage("删除成功");
                    setFrameResult(FrameResultListener.RESULT_OK);
                    dispose();

                }
            }
        });

        getViewer().showLoadingDialog();
    }


    @Override
    public void print() {

        if (hasModifyData()) {

            getViewer().showMesssage("请先保存数据");
            return;
        }

         printFile( );


    }

    private void printFile( ) {
         InputStream inputStream = AppQuotationDetailFrame.class.getClassLoader().getResourceAsStream("jasper/appQuotation.jrxml") ;

         QuotationDetail copy=GsonUtils.fromJson(GsonUtils.toIOs(quotationDetail),QuotationDetail.class);

        for(QuotationItem item:copy.items)
        {
            item.thumbnail= HttpUrl.loadPicture(item.thumbnail);
            item.photoUrl=HttpUrl.loadPicture(item.photoUrl);
//            System.out.println(item.thumbnail);
//            System.out.println(item.photoUrl);
        }

        new AppQuotationPreviewReport(CacheManager.getInstance().bufferData.company, copy,   inputStream ).report();

    }


    @Override
    public boolean hasFocus() {
        return super.hasFocus();
    }

    @Override
    public void viewProduct(long productId) {
        UseCaseFactory.getInstance().createGetProductDetailByIdUseCase(productId).execute(new RemoteDataSubscriber<ProductDetail>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<ProductDetail> data) {


                if (data.isSuccess() && data.datas.size() > 0) {
                    ProductDetail product = data.datas.get(0);
                    HdSwingUtils.showDetailPanel( getWindow(),product);
                }


            }

        });


        getViewer().showLoadingDialog();


    }
}



