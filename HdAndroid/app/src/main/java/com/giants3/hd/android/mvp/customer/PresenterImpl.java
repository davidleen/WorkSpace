package com.giants3.hd.android.mvp.customer;


import com.giants3.hd.android.events.CustomerUpdateEvent;
import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.noEntity.NameCard;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by davidleen29 on 2018/3/17.
 */

public class PresenterImpl extends BasePresenter<CustomerEditMVP.Viewer, CustomerEditMVP.Model> implements CustomerEditMVP.Presenter {

    @Override
    public CustomerEditMVP.Model createModel() {
        return new ModelImpl();
    }

    @Override
    public void save() {

        Customer customer = getModel().getCustomer();

        if (customer == null) return;

        UseCase useCase = UseCaseFactory.getInstance().createSaveCustomerUseCase(customer);

        getView().showWaiting();
        useCase.execute(new RemoteDataSubscriber<Customer>(this) {
            @Override
            protected void handleRemoteData(RemoteData<Customer> data) {

                if (data.isSuccess()) {


                    getView().showMessage("保存成功");
                    EventBus.getDefault().post(new CustomerUpdateEvent(data.datas.get(0)));


                }

            }


        });


    }

    @Override
    public void initCustomer(Customer customer) {

        if (customer == null) {
            generateCustomerCode();
        }


        getModel().setCustomer(customer);
        bindViewData( );
    }

    @Override
    public void scanNameCard(File newPath) {

        UseCase useCase = UseCaseFactory.getInstance().createScanNameCardUseCase(newPath);

        getView().showWaiting("正在识别名片。。。");


        useCase.execute(new RemoteDataSubscriber<NameCard>(this) {
                            @Override
                            protected void handleRemoteData(RemoteData<NameCard> data) {

                                if (data.isSuccess()) {

                                    getModel().bindNameCard(data.datas.get(0));
                                    bindViewData( );

                                }


                            }

                            @Override
                            protected void handleFail(RemoteData<NameCard> data) {


                                if(data.datas!=null&&data.datas.size()>0)
                                {
                                    getModel().setLastRequestNameCard(data.datas.get(0));
                                    bindViewData( );
                                }



                            }
                        }


        );
    }

    private void generateCustomerCode() {

        UseCase useCase = UseCaseFactory.getInstance().createGenerateCustomerCodeUseCase();

        getView().showWaiting();
        useCase.execute(new RemoteDataSubscriber<String>(this) {
            @Override
            protected void handleRemoteData(RemoteData<String> data) {

                if (data.isSuccess()) {
                    Customer customer = getModel().getCustomer();

                    if (customer == null) {
                        customer = new Customer();
                        getModel().setCustomer(customer);
                    }
                    customer.code = data.datas.get(0);
                    bindViewData( );

                }

            }


        });
    }

    private void bindViewData( ) {

        Customer customer=getModel().getCustomer();
        NameCard lastRequestNameCard=getModel().getLastRequestNameCard();
        getView().bindData(customer,lastRequestNameCard);
    }

    @Override
    public void updateValue(String codeText, String nameText, String telText, String faxText, String emailText, String addressText, String nationText) {


        Customer customer = getModel().getCustomer();

        if (customer == null) {
            customer = new Customer();
            getModel().setCustomer(customer);
        }


        customer.code = codeText;
        customer.name = nameText;
        customer.tel = telText;
        customer.fax = faxText;
        customer.email = emailText;
        customer.addr = addressText;

        customer.nation = nationText;


    }


    @Override
    public void start() {

    }

    @Override
    public void deleteCustomer() {



        Customer customer=getModel().getCustomer();
        if(customer==null||customer.id==0)
            return ;

        UseCase useCase = UseCaseFactory.getInstance().createDeleteCustomerUseCase(customer.id);
        getView().showWaiting();
        useCase.execute(new RemoteDataSubscriber<Void>(this) {
            @Override
            protected void handleRemoteData(RemoteData<Void> data) {

                if (data.isSuccess()) {

                    getView().showMessage("删除成功");
                    EventBus.getDefault().post(new CustomerUpdateEvent());
                    getView().finish();

                }

            }


        });


    }

    @Override
    public void retryLastRequest() {




        NameCard nameCard=getModel().getLastRequestNameCard();
        if(nameCard==null|| StringUtils.isEmpty(nameCard.pictureUrl)) return ;


        UseCase useCase = UseCaseFactory.getInstance().createScanResourceUrlUseCase(nameCard.pictureUrl);
        getView().showWaiting("正在解析资源图片。。。");
        useCase.execute(new RemoteDataSubscriber<NameCard>(this) {
                            @Override
                            protected void handleRemoteData(RemoteData<NameCard> data) {

                                if (data.isSuccess()) {

                                    getModel().bindNameCard(data.datas.get(0));
                                    bindViewData( );

                                }


                            }

                        }


        );




    }
}
