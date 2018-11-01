package com.giants3.hd.android.mvp.appquotationdetail;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.android.print.FilePrintDocumentAdapter;
import com.giants3.hd.android.print.PDFPrintDocumentAdapter;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 广交会报价 P层接口
 * Created by davidleen29 on 2016/9/23.
 */

public class PresenterImpl extends BasePresenter<AppQuotationDetailMVP.Viewer, AppQuotationDetailMVP.Model> implements AppQuotationDetailMVP.Presenter {


    @Override
    public AppQuotationDetailMVP.Model createModel() {
        return new ModelImpl();
    }

    @Override
    public void start() {
        loadCustomer();
    }


    @Override
    public void loadCustomer() {

//        RemoteDataSubscriber<Customer> subscriber = new RemoteDataSubscriber<Customer>(this, true) {
//
//            @Override
//            protected void handleRemoteData(RemoteData<Customer> data) {
//                if (data.isSuccess()) {
//                    getModel().setCustomers(data.datas);
//                }
//            }
//        };
//
//
//        executeUseCase(UseCaseFactory.getInstance().createGetCustomerListUseCase(), subscriber);
    }


    @Override
    public void setQuotationId(long quotationId) {

        UseCase useCase;

        //loadQuotation;
        //createTempQuotation;
        RemoteDataSubscriber<QuotationDetail> useCaseSubscriber = new RemoteDataSubscriber<QuotationDetail>(this) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                if (data.isSuccess()) {
                    getModel().setQuotationDetail(data.datas.get(0));
                    bindData();


                }
            }
        };
        if (quotationId > -1) {


            useCase = UseCaseFactory.getInstance().getAppQuotationDetailCase(quotationId);


        } else {

            //createTempQuotation;
            useCase = UseCaseFactory.getInstance().createTempQuotation();

        }


        executeUseCase(useCase, useCaseSubscriber);


    }


    private void bindData() {

        getView().bindData(getModel().getQuotationDetail());
    }

    private <T> void executeUseCase(UseCase useCase, RemoteDataSubscriber<T> subscriber) {

        //createTempQuotation;
        useCase.execute(subscriber);

    }


    @Override
    public void addNewProduct(long productId,String productName,String pVersion) {


        if(getModel().containsProduct(productId))
        {
            getView().showMessage((StringUtils.isEmpty(pVersion)?productName:(productName+"-"+pVersion))+"已经存在报价单中，报价单添加重复。");
            return ;
        }



        getView().showWaiting();





        RemoteDataSubscriber<Product> useCaseSubscriber = new RemoteDataSubscriber<Product>(this) {

            @Override
            protected void handleRemoteData(RemoteData<Product> data) {
                if (data.isSuccess()) {
                    Product product = data.datas.get(0);
                    getModel().addNewProduct(product);
                    bindData();


                } else {
                    getView().showMessage(data.message);
                }
            }
        };

        UseCaseFactory.getInstance().createGetProductByNameAndVersionUseCase(productName,pVersion).execute(useCaseSubscriber);


    }

    @Override
    public void deleteQuotationItem(int item) {


        getModel().deleteQuotationItem(item);
        bindData();

    }

    @Override
    public void updatePrice(int itm, float newFloatValue) {

        getModel().updateItemPrice(itm, newFloatValue);
        getView().bindData(getModel().getQuotationDetail());

//        QuotationDetail quotationDetail = getModel().getQuotationDetail();
//        long quotationId = quotationDetail.quotation.id;
//
//
//        UseCase useCase = UseCaseFactory.getInstance().createUpdateQuotationItemPriceUseCase(quotationId, itm, newFloatValue);
//
//
//        executeQuotationUseCase(useCase);


    }


    @Override
    public void updateQty(int itm, int newQty) {


        getModel().updateItemQty(itm, newQty);
        getView().bindData(getModel().getQuotationDetail());


//        QuotationDetail quotationDetail = getModel().getQuotationDetail();
//        long quotationId = quotationDetail.quotation.id;
//
//
//        UseCase useCase = UseCaseFactory.getInstance().createUpdateQuotationItemQtyUseCase(quotationId, itm, newQty);
//
//
//        executeQuotationUseCase(useCase);
    }

    @Override
    public void updateMemo(int itm, String memo) {


        getModel().updateItemMemo(itm, memo);
        getView().bindData(getModel().getQuotationDetail());
//        QuotationDetail quotationDetail = getModel().getQuotationDetail();
//        long quotationId = quotationDetail.quotation.id;
//
//
//        UseCase useCase = UseCaseFactory.getInstance().createUpdateQuotationItemMemoUseCase(quotationId, itm, memo);
//
//
//        executeQuotationUseCase(useCase);
    }


    @Override
    public void updateValidateTime(String dateString) {


        getModel().updateValidateTime(dateString);
        bindData();


    }

    @Override
    public void updateCreateTime(String dateString) {
        getModel().updateCreateTime(dateString);
        bindData();


    }

    @Override
    public void updateQuotationMemo(String newValue) {
        getModel().updateQuotationMemo(newValue);

        bindData();
    }


    @Override
    public void updateQuotationNumber(String newValue) {


        getModel().updateQuotationNumber(newValue);
        bindData();
    }


    @Override
    public void updateQuotationBooth(String newValue) {

        getModel().updateQuotationBooth(newValue);
        bindData();
    }

    @Override
    public void cancelAllQuotationDiscount() {


        getModel().cancelAllQuotationDiscount();
        bindData();
    }

    @Override
    public boolean restoreInstance(Bundle restoreInstance) {

        String key_quotationdetail = restoreInstance.getString("KEY_QUOTATIONDETAIL");
        if(StringUtils.isEmpty(key_quotationdetail)) return false;
        QuotationDetail quotationDetail= GsonUtils.fromJson(key_quotationdetail,QuotationDetail.class);
        String  oldData=  restoreInstance.getString("KEY_QUOTATIONDETAIL_OLD") ;
        getModel().restoreQuotationDetail(quotationDetail,oldData);
        bindData();

        return true;
    }

    @Override
    public void saveInstance(Bundle savedInstance) {


        QuotationDetail quotationDetail=getModel().getQuotationDetail();
        String oldData=getModel().getOriginData();
       savedInstance.putString("KEY_QUOTATIONDETAIL",GsonUtils.toJson(quotationDetail));
       savedInstance.putString("KEY_QUOTATIONDETAIL_OLD",oldData);


    }

    @Override
    public void exportQuotationPDF() {

        if (getModel().hasModify()) {
            getView().showMessage("先保存报价单。");
            return;
        }


        QuotationDetail quotationDetail = getModel().getQuotationDetail();


        final String filePath =  StorageUtils.getFilePath(   "quotation"+File.separator + quotationDetail.quotation.qNumber+".pdf");
        File file = new File(filePath);
        if(file.exists())
        {
              file.delete();


        }
        FileUtils.makeDirs(filePath);

        long quotationId = quotationDetail.quotation.id;

        UseCase useCase = UseCaseFactory.getInstance().createPrintQuotationUseCase(quotationId, filePath);

        getView().showWaiting();
        useCase.execute(new RemoteDataSubscriber<Void>(this) {
            @Override
            protected void handleRemoteData(RemoteData<Void> data) {

                if (data.isSuccess()) {


                    getView().showMessage("报价单导出成功，存放在目录:"+filePath);


                }

            }


        });

    }

    @Override
    public void deleteQuotation() {


        getView().showWaiting();
        QuotationDetail quotationDetail = getModel().getQuotationDetail();
        long quotationId = quotationDetail.quotation.id;


        UseCase useCase = UseCaseFactory.getInstance().createDeleteQuotationUseCase(quotationId);

        RemoteDataSubscriber<Void> useCaseSubscriber = new RemoteDataSubscriber<Void>(this) {

            @Override
            protected void handleRemoteData(RemoteData<Void> data) {
                if (data.isSuccess()) {

                    getView().showMessage("删除成功");
                    getView().exit();


                } else {
                    getView().showMessage(data.message);
                }
            }
        };
        useCase.execute(useCaseSubscriber);


    }

    @Override
    public void updateItemDiscount(int itm, float newDisCount) {


        getModel().updateItemDiscount(itm, newDisCount);
        bindData();

    }

    @Override
    public void updateQuotationDiscount(float newDisCount) {
        QuotationDetail quotationDetail = getModel().getQuotationDetail();

        getModel().updateQuotationDiscount( newDisCount);
        bindData();
    }


    @Override
    public void saveQuotation() {


        boolean hasModify = getModel().hasModify();
        if (!hasModify) {

            getView().showMessage("数据无改变");
            return;

        }
        getView().showWaiting();


        QuotationDetail quotationDetail = getModel().getQuotationDetail();


        UseCase useCase = UseCaseFactory.getInstance().createSaveQuotationUseCase(quotationDetail);

        RemoteDataSubscriber<QuotationDetail> useCaseSubscriber = new RemoteDataSubscriber<QuotationDetail>(this) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                if (data.isSuccess()) {
                    getModel().setQuotationDetail(data.datas.get(0));
                    bindData();
                    getView().showMessage("保存成功");
                    getView().setResultOK();

                } else {
                    getView().showMessage(data.message);
                }
            }
        };
        useCase.execute(useCaseSubscriber);
    }

    private void executeQuotationUseCase(UseCase useCase) {
        RemoteDataSubscriber<QuotationDetail> useCaseSubscriber = new RemoteDataSubscriber<QuotationDetail>(this) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                if (data.isSuccess()) {
                    getModel().setQuotationDetail(data.datas.get(0));


                } else {
                    getView().showMessage(data.message);
                }
                bindData();
            }
        };
        useCase.execute(useCaseSubscriber);

    }

    @Override
    public void goBack() {


        if (getModel().hasModify()) {
            getView().showUnSaveAlert();

        } else {

            getView().exit();
        }


    }

    private void onPrintPdf(String filePath) {
        PrintManager printManager = (PrintManager) getView().getContext().getSystemService(Context.PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(PrintAttributes.COLOR_MODE_COLOR);
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);;
          PrintDocumentAdapter documentAdapter = new PDFPrintDocumentAdapter(getView().getContext(), filePath);
//         PrintDocumentAdapter documentAdapter = new FilePrintDocumentAdapter(  filePath);
        printManager.print("test pdf print", documentAdapter, builder.build());
    }

    @Override
    public void printQuotation() {


        if (getModel().hasModify()) {
            getView().showMessage("先保存报价单。");
            return;
        }


        QuotationDetail quotationDetail = getModel().getQuotationDetail();


        final String filePath =  StorageUtils.getFilePath(     "TEMP_.pdf");
        File file = new File(filePath);
        if(file.exists())
        {
            boolean delete = file.delete();

            Log.e("temp file deleted:"+delete);
        }

        long quotationId = quotationDetail.quotation.id;


        UseCase useCase = UseCaseFactory.getInstance().createPrintQuotationUseCase(quotationId, filePath);

        getView().showWaiting();
        useCase.execute(new RemoteDataSubscriber<Void>(this) {
            @Override
            protected void handleRemoteData(RemoteData<Void> data) {

                if (data.isSuccess()) {

                    onPrintPdf(filePath);


                }

            }


        });


    }


    @Override
    public void pickCustomer() {


        List<Customer> customers = getModel().getCustomers();


        Customer current = null;



        getView().chooseCustomer(current, customers);


    }


    @Override
    public void updateCustomer(Customer newValue) {


        getModel().updateCustomer(newValue);
        bindData();



    }


}
