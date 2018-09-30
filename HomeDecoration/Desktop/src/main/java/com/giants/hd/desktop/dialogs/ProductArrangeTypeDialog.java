package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.presenter.ProductArrangeTypeIPresenter;
import com.giants.hd.desktop.mvp.viewer.ProductArrangeTypeViewer;
import com.giants.hd.desktop.viewImpl.Panel_ProductArrangeType_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.WorkFlowSubType;
import rx.Subscriber;

import java.awt.*;
import java.util.List;


/**
 *
 * 产品 排厂类型
 * 二级流程 铁件 木件  PU，其他
 *
 * Created by davidleen29 on 2017/1/15.
 */
public class ProductArrangeTypeDialog extends BaseDialog<WorkFlowSubType> implements ProductArrangeTypeIPresenter {



    ProductArrangeTypeViewer viewer;

    private String oldData;

    public ProductArrangeTypeDialog(Window window) {
        super(window);
        setTitle("产品排厂类型");
        setMinimumSize(new Dimension(400, 400));
        viewer = new Panel_ProductArrangeType_List(this);
        setContentPane( viewer.getRoot());

        readProductArrangeTypeList();
    }

    /**
     * 读取外厂家数据
     */
    private void readProductArrangeTypeList() {


        UseCaseFactory.getInstance().createGetWorkFlowSubTypeUseCase().execute(new Subscriber<RemoteData<WorkFlowSubType>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<WorkFlowSubType> data) {

                viewer.hideLoadingDialog();
                if (data.isSuccess()) {
                    setSubTypes(  data.datas);
                }
            }

        });
        viewer.showLoadingDialog();
    }




    @Override
    public void saveData(List<WorkFlowSubType> datas)   {


        if(GsonUtils.toJson(datas).equals(oldData))
        {
            viewer.showMesssage("数据无改变");

            return ;
        }


        UseCaseFactory.getInstance().createSaveWorkFlowSubTypeListUseCase(datas).execute(new Subscriber<RemoteData<WorkFlowSubType>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<WorkFlowSubType> data) {

                viewer.hideLoadingDialog();
                viewer.showMesssage("保存成功");

                if (data.isSuccess()) {
                    setSubTypes(  data.datas);
                }
            }

        });
        viewer.showLoadingDialog();




    }




    private void setSubTypes(List<WorkFlowSubType> subTypes)
    {

        oldData= GsonUtils.toJson(subTypes);

        viewer.setData( subTypes);
    }







}
