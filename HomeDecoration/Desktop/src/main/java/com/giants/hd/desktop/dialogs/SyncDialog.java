package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.frames.ProductDetailFrame;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.mvp.DialogViewer;
import com.giants.hd.desktop.mvp.IViewer;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.viewImpl.Panel_Sync;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  同步对话框
 */
public class SyncDialog extends BaseDialog<Void>  {


    @Inject
    ApiManager apiManager;


    @Inject
    Panel_Sync panel_photoSync;

    IViewer viewer;

    public SyncDialog(Window window)
    {
        super(window, "数据同步");
        setMinimumSize(new Dimension(400, 400));


        setContentPane(panel_photoSync.getRoot());

        viewer=panel_photoSync;
        init();



    }




    public void init()
    {





        panel_photoSync.btn_erp_sync.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                new HdSwingWorker<Void, Void>(SyncDialog.this) {

                    private  long time=System.currentTimeMillis();
                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {



                        return apiManager.syncErpMaterial();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        if(data.isSuccess())
                        {
                            JOptionPane.showMessageDialog(SyncDialog.this,"ERP材料同步成功,耗时："+((System.currentTimeMillis()-time)/1000)+"秒");
                        }else
                        {
                            JOptionPane.showMessageDialog(SyncDialog.this,data.message);
                        }

                    }
                }.go();
            }
        });




        panel_photoSync.equationUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                UseCaseFactory.getInstance().createSynchronizeProductOnEquationUpdate().execute(new Subscriber<RemoteData<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        viewer.hideLoadingDialog();
                        viewer.showMesssage(e.getMessage());

                    }


                    @Override
                    public void onNext(RemoteData<Void> data) {
                        viewer.hideLoadingDialog();
                        if(data.isSuccess())
                        {

                            viewer.showMesssage("同步已经提交");

                        }else
                        viewer.showMesssage(data.message);



                    }

                });
                viewer.showLoadingDialog();





            }
        });


        panel_photoSync.update_product_statistic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int option= JOptionPane.showConfirmDialog(SyncDialog.this, "是否校正所有产品的统计数据吗？"," 提示", JOptionPane.OK_CANCEL_OPTION);

                if (JOptionPane.OK_OPTION == option) {




                    UseCaseFactory.getInstance().createPostUseCase(HttpUrl.updateAllProductStatistics(), null,Void.class).execute(new RemoteDataSubscriber<Void>(viewer) {


                        @Override
                        protected void handleRemoteData(RemoteData<Void> data) {


                            viewer.showMesssage(data.message);


                        }


                    });
                    viewer.showLoadingDialog("正在校正中.....");



                }


            }
        });

    }





}
