package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.AppQuotationSyncPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationSyncViewer;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Sync;
import com.giants3.hd.domain.interractor.UseCase;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;

/**
 * Created by david on 2015/11/23.
 */
public class AppQuotationSyncFrame extends BaseMVPFrame<AppQuotationSyncViewer> implements AppQuotationSyncPresenter {


    public AppQuotationSyncFrame() {
        super(ModuleConstant.TITLE_APP_QUOTATION_SYNC);


    }


    @Override
    protected AppQuotationSyncViewer createViewer() {
        return new Panel_AppQuotation_Sync(this);
    }

    @Override
    public void beginAsync(String urlHead, String startDate, String endDate) {


        final UseCase syncAppQuotationUseCase = UseCaseFactory.getInstance().createSyncAppQuotationUseCase(urlHead, startDate, endDate);

        syncAppQuotationUseCase.execute(new RemoteDataSubscriber<Void>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData data) {

                if (data.isSuccess()) {
                    getViewer().showMesssage("同步成功==" + data.message);
                } else {

                    getViewer().showMesssage(data.message);
                }

            }

        });

        getViewer().showLoadingDialog();
    }

    @Override
    public void beginAsyncPicture(String remoteResource,String filterKey,boolean shouldOverride) {


        final UseCase syncProductPictureUseCase = UseCaseFactory.getInstance().createSyncProductPictureUseCase(remoteResource,filterKey,shouldOverride);

        syncProductPictureUseCase.execute(new RemoteDataSubscriber<Void>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData data) {

                if (data.isSuccess()) {
                    getViewer().showMesssage("同步成功图片成功==" + data.message);
                } else {

                    getViewer().showMesssage(data.message);
                }

            }

        });

        getViewer().showLoadingDialog();






    }


    @Override
    public void beginAsyncProduct(String remoteResource, String filterKey, boolean shouldOverride) {

        final UseCase syncProductPictureUseCase = UseCaseFactory.getInstance().createSyncProductInfoUseCase(remoteResource,filterKey,shouldOverride);

        syncProductPictureUseCase.execute(new RemoteDataSubscriber<Void>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData data) {

                if (data.isSuccess()) {
                    getViewer().showMesssage("同步产品数据成功==" + data.message);
                } else {

                    getViewer().showMesssage(data.message);
                }

            }

        });

        getViewer().showLoadingDialog();

    }

    @Override
    public void initData() {
        final UseCase useCase = UseCaseFactory.getInstance().createInitGjhDataUseCase( );

        useCase.execute(new RemoteDataSubscriber<Void>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData data) {

                if (data.isSuccess()) {
                    getViewer().showMesssage("初始化成功" + data.message);
                } else {

                    getViewer().showMesssage(data.message);
                }

            }

        });

        getViewer().showLoadingDialog();
    }
}
