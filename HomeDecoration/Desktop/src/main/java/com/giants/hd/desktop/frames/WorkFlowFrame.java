package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.WorkFlowIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *  生产流程
 *
 * Created by david on 20160303
 */
public class WorkFlowFrame extends BaseInternalFrame implements WorkFlowIPresenter {
    WorkFlowViewer workFlowViewer;


    private String oldData;
    private List<ErpWorkFlow> data;
    public WorkFlowFrame( ) {
        super("生产流程");



        setData(ErpWorkFlow.WorkFlows);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadUsers();
            }
        });



    }


    private  void  loadUsers()
    {

        UseCaseFactory.getInstance().createGetUserListUseCase().execute(new Subscriber<java.util.List<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(java.util.List<User> users) {


                workFlowViewer.setUserList(users);

            }

        });

    }





    public void setData(List<ErpWorkFlow> datas)
    {
        oldData= GsonUtils.toJson(datas);
        this.data=datas;
        workFlowViewer.setData(datas);
    }


    @Override
    protected Container getCustomContentPane() {
        workFlowViewer =new Panel_Work_Flow( this);
        return workFlowViewer.getRoot();
    }


    @Override
    public void save( ) {

//        if(!hasModifyData())
//        {
//            workFlowViewer.showMesssage("数据无改动");
//            return;
//        }
//
//        UseCaseFactory.getInstance().createSaveWorkFlowUseCase(data).execute(new Subscriber<RemoteData<WorkFlow>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                workFlowViewer.hideLoadingDialog();
//                workFlowViewer.showMesssage(e.getMessage());
//            }
//
//
//            @Override
//            public void onNext(RemoteData<WorkFlow> workFlowRemoteData) {
//                workFlowViewer.hideLoadingDialog();
//                if(workFlowRemoteData.isSuccess())
//                {
//                    setData(workFlowRemoteData.datas);
//                    workFlowViewer.showMesssage("保存成功");
//                }
//
//
//            }
//
//
//        });
//        workFlowViewer.showLoadingDialog();
    }

    @Override
    public boolean hasModifyData() {

      return   data!=null && !GsonUtils.toJson(data).equals(oldData);
    }
}
