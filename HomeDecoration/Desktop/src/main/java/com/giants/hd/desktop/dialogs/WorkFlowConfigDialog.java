package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.presenter.WorkFlowProductIPresenter;
import com.giants.hd.desktop.utils.Config;
import com.giants.hd.desktop.mvp.viewer.WorkFlowConfigViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlow_Config;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.WorkFlow;
import com.giants3.hd.entity.WorkFlowProduct;
import com.giants3.hd.entity.WorkFlowSubType;
import rx.Subscriber;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品关联的生产流程数据
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowConfigDialog extends BaseMVPDialog<WorkFlowProduct,WorkFlowConfigViewer> implements WorkFlowProductIPresenter {
    WorkFlowConfigViewer workFlowViewer;
    private String oldData;
    private WorkFlowProduct data;
    long productId;
    String productName;

    public WorkFlowConfigDialog(Window window, long productId, String productName) {
        super(window, productName + "产品生产流程配置详情");
        setMinimumSize(new Dimension(800, 600));
        workFlowViewer=getViewer();
        this.productId = productId;
        this.productName = productName;
        final List<WorkFlow> allWorkFlows = null;

        List<WorkFlow> workFlows=new ArrayList<>();
        for(WorkFlow workFlow:allWorkFlows)
        {
            if(workFlow.isSelfMade)
            {
                workFlows.add(workFlow);
            }
        }
        final List<WorkFlowSubType> workFlowSubTypes = CacheManager.getInstance().bufferData.workFlowSubTypes;



        workFlowViewer.setWorkFlows(workFlows, workFlowSubTypes);

         readData();

    }


    public void readData() {

        UseCaseFactory.getInstance().createGetWorkFlowOfProduct(productId).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
            @Override
            public void onCompleted() {
             workFlowViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {
               // workFlowViewer.hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {

                    if (workFlowRemoteData.totalCount > 0)
                        setData(workFlowRemoteData.datas.get(0));
                    else {

                        final WorkFlowProduct data = new WorkFlowProduct();
                        data.productId = productId;
                        setData(data);
                       workFlowViewer.showMesssage("该产品未建立生产进度信息");

                    }
                }


            }


        });
       workFlowViewer.showLoadingDialog();
    }


    public void setData(WorkFlowProduct datas) {
        oldData = GsonUtils.toJson(datas);
        this.data = datas;
        workFlowViewer.setData(datas);
        if(Config.DEBUG)
        {
            Config.log("oldData:"+oldData);
        }
    }




    @Override
    public void save() {


        workFlowViewer.getData(data);


        if (!hasModifyData()) {
            workFlowViewer.showMesssage("数据无改动");
            return;
        }

        UseCaseFactory.getInstance().createSaveWorkProductUseCase(data).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {
                workFlowViewer.hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {
                    final WorkFlowProduct workFlowProduct = workFlowRemoteData.datas.get(0);
                    setData(workFlowProduct);
                    setResult(workFlowProduct);
                    workFlowViewer.showMesssage("保存成功");
                    dispose();

                }


            }


        });
        workFlowViewer.showLoadingDialog();
    }

    @Override
    public boolean hasModifyData() {

        if(Config.DEBUG)
        {
            Config.log("data:"+data+",oldData:"+oldData);
        }

        return data != null && !GsonUtils.toJson(data).equals(oldData);
    }

    @Override
    protected WorkFlowConfigViewer createViewer() {
        return new Panel_WorkFlow_Config(this);
    }
}
