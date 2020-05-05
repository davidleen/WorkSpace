package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.presenter.OutFactoryIPresenter;
import com.giants.hd.desktop.mvp.viewer.OutFactoryViewer;
import com.giants.hd.desktop.viewImpl.Panel_OutFactory_LIst;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.entity.WorkFlowProduct;
import rx.Subscriber;

import java.awt.*;
import java.util.List;

/**
 *  外厂对话框
 */
public class OutFactoryDialog extends BaseDialog<WorkFlowProduct> implements OutFactoryIPresenter {



    OutFactoryViewer outFactoryViewer;

    private String oldData;

    public OutFactoryDialog(Window window) {
        super(window);
        setTitle(ModuleConstant.TITLE_OUT_FACTORY);
        setMinimumSize(new Dimension(800, 600));
        outFactoryViewer = new Panel_OutFactory_LIst(this);
        setContentPane( outFactoryViewer.getRoot());

        readOutFactoryData();
    }

    /**
     * 读取外厂家数据
     */
    private void readOutFactoryData() {


        UseCaseFactory.getInstance().createGetOutFactoryUseCase().execute(new Subscriber<RemoteData<OutFactory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                outFactoryViewer.hideLoadingDialog();
                outFactoryViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OutFactory> data) {

                outFactoryViewer.hideLoadingDialog();
                if (data.isSuccess()) {
                    setFactories(  data.datas);
                }
            }

        });
        outFactoryViewer.showLoadingDialog();
    }




    @Override
    public void saveData(List<OutFactory> datas)   {


        if(GsonUtils.toJson(datas).equals(oldData))
        {
            outFactoryViewer.showMesssage("数据无改变");

            return ;
        }


        UseCaseFactory.getInstance().createSaveOutFactoryListUseCase(datas).execute(new Subscriber<RemoteData<OutFactory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                outFactoryViewer.hideLoadingDialog();
                outFactoryViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OutFactory> data) {

                outFactoryViewer.hideLoadingDialog();
                outFactoryViewer.showMesssage("保存成功");

                if (data.isSuccess()) {
                    setFactories(  data.datas);
                }
            }

        });
        outFactoryViewer.showLoadingDialog();




    }




    private void setFactories(List<OutFactory> factories)
    {

        oldData= GsonUtils.toJson(factories);

        outFactoryViewer.setData( factories);
    }







}
